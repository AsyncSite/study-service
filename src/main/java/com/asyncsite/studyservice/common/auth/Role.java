package com.asyncsite.studyservice.common.auth;

/**
 * 사용자 권한 역할을 정의하는 enum
 * JWT 토큰의 roles 클레임과 매핑됨
 */
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");
    
    private final String value;
    
    Role(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 문자열 값을 Role enum으로 변환
     */
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}