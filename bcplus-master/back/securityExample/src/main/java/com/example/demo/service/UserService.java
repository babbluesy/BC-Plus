package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UserVO;

public interface UserService {

    UserVO login(UserVO userVO);

    UserVO createUser(UserVO userVO);

    UserVO findUserByUserEmail(String userEmail);
    
    void deleteUser(Long id);
    
    List<UserVO> findAll();

}
