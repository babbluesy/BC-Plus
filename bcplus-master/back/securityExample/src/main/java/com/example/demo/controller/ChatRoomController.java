package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Group;
import com.example.demo.dto.Message;
import com.example.demo.service.GroupService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
public class ChatRoomController {
	
		@Resource(name = "groupService")
		private GroupService groupService;
	 				
		@GetMapping("/getMsg/{hashCode}")		
		private List<Message> getMsg(@PathVariable(value="hashCode") String hashCode) throws Exception {	
			Group group= groupService.findByHashCode(hashCode);
			List<Message>result=new ArrayList<>();
			result=group.getMessages();
			return result;
		}
}