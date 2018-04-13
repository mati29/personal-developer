package com.mateuszjanwojtyna.personaldeveloper.Aspects;

import com.mateuszjanwojtyna.personaldeveloper.Services.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    AuditService auditService;

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Controllers.*.*(..))")
    public void controllers(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Services.*.*(..))")
    public void services(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Repositories.*.*(..))")
    public void repositories(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Repositories.AuditRepository.*(..))")
    public void auditRepo(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Services.AuditService.*(..))")
    public void auditServices(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Services.impl.AuditServiceImpl.*(..))")
    public void auditServicesImpl(){};

    @Pointcut("execution(* com.mateuszjanwojtyna.personaldeveloper.Controllers.AuditController.*(..))")
    public void auditController(){};

    @Pointcut("controllers() || services() || repositories()")
    public void bussinessPointcut(){};

    @Pointcut("auditRepo() || auditServices() || auditServicesImpl() || auditController()")
    public void auditPointcut(){};

    @Pointcut("bussinessPointcut() && !auditPointcut()")
    public void loggerPointcut(){};

    @Before("loggerPointcut()")
    public void beforeBussiness(JoinPoint joinPoint) {
        auditService.create(joinPoint);
    }

}
