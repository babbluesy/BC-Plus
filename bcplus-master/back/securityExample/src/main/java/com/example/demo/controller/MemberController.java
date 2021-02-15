package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.AppProperties;
import com.example.demo.dto.GoogleVO;
import com.example.demo.dto.Group;
import com.example.demo.dto.Member;
import com.example.demo.dto.Memo;
import com.example.demo.dto.Schedule;
import com.example.demo.dto.UserRole;
import com.example.demo.dto.UserVO;
import com.example.demo.repository.GoogleRepository;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.GroupService;
import com.example.demo.service.MemberService;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.UserService;
import com.example.demo.utils.GoogleUtils;
import com.example.demo.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Log4j2
public class MemberController {
	
	private TokenProvider tokenProvider=new TokenProvider(new AppProperties());
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
    @Resource(name = "userService")
    private UserService userService;
    
	@Resource(name = "groupService")
	private GroupService groupService;
	
	@Resource(name="scheduleService")
	private ScheduleService scheduleService;
	    
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private TokenUtils tokenUtils;
    
    @Autowired
    private GoogleRepository googlerepository;
   
	
	@GetMapping("/{userId}")
	@ApiOperation(value = "Test Sample", tags = "selectByuserId")
	private Member getMemberByUserId(@PathVariable(value="userId") String userId) {		
		return memberService.findByUserId(userId);
	}
	
	@GetMapping("/login/{userId}")	
	private Member getMemberAndToke(@PathVariable(value="userId") String userId,HttpServletResponse response) {	   
		return null;
	}
	
	@RequestMapping("/google/{userId}")	
	private String getGoogleLogin(@PathVariable(value="userId") String userId,HttpServletRequest request,HttpServletResponse response) {	   
		Member member=memberService.findByUserId(userId);
		String token = tokenUtils.generateJwtToken(member);
	    response.addHeader("Authorization", "Bearer " + token);		
		return token;
	}
	@PostMapping()
	@ApiOperation(value = "Test Sample", tags = "create")
	private String insert(@RequestBody Member member) throws Exception {
		UserVO userVO=new UserVO();
		userVO.setUserEmail(member.getUserId());
        userVO.setUserPw(passwordEncoder.encode(member.getPw()));
        userVO.setRole(UserRole.ADMIN);
        try {
        	if(userService.createUser(userVO) == null){
        		
        	}
        }catch(Exception e){
        	throw e;
        }
		member.setPw("");
		member.setMemos(new ArrayList<Memo>());
        memberService.createMember(member);
		return "member insert success";
	}

	@DeleteMapping("/{userId}/{hash}")
	@ApiOperation(value = "Test Sample", tags = "delete")
	private String deleteByName(@PathVariable(value="userId") String userId,@PathVariable(value="hash") String hash) {
		Member member=memberService.findByUserId(userId);
		List<String>groups=member.getGroups();		
	    for(String groupName:groups) {
	    	Group group=groupService.findByGroupName(groupName);
	    	List<String> memberList=group.getGroupMember();
	    	memberList.remove(userId);
	    	group.setGroupMember(memberList);
	    	groupService.createGroup(group);
	    	List<Schedule>list=scheduleService.findByGroup(groupName);
			for(Schedule sch:list) {
				sch.getMembers().remove(userId);
				scheduleService.createSchedule(sch);
			}	
	    }
	    scheduleService.deleteAllByGroupName(hash);
		memberService.deleteMember(userId);
		UserVO userVO=userService.findUserByUserEmail(userId);
		userService.deleteUser(userVO.getId());
		 Optional<GoogleVO> userOptional = googlerepository.findByEmail(userId);
		 if(userOptional.isPresent()) {           
			   googlerepository.deleteById(userOptional.get().getId());
	     } 
		return "member delete success";
	}
	
	@PostMapping("/updateMember")
	@ApiOperation(value = "Test Sample", tags = "update")
	private String update(@RequestBody Member member) {
		Member newMember=memberService.findByUserId(member.getUserId());		
		newMember.setPw(member.getPw());
		newMember.setName(member.getName());
		newMember.setBirthDay(member.getBirthDay());
		UserVO userVO=userService.findUserByUserEmail(member.getUserId());
		if(member.getPw()!=null) {
			  userVO.setUserPw(passwordEncoder.encode(member.getPw()));
		}      
        userService.createUser(userVO);
        newMember.setPw("");
        memberService.createMember(newMember);
		return "member update success";
	}
	
	
	@PostMapping("/addMemo")
	@ApiOperation(value = "Test Sample", tags = "addMemo")
	private List<Memo> addMemo(@RequestBody Member member){		
		Member newMember=memberService.findByUserId(member.getUserId());
		System.out.println(newMember.toString());
		List<Memo>memos=new ArrayList<>();
		memos=newMember.getMemos();		
		memos.add(member.getMemos().get(0));
		newMember.setMemos(memos);
		memberService.createMember(newMember);
		return memos;
	}
	
	@GetMapping("/getMemo/{userId}")
	private List<Memo>getMemo(@PathVariable(value="userId") String userId){
		Member member=memberService.findByUserId(userId);
		System.out.println("memos"+member.getMemos().toString());
		return member.getMemos();
	}
	
	@PostMapping("/deleteMemo")
	private List<Memo>deleteMemo(@RequestBody Member member){
		System.out.println("memo test"+member.toString());
		Member newMember=memberService.findByUserId(member.getUserId());	
		newMember.setMemos(member.getMemos());
		memberService.createMember(newMember);
		return newMember.getMemos();
	}
}
