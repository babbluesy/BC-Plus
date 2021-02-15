package com.example.demo.dto;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Document(collection="holiday")
@Data
public class holiday {
	    @Id
		private ObjectId id;
		private int date;
		private Days days;	
}
