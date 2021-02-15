package com.example.demo.service;

import com.example.demo.dto.holiday;

public interface HolidayService {

	void createLunar(holiday lunar);
	holiday findByDate(int date);
}
