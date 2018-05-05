package com.cmos.tpshe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cyh
 * @Date 13:49 2018/4/20
 * @description
 * @since 2.0
 */
public class Test {
    public static void main(String A[]) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:00");
        String S = format.format(new Date());
        Date d = format.parse(S);
        System.out.println(d.toString());
    }


}
