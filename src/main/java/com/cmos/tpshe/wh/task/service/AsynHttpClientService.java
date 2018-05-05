package com.cmos.tpshe.wh.task.service;

import com.cmos.tpshe.wh.task.bean.WhReturnObject;
import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import com.cmos.tpshe.wh.task.entity.WhTaskTelStatus;
import com.cmos.tpshe.wh.task.util.PBESecret;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cyh
 * @Date 8:34 2018/4/26
 * @description
 * @since 2.0
 */
@Service
public class AsynHttpClientService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AsyncRestTemplate restTemplate;

    @Value("${company.code}")
    public String companyCode;

    @Value("${company.secretKey}")
    public String companysecretKey;

    @Value("${company.url}")
    public String url;

    @Value("${tps.environment}")
    private String env;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    public void setHttpClient(AsyncClientHttpRequestFactory factory){
        restTemplate = new AsyncRestTemplate(factory);
    }

    @Autowired
    private WhTaskService whTaskService;

    public void sendHttpRequest(WhTaskTel tel){
        logger.info("send http request start......");
        request(restTemplate,tel);
        logger.info("send http request end......");
    }

    public void request(AsyncRestTemplate restTemplate, final WhTaskTel tel){
        ListenableFuture<ResponseEntity<byte[]>> future = doRequest(restTemplate,tel);
        if(future == null){
            logger.debug("ListenableFuture is null, url is {}", url);
            return ;
        }

        SuccessCallback<ResponseEntity<byte[]>> callback = new SuccessCallback<ResponseEntity<byte[]>>() {
            @Override
            public void onSuccess(ResponseEntity<byte[]> responseEntity) {
              if(responseEntity.getStatusCode() == HttpStatus.OK){
               String body = new String(responseEntity.getBody());
               //解密
                  byte[] decode64Bytes =PBESecret.decryptBase64(body);
                  byte[] decodePBEBytes =PBESecret.decryptPBE(decode64Bytes, companysecretKey, PBESecret.initSalt());
                  body = new String(decodePBEBytes);
                  try {
                      body = URLDecoder.decode(body, "utf-8");
                      logger.info("return body is {}",body);
                      WhReturnObject returnObject = mapper.readValue(body,WhReturnObject.class);
                      if(StringUtils.equals(returnObject.getRtnCode(),"0")){//
                          whTaskService.updateTelStatus(tel.getId(), WhTaskTelStatus.SUCCESS.getValue());
                      }
                      logger.info("return object is {}",returnObject.toString());
                  }catch (UnsupportedEncodingException e){
                      logger.error(e.getMessage());
                  }catch (IOException d){
                      logger.error(d.getMessage());
                  }

              }else{
                  whTaskService.updateTelStatus(tel.getId(), WhTaskTelStatus.ERROR.getValue());
                  logger.error("return from server error code is {}",responseEntity.getStatusCode());
              }
            }
        };

        FailureCallback failureCallback = new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("Request http api fail, url is {} error is {}", url, throwable.getMessage());
            }
        };

        future.addCallback(callback,failureCallback);


    }


    protected  ListenableFuture<ResponseEntity<byte[]>> doRequest(AsyncRestTemplate restTemplate,WhTaskTel tel){
        String body = buildParams(tel);
        HttpEntity<String> entity = new HttpEntity<>(body, requestJsonHeaders());
        return  restTemplate.postForEntity(url,entity,byte[].class);
    }

    private String buildParams(WhTaskTel tel){

        Map<String, Object> param = new HashMap<>();
        String telnum;
        if(StringUtils.equals("product",env)){//生产环境不用加0
            telnum = tel.getTelnum();
        }else{
            telnum = "0"+tel.getTelnum();
        }
        param.put("destNumber", telnum);
        param.put("callerNumber", "10085");
        param.put("mediaType", "smartCall");
        param.put("accountId", companyCode);
        param.put("callbackUrl", "");//回调地址
        param.put("callId", "");
        param.put("count", "");
        param.put("provinceCode", "371");
        param.put("scCode", tel.getTaskId());
        JSONObject json = JSONObject.fromObject(param);
        JSONObject jsons = JSONObject.fromObject("{}");
        jsons.put("params", json.toString());
        logger.info("request params is {}",jsons.toString());
        byte[] str = PBESecret.encryptPBE(jsons.toString().getBytes(), companysecretKey, PBESecret.initSalt());
        String info = new String(PBESecret.encryptBase64(str));
        return info;

    }

    /**
     * 头部信息
     * @return
     */
    protected HttpHeaders requestJsonHeaders(){
        byte[] key = companyCode.getBytes();
        String keys = new String(PBESecret.encryptBase64(key));
        HttpHeaders headers= new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", keys);
        return headers;
    }




}
