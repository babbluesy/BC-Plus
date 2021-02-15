package com.example.demo.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.UserDetailsVO;
import com.example.demo.error.ErrorMessage;
import com.google.gson.Gson;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        try {
            String userEmail = token.getName();
        String userPw = (String) token.getCredentials();
        // UserDetailsService를 통해 DB에서 아이디로 사용자 조회
        UserDetailsVO userDetailsVO = (UserDetailsVO) userDetailsService.loadUserByUsername(userEmail);

        if (!passwordEncoder.matches(userPw, userDetailsVO.getPassword())) {
            throw new BadCredentialsException(userDetailsVO.getUsername() + "Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetailsVO, userPw, userDetailsVO.getAuthorities());
        } catch (Exception e) {
                ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                e.getMessage(),
                "CustomAuthenticationProvider.java 에서 오류 발생");
                Gson gson=new Gson();
                String errJson=gson.toJson(message);
                    RestTemplate restTemplate = new RestTemplate();

				Map<String,Object> requestMessage = new HashMap<String,Object>();
				requestMessage.put("username", "spring boot login authenticate exception");
				requestMessage.put("text", errJson);
		
				HttpEntity<Map<String,Object>> entity = new HttpEntity<Map<String,Object>>(requestMessage);				        
				String url = "https://meeting.ssafy.com/hooks/ixzf976gj7rstdbdngm73zkzda"; // 사용할 슬랙의 Webhook URL 넣기
		
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                throw e;
        } 

       
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
