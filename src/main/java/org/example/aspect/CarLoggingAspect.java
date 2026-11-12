package org.example.aspect;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CarLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(CarLoggingAspect.class);

    @Pointcut("@annotation(org.example.aspect.AuditAction)")
    public void auditedAction() {}

    @Before("auditedAction() && @annotation(auditAction)")
    public void beforeAction(JoinPoint joinPoint, AuditAction auditAction) {
        if (auditAction.logParameters()) {
            log.info("[@Before] Action [{}]: method='{}', arguments={}",
                    auditAction.action(),
                    joinPoint.getSignature().toShortString(),
                    Arrays.toString(joinPoint.getArgs()));
        } else {
            log.info("[@Before] Action [{}]: method='{}' (parameter logging disabled)",
                    auditAction.action(),
                    joinPoint.getSignature().toShortString());
        }
    }

    @AfterReturning(pointcut = "auditedAction() && @annotation(auditAction)", returning = "result")
    public void afterReturningAction(JoinPoint joinPoint, AuditAction auditAction, Object result) {
        log.info("[@AfterReturning] Action [{}] successfully returned: {}",
                auditAction.action(), result);
    }

    @AfterThrowing(pointcut = "auditedAction() && @annotation(auditAction)", throwing = "ex")
    public void afterThrowingAction(JoinPoint joinPoint, AuditAction auditAction, Throwable ex) {
        log.error("[@AfterThrowing] Exception during action [{}] in method [{}]: {} ({})",
                auditAction.action(),
                joinPoint.getSignature().toShortString(),
                ex.getMessage(), ex.getClass().getSimpleName());
    }

    @After("auditedAction() && @annotation(auditAction)")
    public void afterAnyAction(JoinPoint joinPoint, AuditAction auditAction) {
        log.debug("[@After] Call completion for action [{}] in method [{}]",
                auditAction.action(),
                joinPoint.getSignature().toShortString());
    }
}
