package com.example.demo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.dto.Member;


@Repository
public interface MemberRepository extends MongoRepository<Member,ObjectId>{
	public Member findByUserId(String userId);
	public Member findByName(String name);
	@DeleteQuery
	public void deleteByUserId(String userId);	
}
