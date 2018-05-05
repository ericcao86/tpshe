package com.cmos.tpshe.wh.task.bean;

import java.io.Serializable;

/**
 * @author cyh
 * @Date 16:28 2018/4/25
 * @description
 * @since 2.0
 */
public class WhReturnObject implements Serializable{

    private String rtnCode;
    private String rtnMsg;
    private WhReturnBean bean;

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public WhReturnBean getBean() {
        return bean;
    }

    public void setBean(WhReturnBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "WhReturnObject{" +
                "rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                ", bean=" + bean +
                '}';
    }
}
