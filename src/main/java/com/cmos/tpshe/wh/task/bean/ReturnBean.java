package com.cmos.tpshe.wh.task.bean;

/**
 * @author cyh
 * @Date 8:50 2018/4/24
 * @description
 * @since 2.0
 */
public class ReturnBean {

    private String telnum;
    private String taskId;
    private String card_id;
    private String level_coding;

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getLevel_coding() {
        return level_coding;
    }

    public void setLevel_coding(String level_coding) {
        this.level_coding = level_coding;
    }

    @Override
    public String toString() {
        return "ReturnBean{" +
                "telnum='" + telnum + '\'' +
                ", taskId='" + taskId + '\'' +
                ", card_id='" + card_id + '\'' +
                ", level_coding='" + level_coding + '\'' +
                '}';
    }
}
