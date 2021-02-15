package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Group;
import com.example.demo.dto.Member;
import com.example.demo.dto.Message;
import com.example.demo.dto.Schedule;
import com.example.demo.dto.UserRole;
import com.example.demo.dto.UserVO;
import com.example.demo.dto.WebHook;
import com.example.demo.service.GroupService;
import com.example.demo.service.MemberService;
import com.example.demo.service.ScheduleService;
import com.example.demo.utils.MakeCode;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
@Log4j2
public class GroupController {
	@Resource(name = "groupService")
	private GroupService groupService;
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	@Resource(name="scheduleService")
	private ScheduleService scheduleService;
	
	
	private MakeCode makeCode=new MakeCode();
	
	
	
	@PostMapping()
	@ApiOperation(value = "Test Group", tags = "createGroup")
	private Group insert(@RequestBody Group group) throws NoSuchAlgorithmException,Exception {
		if(!groupService.findByGroupNameExists(group.getGroupName())) {
			group.setHashCode(makeCode.sha256(group.getGroupName()));
			String userId=group.getGroupMember().get(0);
			group.setGroupMaster(userId);
			groupService.createGroup(group);
			Member member=memberService.findByUserId(userId);
			member.getGroups().add(group.getGroupName());
			
			memberService.createMember(member);
			
		}else {
			throw new Exception("already exist");
		}
		return group;
	}
	
	@GetMapping("/grant/{userid}/{groupid}")
	@ApiOperation(value = "Test Group", tags = "selectGroupById")
	private Group grantByUserid(@PathVariable(value="userid") String userid, @PathVariable(value="groupid") String groupid) throws Exception {	
		Group group= groupService.findByGroupName(groupid);
		List<String> member=group.getGroupMember();
		if(member.contains(userid)&&!group.getGroupMaster().equals(userid)) {
		group.setGroupMaster(userid);
		groupService.createGroup(group);
		return group;
		}else {
			throw new Exception();
		}
	}
	
	
	@GetMapping("/{name}")
	@ApiOperation(value = "Test Group", tags = "selectGroupById")
	private Group getByGroupName(@PathVariable(value="name") String name) {	
		Group group= groupService.findByGroupName(name);
		return group;
	}

	@GetMapping("/getMessage/{name}")
	@ApiOperation(value = "Test Group", tags = "selectGroupById")
	private List<Message> getMessage(@PathVariable(value="name") String name) {	
		Group group= groupService.findByGroupName(name);
		return group.getMessages();
	}

	@GetMapping("/grantmaster/{userid}/{groupid}")		// 권한 부여.
	private String grantByuserid(@PathVariable(value="id") String userid, @PathVariable(value="groupid") String groupid){	
		Group group= groupService.findByGroupName(groupid);
			group.setGroupMaster(userid);
			groupService.createGroup(group);
			return "success group grant";
	}
	
	@DeleteMapping("/{userid}/{groupid}")
	@ApiOperation(value = "Test Group", tags = "deleteGroupById")
	private String  getoutById(@PathVariable(value="userid") String userid, @PathVariable(value="groupid") String groupid) { // 멤버 탈퇴.
		// 멤버 탈퇴 로직
		// 1. 그룹 멤버가 남아있는지 확인 -> 없으면 폭파. 2. 그룹일정에서 본인이 들어간 거 update 3. 그룹멤버 update 4. 본인 그룹 update.
		Group group=groupService.findByGroupName(groupid);
		List<String> members=group.getGroupMember();
		if(members.size()==1) { // 나 혼자면,
			deleteById(groupid);
		}else {
			List<Schedule> list = scheduleService.findByGroup(groupid); // 찾기 create 하면 업데이트 됨.
			for(Schedule s:list) {
					Schedule newSchedule=new Schedule();
					newSchedule.setAllDay(true);
					newSchedule.setColor(s.getColor());
					newSchedule.setDescription(s.getDescription());
					newSchedule.setEnd(s.getEnd());
					newSchedule.setGroup(s.getGroup());
					newSchedule.setKey(s.getKey());;
					newSchedule.setRend(s.getRend());
					newSchedule.setRstart(s.getRstart());
					newSchedule.setSid(s.getSid());
					newSchedule.setStart(s.getStart());
					newSchedule.setTitle(s.getTitle());
					List<String> member=s.getMembers();
					member.remove(userid);
					newSchedule.setMembers(member);
					scheduleService.createSchedule(newSchedule);
			}
			members.remove(userid);
			group.setGroupMember(members);
			Member temp = memberService.findByUserId(userid);
			List<String> mygroups = temp.getGroups();
			mygroups.remove(groupid);
			temp.setGroups(mygroups);
			memberService.createMember(temp);
		}
		return "group out success";
	}

