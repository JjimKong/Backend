package com.jjimkong_backend.global.config.oauth2.userinfo;

import com.jjimkong_backend.global.config.oauth2.userinfo.util.CastMapUtil;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private Map<String, Object> account;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        account = CastMapUtil.safeGetMap(attributes, "kakao_account");
    }

    @Override
    public String getEmail() {
        return (String) account.get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> profile = CastMapUtil.safeGetMap(account, "profile");

        return (String) profile.get("nickname");
    }


}
