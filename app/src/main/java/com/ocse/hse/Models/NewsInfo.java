package com.ocse.hse.Models;

import android.net.Uri;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leehaining on 14-4-21.
 */
public class NewsInfo implements Serializable{
    /*
    "list": [
        {
            "id": 386,
            "title": "河南油田金保工程顺利上线",
            "Content": "<P>河南油田金保工程顺利上线，今后职工看病不用再排除报销了</P>",
            "brief": "",
            "updatetime": "2010-8-7 13:20:00",
            "sender": "zjd"
        }
     */
    private String title,content,updatetime;
    private String OC_XWXXB_ID,BT,TJSJ,TPLJ,XWLB;
    private Boolean SFTPXW;
    public NewsInfo(int OC_XWXXB_ID,String BT,String SFTPXW,String TJSJ,String TPLJ,String XWLB)
    {
        this.BT=BT;
        this.OC_XWXXB_ID=Integer.toString(OC_XWXXB_ID);
        if(SFTPXW.equals("1"))
        {
            this.SFTPXW=true;
            this.TPLJ=TPLJ;
            this.BT="[图]"+this.BT;
        }
        else
        {
            this.SFTPXW=false;
            this.TPLJ="";
        }
        this.XWLB=XWLB;
        this.TJSJ=TJSJ;
    }

    public String getTitle(){
        return this.BT;
    }
    public String getUpdatetime(){
        return TJSJ;
    }
    public String getID(){
        return this.OC_XWXXB_ID;
    }
    public Boolean isImage(){
        return this.SFTPXW;
    }
    public String getImageUrl(){
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("url","http://10.52.1.131"+this.TPLJ);

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendEncodedPath(ApplicationConstants.APP_URL_API)
                    .appendEncodedPath(ApplicationConstants.APP_URL_IMAGE_PROXY);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
            urlString=builder.build().toString();
        }catch (Exception e)
        {
            AppLog.e(e.getMessage());
        }
        return urlString;
    }
}
