package com.example.demo.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.WebHookMsg;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import net.steppschuh.markdowngenerator.table.Table;

public class WebHookUtil {
    public void sendMessage(List<String>memberList,List<String>webhooks,String title,String endTime){
        String mem="";
        for(String member:memberList){
            mem+=member+" ";

        }
        
        

        for(String url:webhooks){
            RestTemplate restTemplate = new RestTemplate();
            
            WebHookMsg msg=new WebHookMsg();

            
            String table="| Schedule Title | End Time | Member |\n | :------------ |:---------------:| -----:|\n | "+title+" | "+endTime+" | "+mem+" |";
            msg.setIcon_url("https://bcplus.me/favicon.ico");
            msg.setText( "![Mattermost alt <](https://bcplus.me/favicon.ico =50x50)\n\n#### 새로운 일정이 할당되었습니다.\n\n"+table);
            
            restTemplate.postForEntity(url, msg, String.class);
        }
    }    
}
