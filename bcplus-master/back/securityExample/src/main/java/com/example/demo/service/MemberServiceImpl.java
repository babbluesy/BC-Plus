package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Member;
import com.example.demo.repository.MemberRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("memberService")
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Member createMember(Member member) {		
		return memberRepository.save(member);
	}

	@Override
	public Member findByUserId(String userId) {		
		return memberRepository.findByUserId(userId);
	}

	@Override
	public void deleteMember(String member) {		
		memberRepository.deleteByUserId(member);
	}

	@Override
	public Member findByName(String name) {		
		return memberRepository.findByName(name);
	}

	

}
