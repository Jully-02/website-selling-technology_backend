package vn.jully.website_selling_technology_backend.components.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.jully.website_selling_technology_backend.controllers.ProductController;

@Component
@Aspect
@RequiredArgsConstructor
public class UserActivityLogger {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    // named pointcut
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods() && execution(* vn.jully.website_selling_technology_backend.controllers.UserController.*(..))")
    public Object logUserActivity (ProceedingJoinPoint joinPoint) throws Throwable {
        // Write log before execute method
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getRemoteAddr();
        logger.info("User activity started: " + methodName + ", IP address: " + remoteAddress);
        // Execute main method
        Object result = joinPoint.proceed();

        // Write log after execute method
        logger.info("User activity finished: " + methodName);
        return result;
    }
}
