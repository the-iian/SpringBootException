package com.springboot.exception.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {


    // 예외 잡을 메소드 생성
    @ExceptionHandler(value = Exception.class) // 전체적인 예외 모두 잡기. ExceptionHandler(value =) 어떤 값을 잡을건지
    public ResponseEntity exception(Exception e){ // Exception e를 통해 설정한 예외값을 매개변수로 받을수있다


        System.out.println("---------------------");
        System.out.println(e.getLocalizedMessage());
        System.out.println("---------------------");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(""); // INTERNAL_SERVER_ERROR : 서버측 오류

    }


}
