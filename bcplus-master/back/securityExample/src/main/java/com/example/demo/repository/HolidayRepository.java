package com.example.demo.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.holiday;

@Repository
public interface HolidayRepository extends MongoRepository<holiday, ObjectId>{
		holiday findByDate(int date);
}
