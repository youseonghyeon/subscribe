package com.example.subscribify.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 세션 컨텍스트에서 현재 로그인된 사용자를 바인딩하기 위한 어노테이션입니다.
 *
 * 컨트롤러의 메서드 파라미터로 User 객체를 직접 주입하는데 사용됩니다.
 * 이 어노테이션을 사용하는 모든 메서드 파라미터는 현재 세션에서 User 객체를 가져와 인자를 해결하게 됩니다.
 *
 * 세션에 User 정보가 없다면 알아서 null을 전달하게 됩니다.
 * 이 어노테이션을 사용하는 파라미터는 User 타입이어야 하며 null 가능성을 처리해야 합니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SessionUser {
}