	@DeleteMapping("/{groupid}")
	@ApiOperation(value = "Test Group", tags = "deleteGroupById")
	private String deleteById(@PathVariable(value="groupid") String groupid) { // 그룹 폭파.
		// 그룹을 삭제할때 필요한 내용
		// 1. 그룹 지우기 2. 멤버에서 그룹 포함한거 다 지우기. 3. 일정에서 그룹 일정 다 지우기.
		Group group=groupService.findByGroupName(groupid);
		List<String> members=group.getGroupMember();
		for(String member:members) {
			Member newMember=memberService.findByUserId(member);
			List<String>groups=newMember.getGroups();
			groups.remove(groupid);
			memberService.createMember(newMember);
		}
		scheduleService.deleteAllByGroupName(group.getGroupName());
		groupService.deleteAllByGroupName(groupid);
		return "group delete success";
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Test Group", tags = "updateGroup")
	private String update(@RequestBody Group group) throws Exception {
		if(groupService.existsByHashCode(group.getHashCode())) {
		Group newGroup=groupService.findByGroupName(group.getGroupName());
		newGroup.setGroupDetail(group.getGroupDetail());
		groupService.createGroup(newGroup);
		}else {
			throw new Exception();
		}
		return "group update success";
	}
	
	@GetMapping("/all/{id}")
	private List<Group> findByGroupMember(@PathVariable(value="id") String id){		
		return groupService.findByGroupMember(id);		
	}

	@GetMapping("invite/{name}/{code}")
	@ApiOperation(value = "Test Group", tags = "selectGroupById")
	private Group invite(@PathVariable(value="name") String name,@PathVariable(value="code") String code) throws Exception  {	
		if(groupService.existsByHashCode(code)) {
			Group group=groupService.findByHashCode(code);
			List<String> list=group.getGroupMember();
			for(String member:list) {
				if(member.equals(name)) {
					throw new Exception();
				}
			}
			Member newMember=memberService.findByUserId(name);
			newMember.getGroups().add(group.getGroupName());
			memberService.createMember(newMember);
			list.add(name);
			group.setGroupMember(list);
			groupService.createGroup(group);
			return group;
		}else {
			throw new Exception();
		}	
	}

	@GetMapping("/leaveGroup/{groupName}/{userid}")
	private Group leaveGroup(@PathVariable(value="groupName") String groupName,@PathVariable(value="userid") String userid) {
		Group group=groupService.findByGroupName(groupName);
		group.getGroupMember().remove(userid);
		groupService.createGroup(group);
		Member member=memberService.findByUserId(userid);
		member.getGroups().remove(groupName);
		memberService.createMember(member);
		List<Schedule>list=scheduleService.findByGroup(groupName);
		for(Schedule sch:list) {
			sch.getMembers().remove(userid);
			scheduleService.createSchedule(sch);
		}		
		return group;
	}



	@GetMapping("/getMemberList/{groupName}")
	private List<String> getMemberList(@PathVariable(value="groupName") String groupName){
		Group group=groupService.findByGroupName(groupName);
		return group.getGroupMember();
	}

	//slack mattermost 같은 웹훅알람 등록 유요성 검사는 X
	@PostMapping("/addWebHook")
	private Group addWebHook(@RequestBody WebHook webHook){		
		Group group=groupService.findByGroupName(webHook.getGroupName());
		List<String> webhooks=new ArrayList<>();
		webhooks=group.getWebhooks();
		webhooks.add(webHook.getWebhook());
		group.setWebhooks(webhooks);
		groupService.createGroup(group);
		return group;
	}


}
