package com.demo.jwt_auth.controller;

import com.demo.jwt_auth.pojo.Students;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private final List<Students> students=new ArrayList<>(
            List.of(
                    new Students(1,"nisha",4),
                    new Students(2,"Rohit",8)
            )
    );
    @GetMapping("/students")
    public List<Students> getAllStudents(){
        return students;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/students")
    public Students postStudent(@RequestBody Students student){
        students.add(student);
        return student;
    }
}
