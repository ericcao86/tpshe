package com.cmos.tpshe.wh.task;

import com.cmos.tpshe.wh.task.util.PBESecret;

/**
 * @author cyh
 * @Date 11:17 2018/4/24
 * @description
 * @since 2.0
 */
public class Test {
    public static void main(String ARGS []){
        String code = "C180409G2ZJ";
        byte[] key = code.getBytes();
        String keys = new String(PBESecret.encryptBase64(key));
        System.out.println(keys);
    }
}
