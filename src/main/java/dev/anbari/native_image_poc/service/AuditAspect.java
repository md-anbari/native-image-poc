package dev.anbari.native_image_poc.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuditAspect {

    @Pointcut("execution(* dev.anbari.native_image_poc.service.EmployeeService.save(..))")
    public void saveEmployee() {}

    @Before("saveEmployee()")
    public void captureCreator(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";

        log.info(">>> [AUDIT] User '{}' is creating an employee, payload = {}", username, joinPoint.getArgs()[0]);

    }
}
