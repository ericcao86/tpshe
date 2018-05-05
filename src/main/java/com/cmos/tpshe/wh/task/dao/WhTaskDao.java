package com.cmos.tpshe.wh.task.dao;

import com.cmos.tpshe.wh.task.entity.WhTask;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author cyh
 * @Date 14:18 2018/4/19
 * @description
 * @since 2.0
 */
public interface WhTaskDao {

    int save(WhTask task);
    List<WhTask> queryAll();
    int deleteById(@Param("id") Integer id);
    int updateStateById(@Param("state") String state, @Param("id") Integer id);
    List<WhTask> queryByTaskTime(@Param("taskTime") Date taskTime);
    WhTask queryById(@Param("id") Integer id);


}
