package com.connectravel.exception;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(String message) {
        super(message);
    }
    // 필요하다면 추가적인 생성자나 메서드를 여기에 정의할 수 있습니다.
}
