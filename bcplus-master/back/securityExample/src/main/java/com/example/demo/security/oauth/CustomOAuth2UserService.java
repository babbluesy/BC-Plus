package com.example.demo.security.oauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.dto.AuthProvider;
import com.example.demo.dto.GoogleVO;
import com.example.demo.dto.Group;
import com.example.demo.dto.Member;
import com.example.demo.error.OAuth2AuthenticationProcessingException;
import com.example.demo.repository.GoogleRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.UserPrincipal;
import com.example.demo.security.oauth.user.OAuth2UserInfo;
import com.example.demo.security.oauth.user.OAuth2UserInfoFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private GoogleRepository googleRepository;
    
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    	System.out.println(oAuth2UserRequest.toString());
    	System.out.println(oAuth2User.toString());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<GoogleVO> userOptional = googleRepository.findByEmail(oAuth2UserInfo.getEmail());
        GoogleVO user;
      
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private GoogleVO registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    	GoogleVO user = new GoogleVO();
    	Member member=new Member();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        member.setUserId(oAuth2UserInfo.getEmail());
        member.setName(oAuth2UserInfo.getName());
        List<String> groups=new ArrayList();
        member.setGroups(groups);
        member.setBirthDay(new Date());
        member.setPw("");
        memberRepository.save(member);
        return googleRepository.save(user);
    }

    private GoogleVO updateExistingUser(GoogleVO existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return googleRepository.save(existingUser);
    }

}