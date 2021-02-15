package com.example.demo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Document(collection="lunar")
@Data
public class Day {
	@Id
	private String lunWolgeon;
	private int lunYear;
	private int solJd;
	private String lunIljin;
	private String lunMonth;
	private String solDay;
	private String solMonth;
	private int lunNday;
	private String lunSecha;
	private String lunLeapmonth;
	private String solWeek;
	private String lunDay;
	private String solLeapyear;
	private int solYear;
}
