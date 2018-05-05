package com.cmos.tpshe.wh.task.bean;

/**
 * @author cyh
 * @Date 14:32 2018/4/25
 * @description
 * @since 2.0
 */
public class ReturnObject {

    private String returnCode;
    private String returnMsg;
    private ReturnBean bean;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public ReturnBean getBean() {
        return bean;
    }

    public void setBean(ReturnBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", bean=" + bean +
                '}';
    }
}
