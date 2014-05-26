package com.ocse.hse.app;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mVolleyInstance=null;
    private RequestQueue mRequestQueue;
    public static VolleySingleton getInstance(){
        if(mVolleyInstance==null)
        {
            mVolleyInstance=new VolleySingleton();
            AppLog.d("mVolleyInstance=null, and returned");
        }
        else
        {
            AppLog.d("mVolleyInstance is not null, and returned");
        }
        return mVolleyInstance;
    }
    public VolleySingleton(){
        this.mRequestQueue= Volley.newRequestQueue(ApplicationController.context);
    }
    public  RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }
}