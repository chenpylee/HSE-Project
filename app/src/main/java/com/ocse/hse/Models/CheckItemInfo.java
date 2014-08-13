package com.ocse.hse.Models;

import java.io.Serializable;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckItemInfo implements Serializable{
    private String sectionName;

    private boolean isHeader;
    private String task_id;
    private String organ_id;
    private String rule_lv1;
    private String rule_lv2;
    private String rule_lv3;
    private String rule_content;
    private String rule_status;

    public CheckItemInfo(Boolean isHeader,String sectionName,String task_id,String organ_id,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content,String rule_status)
    {
        this.isHeader=isHeader;
        this.sectionName=sectionName;
        this.task_id=task_id;
        this.organ_id=organ_id;
        this.rule_lv1=rule_lv1;
        this.rule_lv2=rule_lv2;
        this.rule_lv3=rule_lv3;
        this.rule_content=rule_content;
        this.rule_status=rule_status;
    }
    public boolean getIsHeader()
    {
        return isHeader;
    }
    public String getSectionName()
    {
        return sectionName;
    }
    public String getTaskID()
    {
        return task_id;
    }
    public String getOrganID()
    {
        return organ_id;
    }
    public String getRuleLv1()
    {
        return rule_lv1;
    }
    public String getRuleLv2()
    {
        return rule_lv2;
    }
    public String getRuleLv3()
    {
        return rule_lv3;
    }

    public String getRuleContent()
    {
        return rule_content;
    }
    public String getRuleStatus()
    {
        return rule_status;
    }

}
