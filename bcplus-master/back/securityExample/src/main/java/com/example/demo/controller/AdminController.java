package com.example.demo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserVO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource(name = "userService")
    private UserService userService;
    
	@GetMapping("/getAll")
	private List<UserVO> getAll(){
		return userService.findAll();
	}
}
