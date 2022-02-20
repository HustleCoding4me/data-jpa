package study.datajpa.controller;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        return "hello";
    }
}
