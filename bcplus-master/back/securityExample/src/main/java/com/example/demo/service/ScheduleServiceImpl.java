package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Schedule;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ScheduleRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService{
	

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Override
	public Schedule createSchedule(Schedule schedule) {		
		return scheduleRepository.save(schedule);
	}

	@Override
	public List<Schedule> findByIdOrKey(String id, String Key) {		
		return scheduleRepository.findByIdOrKey(new ObjectId(id), Key);
	}

	@Override
	public List<Schedule> findByStartTimeBetweenAndGroup(Date from, Date to, String uid) {	
		return scheduleRepository.findByStartBetweenAndGroup(from, to, uid);
	}

	@Override
	public void deleteAllBySid(String sid) {
		
		scheduleRepository.deleteAllBySid(sid);
	}

	@Override
	public boolean findBySidExists(String sid) {		
		return scheduleRepository.existsBySid(sid);
	}

	@Override
	public List<Schedule> findByRendBetweenAndGroup(Date from, Date to, String uid) {		
		return scheduleRepository.findByRendBetweenAndGroup(from, to, uid);
	}
	@Override
	public void deleteAllByGroupName(String groupName) {
		scheduleRepository.deleteAllByGroup(groupName);

	}

	@Override
	public List<Schedule> findBySid(String sid) {		
		return scheduleRepository.findBySid(sid);
	}
	
	@Override
	public List<Schedule> findByGroup(String groupid) {
		return scheduleRepository.findByGroup(groupid);

	}

	@Override
	public List<Schedule> findByStartBetweenAndGroupAndMembers(Date from, Date to, String uid, String member) {	
		return scheduleRepository.findByStartBetweenAndGroupAndMembers(from,to,uid,member);
	}

	@Override
	public List<Schedule> findByRendBetweenAndGroupAndMembers(Date from, Date to, String uid, String member) {	
		return scheduleRepository.findByRendBetweenAndGroupAndMembers(from, to, uid, member);
	}
	
}
