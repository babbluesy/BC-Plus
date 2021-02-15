package com.example.demo.service;

import com.example.demo.dto.Lunar;

public interface LunarService {
	void createLunar(Lunar lunar);
	Lunar findByDate(int date);

}
