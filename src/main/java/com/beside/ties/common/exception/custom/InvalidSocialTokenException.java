package com.beside.ties.common.exception.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class InvalidSocialTokenException extends RuntimeException{
    public InvalidSocialTokenException() {
        super("토큰값이 유효하지 않습니다.");
    }
}
