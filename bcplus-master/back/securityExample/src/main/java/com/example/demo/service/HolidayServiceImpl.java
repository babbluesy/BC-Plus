package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.holiday;
import com.example.demo.repository.HolidayRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("holidayService")
public class HolidayServiceImpl implements HolidayService {

	@Autowired
	private HolidayRepository holydayRepository;
	
	@Override
	public void createLunar(holiday day) {
		holydayRepository.save(day);
	}

	@Override
	public holiday findByDate(int date) {
		return holydayRepository.findByDate(date);
	}

}
