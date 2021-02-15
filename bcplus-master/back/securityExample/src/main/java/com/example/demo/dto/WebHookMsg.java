package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebHookMsg {
    private String username;
    private String icon_url;
    private String text;    
}
