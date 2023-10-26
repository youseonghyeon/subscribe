package com.example.subscribify.entity;

import lombok.Getter;

@Getter
public enum DuplicatePaymentOption {

    DISALLOW_DUPLICATION("disallowDuplication", "중복 결제 불가능"),
    ALLOW_DUPLICATION("allowDuplication", "중복 결제 허용");
//    EXTEND_ON_DUPLICATION("extendOnDuplication", "중복 결제 시 기간 연장");

    private final String code;
    private final String description;

    DuplicatePaymentOption(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 옵션 코드로부터 해당 enum을 찾는 유틸리티 메서드
    public static DuplicatePaymentOption fromCode(String code) {
        for (DuplicatePaymentOption option : values()) {
            if (option.getCode().equals(code)) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
