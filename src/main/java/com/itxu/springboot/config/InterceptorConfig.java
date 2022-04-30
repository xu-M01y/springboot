package com.itxu.springboot.config;


import com.itxu.springboot.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig  implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .excludePathPatterns("/user/login", "/user/register",
                        "/**/export", "/**/import","/file/**","/echarts/**","/role/**","/menu/**",
                        //放行swagger
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**")
                .addPathPatterns("/**"); // 拦截所有请求，通过判断token是否合法来决定是否需要登录



    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
