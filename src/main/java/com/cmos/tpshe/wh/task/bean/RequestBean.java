package com.cmos.tpshe.wh.task.bean;

/**
 * @author cyh
 * @Date 9:17 2018/4/24
 * @description
 * @since 2.0
 */
public class RequestBean {

    private String id;

    private String call_dst_id;
    private String privateData;

    public String getCall_dst_id() {
        return call_dst_id;
    }

    public void setCall_dst_id(String call_dst_id) {
        this.call_dst_id = call_dst_id;
    }

    public String getPrivateData() {
        return privateData;
    }

    public void setPrivateData(String privateData) {
        this.privateData = privateData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
