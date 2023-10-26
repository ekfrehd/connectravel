package com.connectravel.constant;

public enum Role {

    USER("ROLE_USER"), // 일반 유저
    ADMIN("ROLE_ADMIN"), // 관리자
    SELLER("ROLE_SELLER"); // 판매자

    private final String role;
    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
