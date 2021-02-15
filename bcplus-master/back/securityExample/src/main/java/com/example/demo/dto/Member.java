package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection="member")
@Data
public class Member {
	@Id
	private ObjectId id;
	private String name;
	private String userId;	
	private String pw;		
    private Date birthDay;  
    private List<String> groups;
    private List<Memo> memos;

}
