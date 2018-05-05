package com.cmos.tpshe.wh.task.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * @author cyh
 * @Date 10:52 2018/4/26
 * @description
 * @since 2.0
 */
public enum  WhTaskStatus {

    WAIT("0","等待运行"),
    RUN("1","运行中"),
    STOP("2","停止/中断"),
    FINISH("3","结束");


    private String value;
    private String name;

    private WhTaskStatus(String value,String name){
        this.value = value;
        this.name = name;
    }

    public static String getName(String value){
        for(WhTaskStatus v :WhTaskStatus.values()){
            if(StringUtils.equals(v.getValue(),value)){
                return v.getName();
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
