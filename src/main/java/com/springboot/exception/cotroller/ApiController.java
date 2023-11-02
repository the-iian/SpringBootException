package com.springboot.exception.cotroller;

import com.springboot.exception.dto.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class ApiController { // 예외발생시키기

    @GetMapping // http://localhost:8080/api/user?name&age
    public User get(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age){ // 인자값 dto엔 int, controller에선 Integer
        // @RequestParam(required = false) : 인자에 값이 없더라도 실행시킬수 있도록 함

        User user = new User();
        user.setName(name);
        user.setAge(age); //

        int a = 10 + age;

        return user;

    } // 500 server error 터짐.


    @PostMapping("") // http://localhost:8080/api/user
    public User post(@Valid @RequestBody User user){

        System.out.println(user);
        return user;

    }
}
