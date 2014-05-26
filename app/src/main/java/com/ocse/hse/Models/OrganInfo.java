package com.ocse.hse.Models;

import java.io.Serializable;

/**
 * Created by leehaining on 14-5-22.
 */
public class OrganInfo implements Serializable{
    private String organID;//单位编码
    private String organName;//单位名称
    private String organParentID;//上级单位编码
    private String organParentName;//上级单位名称
    public OrganInfo(String organID,String organName,String organParentID,String organParentName)
    {
        this.organID=organID;
        this.organName=organName;
        this.organParentID=organParentID;
        this.organParentName=organParentName;
    }
    public String getOrganID(){
        return this.organID;
    }
    public String getOrganName(){
        return this.organName;
    }
    public String getOrganParentID(){
        return this.organParentID;
    }
    public String getOrganParentName(){
        return this.organParentName;
    }
}
