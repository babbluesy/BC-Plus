package com.example.demo.security;


import com.example.demo.dto.GoogleVO;
import com.example.demo.error.ResourceNotFoundException;
import com.example.demo.repository.GoogleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    GoogleRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        GoogleVO user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
    	GoogleVO user = userRepository.findById(id)
    			.orElseThrow(() -> 
    				new ResourceNotFoundException("User", "id", id)
        );
 
        return UserPrincipal.create(user);
    }
}