package com.cmos.tpshe.wh.task.service;

import com.cmos.tpshe.wh.task.bean.RequestTestBean;
import com.cmos.tpshe.wh.task.dao.WhTaskDao;
import com.cmos.tpshe.wh.task.dao.WhTaskTelDao;
import com.cmos.tpshe.wh.task.entity.WhTask;
import com.cmos.tpshe.wh.task.entity.WhTaskStatus;
import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import com.cmos.tpshe.wh.task.entity.WhTaskTelStatus;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cyh
 * @Date 14:58 2018/4/19
 * @description
 * @since 2.0
 */
@Service
public class WhTaskService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WhTaskDao taskDao;

    @Autowired
    private WhTaskTelDao taskTelDao;


    @Autowired
    private AsynHttpClientService asynHttpClientService;


    public int saveTask(WhTask task) {
        return taskDao.save(task);
    }

    public int saveTel(WhTaskTel taskTel) {
        WhTaskTel task = taskTelDao.queryByTelAndTask(taskTel.getTelnum(),taskTel.getTaskId());
        if(task == null){
            return taskTelDao.save(taskTel);
        }else{
            return 0;
        }

    }


    public int updateTelStatus(Integer id,String status){
        return taskTelDao.updateStateById(status,id);
    }


    public void deleteAll(){
        List<WhTask> tasks= taskDao.queryAll();
        for(WhTask task:tasks){
            taskDao.deleteById(task.getId());
        }
        List<WhTaskTel> tels = taskTelDao.queryAll();
        for(WhTaskTel t :tels){
            taskTelDao.deleteById(t.getId());
        }
    }

    public int delById(Integer id){
        List<WhTaskTel> taskTels =  taskTelDao.queryByTaskId(id);
        for(WhTaskTel tel:taskTels){
            taskTelDao.updateStateById(WhTaskTelStatus.DELETE.getValue(),tel.getId());
        }
        return taskDao.deleteById(id);
    }



    public List<WhTask> queryAll() {
        List<WhTask> whTasks = taskDao.queryAll();
        List<WhTask> resTasks = new ArrayList<>();
        for(WhTask task :whTasks){
            task.setState(WhTaskStatus.getName(task.getState()));
            resTasks.add(task);
        }
        return resTasks;
    }

    public List<WhTask> queryByTaskTime(Date taskTime){
        return taskDao.queryByTaskTime(taskTime);
    }

    public List<WhTaskTel> queryByTaskId(Integer taskId){
        return taskTelDao.queryByTaskId(taskId);
    }

    public WhTaskTel queryByTelAndTask(String taskId,String tel){
        logger.info("taskId is {} and telnum is {}",taskId,tel);
      return taskTelDao.queryByTelAndTask(tel,taskId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(HttpServletRequest request, MultipartFile file)throws IOException {
        WhTask task= saveTaskForm(request);
        saveTaskTelFile(file,task);
    }


    private WhTask saveTaskForm(HttpServletRequest request) {
        WhTask task = new WhTask();
        task.setConcurrency(request.getParameter("concurrency"));
        task.setCreateTime(new Date());
        task.setState(WhTaskStatus.WAIT.getValue());
        task.setDescr(WhTaskStatus.WAIT.getName());
        task.setTaskName(request.getParameter("taskName"));
        task.setTaskId(request.getParameter("taskId"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date date = f.parse(request.getParameter("taskTime"));
            task.setTaskTime(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        saveTask(task);
        return task;

    }

    private WhTaskTel saveTaskTelFile(MultipartFile file,WhTask task)throws IOException {
        WhTaskTel taskTel =null;
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = workbook.getSheetAt(0); //获取第一个sheet
        for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {// getLastRowNum，获取最后一行的行标
            System.out.println(sheet.getLastRowNum()+"------------------------");
            HSSFRow row = sheet.getRow(j);
            if (row != null) {
                if(row.getCell(0) !=null){
                    DecimalFormat format = new DecimalFormat("#");
                    Number value = row.getCell(0).getNumericCellValue();
                    String phone = format.format(value);
                    String level = row.getCell(1).getStringCellValue();
                    Number number = row.getCell(2).getNumericCellValue();
                    String cardNum =format.format(number);
                    taskTel = setTelFromTask(task,phone,level,cardNum);

                    saveTel(taskTel);
                }
            }
        }


        return taskTel;

    }

    private WhTaskTel setTelFromTask(WhTask task,String phone,String level,String cardNum){
        WhTaskTel taskTel = new WhTaskTel();
        taskTel.setCreateTime(new Date());
        taskTel.setState(WhTaskTelStatus.WAIT.getValue());
        taskTel.setTaskId(task.getTaskId());
        taskTel.setUpdateTime(new Date());
        taskTel.setTelnum(phone);
        taskTel.setLevel(level);
        taskTel.setCardNum(cardNum);
        taskTel.setWhId(task.getId());
        taskTel.setDescr(WhTaskTelStatus.WAIT.getName());
        return taskTel;
    }

    public void testVoice(RequestTestBean testBean){
        WhTaskTel tel = new WhTaskTel();
        tel.setTaskId(testBean.getTaskId());
        tel.setTelnum(testBean.getTelnum());
        asynHttpClientService.sendHttpRequest(tel);

    }
}