package com.ocse.hse.Models;

import java.util.ArrayList;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckRuleSection {

    private String organID="";
    private String ruleLv1="";
    private String ruleLv2="";
    private String ruleLv3="";
    private ArrayList<CheckRulesInfo> ruleList;
    public CheckRuleSection(String taskID,String organID,String ruleLv1,String ruleLv2,String ruleLv3)
    {
        this.organID=organID;
        this.ruleLv1=ruleLv1;
        this.ruleLv2=ruleLv2;
        this.ruleLv3=ruleLv3;
        this.ruleList=CheckRulesInfo.getLv3List( taskID, organID, ruleLv1, ruleLv2, ruleLv3);
    }
    public String getSectionName()
    {
        return this.ruleLv3;
    }
    public ArrayList<CheckRulesInfo> getSectionRuleList()
    {
        return this.ruleList;
    }
}
