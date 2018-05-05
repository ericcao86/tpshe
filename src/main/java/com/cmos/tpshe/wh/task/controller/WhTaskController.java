package com.cmos.tpshe.wh.task.controller;

import com.cmos.tpshe.wh.task.bean.RequestBean;
import com.cmos.tpshe.wh.task.bean.RequestTestBean;
import com.cmos.tpshe.wh.task.bean.ReturnBean;
import com.cmos.tpshe.wh.task.bean.ReturnObject;
import com.cmos.tpshe.wh.task.entity.WhTask;
import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import com.cmos.tpshe.wh.task.service.WhTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author cyh
 * @Date 15:03 2018/4/19
 * @description
 * @since 2.0
 */
@Controller
@RequestMapping("/wh/task")
public class WhTaskController {

    @Autowired
    private WhTaskService taskService;

    @RequestMapping("/index")
    public String index(Model model){
        List<WhTask> tasks = taskService.queryAll();
        model.addAttribute("tasks",tasks);
        return "index";
    }

    @RequestMapping("/getTasks")
    @ResponseBody
    public  List<WhTask> getTasks(Model model){
        List<WhTask> tasks = taskService.queryAll();
        return tasks;
    }




    @RequestMapping("/create")
    public String create(Model model){
        return "create";
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public String save(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file)throws IOException{

        String fileName =file.getOriginalFilename();

        System.out.println("task "+ request.getParameter("taskName"));
        taskService.save(request, file);
        return "create";
    }


    @RequestMapping(value = "/getTelTask",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject getTelTask(@RequestBody RequestBean requestBean){
        ReturnObject object = new ReturnObject();
        object.setReturnCode("0");
        ReturnBean bean = new ReturnBean();
        WhTaskTel tel = taskService.queryByTelAndTask(requestBean.getPrivateData(),requestBean.getCall_dst_id());
        if(tel == null){
            object.setReturnMsg("无数据");
            object.setBean(bean);
            return object;
        }else{
            object.setReturnCode("0");
            object.setReturnMsg("成功");
            bean.setTaskId(tel.getTaskId());
            bean.setCard_id(tel.getCardNum());
            bean.setLevel_coding(tel.getLevel());
            bean.setTelnum(tel.getTelnum());
            object.setBean(bean);
        }
        return object;
    }

    @RequestMapping(value = "/deleteAll",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObject deleteAll(){
        ReturnObject object = new ReturnObject();
        object.setReturnCode("0");
        ReturnBean bean = new ReturnBean();
        object.setReturnMsg("成功");
        object.setBean(bean);
        taskService.deleteAll();
        return object;
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean test(@RequestBody RequestTestBean testBean){
        ReturnBean bean = new ReturnBean();
        taskService.testVoice(testBean);
        return bean;
    }


    @RequestMapping(value = "/del",method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean del(@RequestBody Map<String,Object> params){
        ReturnBean bean = new ReturnBean();
        String id = params.get("id").toString();
        Integer i = Integer.parseInt(id);
        taskService.delById(i);
        return bean;
    }






}
