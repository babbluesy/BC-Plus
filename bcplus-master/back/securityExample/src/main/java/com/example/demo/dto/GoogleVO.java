package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@NoArgsConstructor
@Entity
@Table(name = "googles")
@Getter
public class GoogleVO extends CommonVO implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Setter
	@Column(nullable = false)
	private String name;
	
	@Setter
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	
	@Setter
    private String imageUrl;
	
	@Setter
    @Column(nullable = false)
    private Boolean emailVerified = false;

	@Setter
    @JsonIgnore
    private String password;
	
	@NotNull
	@Setter
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
	
	@Setter
	private String providerId;	
}
