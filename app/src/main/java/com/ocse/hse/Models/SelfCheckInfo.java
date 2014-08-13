package com.ocse.hse.Models;

import java.io.Serializable;

/**
 * Created by leehaining on 14-7-3.
 */
public class SelfCheckInfo implements Serializable {
    private String checkName;
    private int checkRecordsTotal;
    private String checkTime;
    public SelfCheckInfo(String name,int total,String time)
    {
        checkName="";
        checkRecordsTotal=0;
        checkTime="";

        checkName=name;
        checkRecordsTotal=total;
        checkTime=time;
    }
    public String getCheckName()
    {
        return checkName;
    }
    public String getCheckTime()
    {
        return checkTime;
    }
    public String getCheckRecordsTotal()
    {
        return Integer.toString(checkRecordsTotal);
    }
}
