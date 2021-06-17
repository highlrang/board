package com.myproject.myweb.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    NORMAL_USER("ROLE_NORMAL", "일반 사용자"),
    SILVAL_USER("ROLE_SILVAL", "중급 사용자"),
    GOLD_USER("ROLE_GOLD", "고급 사용자");

    private final String key;
    private final String title;



}
