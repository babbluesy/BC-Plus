package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.dto.Group;
import com.example.demo.dto.Message;
import com.example.demo.service.GroupService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@Controller
public class ChatController {
	
	
	@Resource(name = "groupService")
	private GroupService groupService;

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(Message message) {   	
    	System.out.println(message.toString());    	
    	Group group=groupService.findByGroupName(message.getGroupName());
    	List<Message>result=new ArrayList<>();
		result=group.getMessages();
		result.add(message);
		group.setMessages(result);
		groupService.createGroup(group);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getGroupName(), message);        
    }
}