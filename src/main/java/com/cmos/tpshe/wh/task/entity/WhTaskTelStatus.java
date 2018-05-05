package com.cmos.tpshe.wh.task.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * @author cyh
 * @Date 11:01 2018/4/26
 * @description
 * @since 2.0
 */
public enum WhTaskTelStatus {

    WAIT("0","等待执行"),
    SUCCESS("1","执行成功"),
    ERROR("2","执行失败"),
    DELETE("3","已删除");

    private String value;
    private String name;

    private WhTaskTelStatus(String value,String name){
        this.value = value;
        this.name = name;
    }

    public static String getName(String value){
        for(WhTaskTelStatus v : WhTaskTelStatus.values()){
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
