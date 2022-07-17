package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author 伏辰
 * @date 2022/7/15
 */
//@Component
//@Aspect
public class AlphaAspect {
	
	@Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
	public void pointCut(){
	
	}
	
	@Before(" pointCut()")
	public void doBefore() {
		System.out.println("Before");
	}
	
	@After(" pointCut()")
	public void doAfter() {
		System.out.println("After");
	}
	
	@AfterReturning(" pointCut()")
	public void doAfterReturning() {
		System.out.println("AfterReturning");
	}
	
	@AfterThrowing(" pointCut()")
	public void doAfterThrowing() {
		System.out.println("AfterThrowing");
	}
	
	@Around(" pointCut()")
	public Object doAround( ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("Around before");
		Object obj = joinPoint.proceed();
		System.out.println("Around after");
		return obj;
	}
	
}
