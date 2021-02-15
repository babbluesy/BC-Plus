package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Lunar;
import com.example.demo.repository.LunarRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("lunarService")
public class LunarServiceImpl implements LunarService{

	@Autowired
	private LunarRepository lunarRepository;
	
	@Override
	public void createLunar(Lunar lunar) {
		lunarRepository.save(lunar);
	}

	@Override
	public Lunar findByDate(int date) {
		return lunarRepository.findByDate(date);
	}

}
