package com.itxu.springboot;

import com.itxu.springboot.pojo.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


@SpringBootApplication
@MapperScan("com.itxu.springboot.mapper")
public class SpringbootApplication {

    public static void main(String[] args) { SpringApplication.run(SpringbootApplication.class, args); }





}
