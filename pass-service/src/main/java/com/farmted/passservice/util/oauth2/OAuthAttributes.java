package com.farmted.passservice.util.oauth2;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.SocialType;
import com.farmted.passservice.util.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.farmted.passservice.util.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;


// 각 소셜에서 받아오는 데이터가 다르므로 데이터를 분기 처리하는 DTO 클래스

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;    // OAuth2 로그인 진행 시 키가 되는 필드 값
    private final OAuth2UserInfo oAuth2UserInfo;  // 소셜 타입 별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /*
    * socialType에 맞는 메소드를 호출하여 OAuthAttributes 객체 반환
    * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키가 되는 값 / attributes : OAuth 서비스의 유저 정보들
    * of 메소드(ofGoogle, ofKakao ..)들은 각각 소셜 로그인 API에서 제공하는 회원의 식별값(id),
    * attributes, nameAttributeKey를 저장 후 build
    */
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
                                     Map<String, Object> attributes) {

//        if (socialType == SocialType.KAKAO) {
//            return ofKakao(userNameAttributeName, attributes);
//        }
//        if (socialType == SocialType.NAVER) {
//            return ofNaver(userNameAttributeName, attributes);
//        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    /*
    * of 메서드로 OAuthAttribues 객체가 생성, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
    * OAuth2UserInfo에서 socialId(식별값), email, imageUrl을 가져오고
    * JWT에 담기위한 값으로 랜덤 UUID, role은 GUEST로 설정하여 build
    * */

    public Pass toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return Pass.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .uuid(UUID.randomUUID().toString())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .role(RoleEnums.GUEST)
                .build();
    }
}
