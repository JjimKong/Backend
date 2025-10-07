package com.jjimkong_backend.global.config.oauth2;

import com.jjimkong_backend.domain.users.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private User User;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            User User) {
        super(authorities, attributes, nameAttributeKey);
        this.User = User;
    }
}
