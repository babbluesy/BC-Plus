package com.example.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MakeCode {
	public String sha256(String name) throws NoSuchAlgorithmException {
		MessageDigest md=MessageDigest.getInstance("SHA-256");
		md.update(name.getBytes());
		return bytesToHex(md.digest());
	}
	
	public  String bytesToHex(byte[] bytes) {
		StringBuilder builder=new StringBuilder();
		for(byte b:bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

}
