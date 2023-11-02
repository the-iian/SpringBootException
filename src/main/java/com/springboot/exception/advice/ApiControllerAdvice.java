package com.springboot.exception.advice;

import com.springboot.exception.cotroller.ApiController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackageClasses = ApiController.class) // ApiController에서만 동작하는 Advice
// @RestControllerAdvice(basePackages = "com.springboot.exception.controller")
// (basePackages = "com.springboot.exception.controller") : 이 하위에있는 예외를 모두 처리할거라는 지정
public class ApiControllerAdvice {


    /* 예외 잡을 메소드 생성
    @ExceptionHandler(value = Exception.class) // 전체적인 예외 모두 잡기. ExceptionHandler(value =) 어떤 값을 잡을건지
    public ResponseEntity exception(Exception e){ // 설정한 예외값을 매개변수로 받기

        System.out.println(e.getClass().getName()); // 어느 클래스에서 예외발생하는지 조회

        System.out.println("---------------------");
        System.out.println(e.getLocalizedMessage());
        System.out.println("---------------------");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(""); // INTERNAL_SERVER_ERROR : 서버측 오류

    } */


    // 클라이언트가 잘못한 예외상황 처리

    // 전체 예외 모두 잡기
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e){
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }


    // 제약조건과 맞지않는 데이터 길이가 입력됐을 때 예외처리
    // 어떤 필드가 잘못됐는지 정보를 담고있다
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e){

        e.getConstraintViolations().forEach(error ->{

            System.out.println(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


    // value가 null일 때
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e){

        String fieldName = e.getParameterName();
        String fieldType = e.getParameterType();
        String invalidValue = e.getMessage();

        System.out.println(fieldName);
        System.out.println(fieldType);
        System.out.println(invalidValue);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());


    }


    // 특정 메소드 예외처리
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();

        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            String fieldName = field.getField();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString(); // 어떤값이 잘못 들어갔는지 출력

            System.out.println("---------------------");
            System.out.println(fieldName);
            System.out.println(message);
            System.out.println(value);

        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 메시지 바로 담기
    }

}
