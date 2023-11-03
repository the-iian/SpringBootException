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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.springboot.exception.dto.Error;
import com.springboot.exception.dto.ErrorResponse;

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


    // value가 null일 때
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        String fieldName = e.getParameterName();
        String invalidValue = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setField(fieldName);
        errorMessage.setMessage(e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI()); // 요청한 url에서 에러발생함을 넣기
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }


    // 특정 메소드 예외처리
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            String fieldName = field.getField();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString(); // 어떤값이 잘못 들어갔는지 출력

            Error errorMessage = new Error();
            errorMessage.setField(fieldName);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(value);

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI()); // 요청한 url에서 에러발생함을 넣기
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse); // 메시지 바로 담기
    }


    // 제약조건과 맞지않는 데이터 길이가 입력됐을 때 예외처리
    // 어떤 필드가 잘못됐는지 정보를 담고있다
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest){

        List<Error> errorList = new ArrayList<>();

        e.getConstraintViolations().forEach(error ->{

            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size() -1).getName();
            String message = error.getMessage();
            String invalidValue = error.getInvalidValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(field);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(invalidValue);

            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setRequestUrl(httpServletRequest.getRequestURI()); // 요청한 url에서 에러발생함을 넣기
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResultCode("FAIL");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
