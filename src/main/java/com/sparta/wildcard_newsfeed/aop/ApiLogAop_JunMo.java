package com.sparta.wildcard_newsfeed.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "ApiLogAop")
@Aspect
@Component
public class ApiLogAop_JunMo {
    //@Pointcut("execution( * *..*Service.findUserId(..) )")
    @Pointcut("within(*..*Controller)")
    public void controller() {
    }

    @Before("controller()")
    public void before(){
        try{
            log.info("test");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            log.info("Request URL : " + request.getRequestURL());
            log.info("HTTP Method : " + request.getMethod());

        } finally {

        }
    }
}
