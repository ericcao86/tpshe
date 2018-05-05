package com.cmos.tpshe.wh.task.dao;

import com.cmos.tpshe.wh.task.entity.WhTaskTel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cyh
 * @Date 14:24 2018/4/19
 * @description
 * @since 2.0
 */
public interface WhTaskTelDao {
    int save(WhTaskTel taskTel);
    int updateStateById(@Param("state") String state, @Param("id") Integer id);
    List<WhTaskTel> queryByTaskId(@Param("taskId") Integer taskId);
    WhTaskTel queryByTelAndTask(@Param("telnum") String telnum,@Param("taskId") String taskId);
    int deleteById(@Param("id") Integer id);
    List<WhTaskTel> queryAll();



}
