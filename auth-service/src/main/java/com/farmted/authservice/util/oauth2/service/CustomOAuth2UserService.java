package com.farmted.authservice.util.oauth2.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.SocialType;
import com.farmted.authservice.util.oauth2.CustomOAuth2User;
import com.farmted.authservice.util.oauth2.OAuthAttributes;
import com.farmted.authservice.repository.PassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final PassRepository passRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService - loadUser() = OAuth2 로그인 요청 진입");

        // OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고있는 유저
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // userRequest에서 registrationId 추출 후 registrationId로 socialType 저장
        // userNameAttributeName은 이후에 nameAttributeKey로 설정
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();     // OAuth2 로그인 성공 시 PK 가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes();    // userInfo의 Json값(유저 정보)

        // socialType에 따라 유저 정보를 통해 OAuthAttribues 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Pass createdPass = getPass(extractAttributes, socialType);   // getAuth() 메서드로 Pass 객체 생성 후 반환

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdPass.getRole().getKey())),
                        attributes,
                        extractAttributes.getNameAttributeKey(),
                        createdPass.getEmail(),
                        createdPass.getRole()
                );
    }

    // socialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메서드
    // 만약 찾은 회원이 있다면 그대로 반환, 없다면 saveUser()를 호출하여 회원 저장
    private Pass getPass(OAuthAttributes attributes, SocialType socialType) {
        Pass findPass = passRepository.findBySocialTypeAndSocialId(socialType, attributes.getOAuth2UserInfo().getId()).orElse(null);

        if(findPass == null) return savePass(attributes, socialType);

        return findPass;
    }

    // OAuthAttributes의 toEntity() 메서드를 통해 builder로 Auth 객체 생성 후 반환
    private Pass savePass(OAuthAttributes attributes, SocialType socialType) {
        Pass createdPass = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        return passRepository.save(createdPass);
    }

    private SocialType getSocialType(String registrationId) {
//        if(NAVER.equals(registrationId)) return SocialType.NAVER;
//        if(KAKAO.equals(registrationId)) return SocialType.KAKAO;
        return SocialType.GOOGLE;
    }
}
