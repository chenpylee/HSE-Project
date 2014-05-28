package com.ocse.hse.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by leehaining on 14-5-22.
 */
public class TaskInfo implements Serializable {
    private String taskID;
    private String taskName;//检查任务名称
    private Date taskStartDate;//检查开始时间
    private Date taskEndDate;//检查结束时间
    private String taskDescription;//检查任务描述
    private ArrayList<OrganInfo> taskOrgansList;//受检单位列表
    public TaskInfo(String taskID,String taskName,Date taskStartDate,Date taskEndDate,String taskDescription,ArrayList<OrganInfo>taskOrgans){
        this.taskID=taskID;
        this.taskName=taskName;
        this.taskStartDate=taskStartDate;
        this.taskEndDate=taskEndDate;
        this.taskDescription=taskDescription;
        taskOrgansList=new ArrayList<OrganInfo>();
        Iterator<OrganInfo> it = taskOrgans.iterator();
        while(it.hasNext())
        {
            OrganInfo item = it.next();
            this.taskOrgansList.add(item);
        }
    }
    public String getTaskID(){
        return this.taskID;
    }
    public String getTaskName(){
        return this.taskName;
    }
    public String getTaskStartDateString(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String strDate=dateFormat.format(this.taskStartDate);
        return strDate;
    }
    public Date getTaskStartDate(){
        return this.taskStartDate;
    }
    public String getTaskEndDateString(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String strDate=dateFormat.format(this.taskEndDate);
        return strDate;
    }
    public Date getTaskEndDate(){
        return this.taskEndDate;
    }
    public String getTaskDescription(){
        return this.taskDescription;
    }
    public ArrayList<OrganInfo> getTaskOrgansList(){
        return this.taskOrgansList;
    }

}
