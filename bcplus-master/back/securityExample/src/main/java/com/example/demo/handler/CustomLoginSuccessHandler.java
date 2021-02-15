package com.example.demo.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Member;
import com.example.demo.dto.UserDetailsVO;
import com.example.demo.dto.UserVO;
import com.example.demo.service.MemberService;
import com.example.demo.utils.TokenUtils;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	   @Autowired
	    private TokenUtils tokenUtils;
        
		@Resource(name = "memberService")
		private MemberService memberService;
	   
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                        Authentication authentication) throws IOException {
	        UserVO userVO = ((UserDetailsVO)authentication.getPrincipal()).getUserVO();
	        String url=request.getRequestURI().toString();
	        url=url.replaceAll("http://","https://");
	        System.out.println(url);
	        Member member=memberService.findByUserId(userVO.getUserEmail());
			String token = tokenUtils.generateJwtToken(member);
		    response.addHeader("Authorization", "Bearer " + token);		    
		    response.setCharacterEncoding("utf-8");
	        response.setContentType("application/json");
	       
	   
	        ArrayList<Member> list = new ArrayList<Member>();
	        list.add(member);	       
	        Gson gson = new Gson();	
	        String jsonList = gson.toJson(list);       
	        PrintWriter out = response.getWriter();	    
	        out.write(jsonList);
	        out.flush();
	        out.close();
	    }

}
