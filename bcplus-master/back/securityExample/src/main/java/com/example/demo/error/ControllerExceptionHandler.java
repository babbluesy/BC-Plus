package com.example.demo.error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
        RestTemplate restTemplate = new RestTemplate();
        Gson gson=new Gson();
        String errJson=gson.toJson(message);
          
        Map<String,Object> requestMessage = new HashMap<String,Object>();
        requestMessage.put("username", "spring boot login authenticate exception");
        requestMessage.put("text", errJson);      

        HttpEntity<Map<String,Object>> entity = new HttpEntity<Map<String,Object>>(requestMessage);
       
        String url = "https://meeting.ssafy.com/hooks/ixzf976gj7rstdbdngm73zkzda"; // 사용할 슬랙의 Webhook URL 넣기

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    return message;
  }
    
}
