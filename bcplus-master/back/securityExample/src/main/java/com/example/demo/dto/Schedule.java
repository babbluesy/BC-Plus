																																																																																																							package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Document(collection="schedule")
@Data
public class Schedule {
	@Id
	private ObjectId id;
	private String sid;
	private Date start;
	private Date end;	
	private String color;
	private String group;
	private List<String> members;
	private boolean allDay;
	private boolean editable;
	private String status;
	@Indexed
	private Date rstart;
	private Date rend;
	private String key;
	private String title;
	private String description;
}
