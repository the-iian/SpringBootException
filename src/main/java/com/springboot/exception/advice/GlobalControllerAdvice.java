package com.springboot.exception.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
// @RestControllerAdvice(basePackages = "com.springboot.exception.controller")
// (basePackages = "com.springboot.exception.controller") : 이 하위에있는 예외를 모두 처리할거라는 지정
public class GlobalControllerAdvice {


    // 예외 잡을 메소드 생성
    @ExceptionHandler(value = Exception.class) // 전체적인 예외 모두 잡기. ExceptionHandler(value =) 어떤 값을 잡을건지
    public ResponseEntity exception(Exception e){ // 설정한 예외값을 매개변수로 받기

        System.out.println(e.getClass().getName()); // 어느 클래스에서 예외발생하는지 조회

        System.out.println("---------------------");
        System.out.println(e.getLocalizedMessage());
        System.out.println("---------------------");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(""); // INTERNAL_SERVER_ERROR : 서버측 오류

    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class) // 특정메소드의 예외처리
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 메시지 바로 담기
    }

}
