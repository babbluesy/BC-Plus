package com.example.demo.service;

import com.example.demo.dto.Member;


public interface MemberService {
	Member createMember(Member member);
	Member findByUserId(String userId);
	Member findByName(String name);
	void deleteMember(String member);
	
}
