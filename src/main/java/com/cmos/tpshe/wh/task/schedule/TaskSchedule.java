package com.cmos.tpshe.wh.task.schedule;

import com.cmos.tpshe.wh.task.entity.WhTask;
import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import com.cmos.tpshe.wh.task.service.AsynHttpClientService;
import com.cmos.tpshe.wh.task.service.HttpClientService;
import com.cmos.tpshe.wh.task.service.WhTaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author cyh
 * @Date 13:55 2018/4/20
 * @description
 * @since 2.0
 */
@Component
@EnableScheduling
@Configurable
public class TaskSchedule {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WhTaskService taskService;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private ConcurrentHashMap<String,Object> pool = new ConcurrentHashMap();

    @Autowired
    private AsynHttpClientService asynHttpClientService;

    @Value("${tps.environment}")
    private String env;

    @Scheduled(cron ="0 0/1 * * * ?")
    public void sendRequest(){

        Date nowDate = getNowDateIgnoreSS();
        logger.info("start schedule now date is {}",nowDate);
        List<WhTask> tasks = taskService.queryByTaskTime(nowDate);
        logger.info("task size is {}",tasks.size());
        if(!tasks.isEmpty()){
            buildPool(tasks);
            sendHttpMsg();
        }
        logger.info("schedule end-------------------");

    }

   private Date getNowDateIgnoreSS(){
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
       String s = format.format(new Date());
       Date d=null;
       try {
            d = format.parse(s);
       }catch (ParseException e){
          System.out.println(e.getMessage());
       }
      return d;
   }



   public void sendHttpMsg(){
           int i = 1;
           if(pool.size()>0){
               for(Map.Entry<String,Object> e: pool.entrySet() ){
                   WhTaskTel tel = (WhTaskTel)e.getValue();
                   if(i % 50 == 0){
                       try {
                           Thread.sleep(1 * 60 * 1000l);
                       }catch (InterruptedException f){
                           logger.error("current thread is interrupt error msg is {}",f.getMessage());
                       }
                   }else{
                       asynHttpClientService.sendHttpRequest(tel);
                       pool.remove(e.getKey());

                   }
               }
           }
       i++;


   }

   private void buildPool(List<WhTask> tasks){
       if(tasks.isEmpty()){
           return;
       }else{
           for(WhTask task:tasks){
               List<WhTaskTel> tels = taskService.queryByTaskId(task.getId());
               if(!tels.isEmpty()){
                   for(WhTaskTel del:tels){
                       pool.put(del.getTelnum()+del.getTaskId(),del);
                   }
               }
           }
       }


   }

//    @Scheduled(cron ="0 0/3 * * * ?")
//    public void testTel(){
//        if(!StringUtils.equals("product",env)){
//            logger.info("schedule start- with test-----------------");
//            List<WhTaskTel> taskTels =buildTask();
//            for(WhTaskTel del:taskTels){
//
//                // clientService.testVoice(del);
//                asynHttpClientService.sendHttpRequest(del);
//
//            }
//        }
//
//            }



//    @Scheduled(cron ="0 10,30,50 0/1 * * ?")
//    public void testWh010(){
//        logger.info("schedule start- with ,wh010-----------------");
//        WhTaskTel tel = new WhTaskTel();
//        tel.setTelnum("015156822665");
//        tel.setTaskId("wh010");
//        clientService.testVoice(tel);
//        logger.info("schedule end with ,wh010-----------------");
//    }

    private List<WhTaskTel> buildTask(){
        List<WhTaskTel> taskTels = new ArrayList<>();
//        WhTaskTel wh003 = new WhTaskTel();
//        wh003.setTaskId("wh003");
//        wh003.setTelnum("15036120781");
//        taskTels.add(wh003);
//        WhTaskTel wh010 = new WhTaskTel();
//        wh010.setTelnum("15156822665");
//        wh010.setTaskId("wh010");
//        taskTels.add(wh010);
//        WhTaskTel wh011 = new WhTaskTel();
//        wh011.setTaskId("wh011");
//        wh011.setTelnum("18770093159");
//        taskTels.add(wh011);
        WhTaskTel wh001 = new WhTaskTel();
        wh001.setTelnum("18845341443");
        wh001.setTaskId("wh003");
        taskTels.add(wh001);
        return taskTels;
    }


}
