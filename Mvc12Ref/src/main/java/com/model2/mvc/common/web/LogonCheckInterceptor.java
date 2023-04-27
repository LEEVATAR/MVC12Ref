package com.model2.mvc.common.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.model2.mvc.service.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/*
 * FileName : LogonCheckInterceptor.java
 *  ㅇ Controller 호출전 interceptor 를 통해 선처리/후처리/완료처리를 수행
 *  	- preHandle() : Controller 호출전 선처리   
 * 			(true return ==> Controller 호출 / false return ==> Controller 미호출 ) 
 *  	- postHandle() : Controller 호출 후 후처리
 *    	- afterCompletion() : view 생성후 처리
 *    
 *    ==> 로그인한 회원이면 Controller 호출 : true return
 *    ==> 비 로그인한 회원이면 Controller 미 호출 : false return
 */
@Component
public class LogonCheckInterceptor implements HandlerInterceptor {
	
	///Field	
	///Constructor 스프링부트에는 필요가 읎어용
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
    	System.out.println("\n[ LogonCheckInterceptor start........]");
		
		//==> 로그인 유무확인
    	HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
          //==> 로그인한 회원이라면...
        if (user != null) {
            String uri = request.getRequestURI();
          //==> 로그인 상태에서 접근 불가 URI
            if (uri.contains("addUser") || uri.contains("login") || uri.contains("checkDuplication")) {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                System.out.println("[ 로그인 상태.. 로그인 후 불필요함.... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
                return false;
            }
            System.out.println("[ 로그인 상태 ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
        } else {
        	//==> 미 로그인한 화원이라면...
			//==> 로그인 시도 중.....
            String uri = request.getRequestURI();

            if (uri.contains("addUser") || uri.contains("login") || uri.contains("checkDuplication")) {
            	System.out.println("[ 로그 시도 상태 .... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
                return true;
            }

            request.getRequestDispatcher("/index.jsp").forward(request, response);
            System.out.println("[ 로그인 이전 ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
            return false;
        }
    }
}//EOC
