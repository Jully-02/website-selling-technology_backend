package vn.jully.website_selling_technology_backend.components.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.jully.website_selling_technology_backend.controllers.ProductController;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RequiredArgsConstructor
public class PerformanceAspect {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private String getMethodName (JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

//    @Pointcut("within(vn.jully.website_selling_technology_backend.controllers.*)")
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController.*)")
    public void controllerMethods() {};

    @Before("controllerMethods()")
    public void beforeMethodExecution (JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        logger.info("Starting execution of " + methodName);
    }

    @After("controllerMethods()")
    public void afterMethodExecution (JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        logger.info("Finished execution of " + methodName);
    }

    @Around("controllerMethods()")
    public Object measureControllerMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        // proceedingJoinPoint.getArgs()
        Object returnValue = proceedingJoinPoint.proceed();
        long end = System.nanoTime();
        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info("Execution of " + methodName + " took " +
                TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return returnValue;
    }
}
