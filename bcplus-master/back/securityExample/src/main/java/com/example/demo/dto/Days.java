package com.example.demo.dto;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection="holiday")
@NoArgsConstructor
public class Days {
       private Object day;
}
