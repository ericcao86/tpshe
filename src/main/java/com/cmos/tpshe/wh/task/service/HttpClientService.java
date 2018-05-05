package com.cmos.tpshe.wh.task.service;


import com.cmos.tpshe.wh.task.bean.WhReturnObject;
import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import com.cmos.tpshe.wh.task.util.PBESecret;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cyh
 * @Date 16:09 2018/4/23
 * @description
 * @since 2.0
 */
public class HttpClientService {


    private static final String APPLICATION_JSON = "application/json";

    private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);

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

    public  void testVoice(WhTaskTel del) {
        logger.debug("send voice msg start----------------------------");
        Map<String, Object> param = new HashMap<>();
        String telnum;
        if(StringUtils.equals("product",env)){//生产环境不用加0
            telnum = del.getTelnum();
        }else{
            telnum = "0"+del.getTelnum();
        }
        param.put("destNumber", telnum);
        param.put("callerNumber", "10085");
        param.put("mediaType", "smartCall");
        param.put("accountId", companyCode);
        param.put("callbackUrl", "");//回调地址
        param.put("callId", "");
        param.put("count", "");
        param.put("provinceCode", "371");
        param.put("scCode", del.getTaskId());
        JSONObject json = JSONObject.fromObject(param);
        JSONObject jsons = JSONObject.fromObject("{}");
        jsons.put("params", json.toString());
        logger.info("-----------params is {}",jsons.toString());
        // 企业编码进行Base64加密
        byte[] key = companyCode.getBytes();
        logger.info("-----------key is {}",key.toString());
        String keys = new String(PBESecret.encryptBase64(key));
        logger.info("-----------keys is {}",keys.toString());
        // 用企业秘钥对参数内容加密
        byte[] str = PBESecret.encryptPBE(jsons.toString().getBytes(), companysecretKey, PBESecret.initSalt());
        String info = new String(PBESecret.encryptBase64(str));
        logger.info("-----------info is {}",info.toString());
        // 调用http请求进行接口访问求
        String result = httpPostWithJSONCompanyCode(url, info, keys);
        byte[] decode64Bytes =PBESecret.decryptBase64(result);
        byte[] decodePBEBytes =PBESecret.decryptPBE(decode64Bytes, companysecretKey, PBESecret.initSalt());
        result = new String(decodePBEBytes);

        try {
            result = URLDecoder.decode(result, "utf-8");
            logger.info("return result is {}",result);
            WhReturnObject returnObject = mapper.readValue(result,WhReturnObject.class);
            logger.info("return object is {}",returnObject.toString());
        }catch (UnsupportedEncodingException e){
           logger.error(e.getMessage());
        }catch (IOException d){
            logger.error(d.getMessage());
        }


        logger.info("send voice msg end----------------------------");


    }

    private   String httpPostWithJSONCompanyCode(String url, String json, String companyCode){
        DefaultHttpClient httpClient = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("errorMsg", "下发连接异常");
        JSONObject js = JSONObject.fromObject(resultMap);
        String result = "";
        try {
            httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 40000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 40000);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authorization", companyCode);
            StringEntity se = new StringEntity(json, "application/json", "utf-8");
            httpPost.setEntity(se);
            HttpResponse response;
            response = httpClient.execute(httpPost);
            // 判定返回状态是否成功
            int flag = response.getStatusLine().getStatusCode();
            logger.info("response status is {} ",flag);
            if (flag == 200) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                result = js.toString();
            }
        } catch (ClientProtocolException e) {
            result = js.toString();
        } catch (IOException e) {
            result = js.toString();
        } finally {

            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return result;
    }
}


