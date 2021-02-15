package com.example.demo.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GoogleToken;
import com.example.demo.dto.Member;
import com.example.demo.dto.UserRole;
import com.example.demo.dto.UserVO;
import com.example.demo.utils.GoogleUtils;
import com.example.demo.utils.TokenUtils;

import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Log4j2
public class TokenController {
	
	@Autowired
	private GoogleUtils googleUtils;
	
	@Autowired
	private TokenUtils tokenUtils;
	@GetMapping("/aaa")
	private String testNginx() {
		return "hello nginx";
	}
	@GetMapping("/token")	
	private Member getGoogleAcountByToken(@RequestParam String code,@RequestParam String scope,@RequestParam String prompt,HttpServletResponse response) throws IOException, GeneralSecurityException {
		System.out.println("구글 api 인증요청 받음\n\n\n\n\n\n");
		System.out.println(code);
		System.out.println("구글 api 인증요청 받음\n\n\n\n\n\n");
		System.out.println("*************************************");
		System.out.println("*************************************");
		System.out.println("*************************************");
		System.out.println("*************************************");
		GoogleToken googleToken=new GoogleToken();
		googleToken.setCode(code);
		googleToken.setRedirect_uri("http://localhost:8080/api/token");
		Member member=googleUtils.checkMember(googleToken);		
		String token = tokenUtils.generateJwtToken(member);
	    response.addHeader("Authorization", "Bearer " + token);
		return member;
	}
	@PostMapping("/token")	
	private Member getGoogleAcountByToken(@RequestBody GoogleToken googleToken,HttpServletResponse response) throws IOException, GeneralSecurityException {
		System.out.println("구글 api 인증요청 받음");
		System.out.println(googleToken.toString());
		Member member=googleUtils.checkMember(googleToken);
		String token = tokenUtils.generateJwtToken(member);
	    response.addHeader("Authorization", "Bearer " + token);
		return member;
	}

}
