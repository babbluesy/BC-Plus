package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Document(collection="group")
@Data
public class Group {	
	@Id
	private ObjectId id;
	private String groupName;	
	private String groupMaster;
    private List<String> groupMember;
    private List<String> webhooks;
    private List<Message> messages;
    private String groupDetail;
    private String hashCode;
}
