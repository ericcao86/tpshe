package com.cmos.tpshe.wh.task.bean;

import java.io.Serializable;

/**
 * @author cyh
 * @Date 16:30 2018/4/25
 * @description
 * @since 2.0
 */
public class WhReturnBean implements Serializable {

    private String rcode;
    private String result;
    private String resultCode;
    private String reason;

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "WhReturnBean{" +
                "rcode='" + rcode + '\'' +
                ", result='" + result + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
