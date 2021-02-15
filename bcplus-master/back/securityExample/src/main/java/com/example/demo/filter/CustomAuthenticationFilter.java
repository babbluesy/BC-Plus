package com.example.demo.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.UserVO;
import com.example.demo.error.InputNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	 public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
	        super.setAuthenticationManager(authenticationManager);
	    }

	    @Override
	    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
	        UsernamePasswordAuthenticationToken authRequest;
	        try{
	            UserVO userVO = new ObjectMapper().readValue(request.getInputStream(), UserVO.class);
	            authRequest = new UsernamePasswordAuthenticationToken(userVO.getUserEmail(), userVO.getUserPw());
	        } catch (IOException exception){
				
	            throw new InputNotFoundException();
	        }
	        setDetails(request, authRequest);
	        return this.getAuthenticationManager().authenticate(authRequest);
	    }
}
