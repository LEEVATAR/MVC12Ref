package com.model2.mvc.common.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.model2.mvc.service.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/*
 * FileName : LogonCheckInterceptor.java
 *  �� Controller ȣ���� interceptor �� ���� ��ó��/��ó��/�Ϸ�ó���� ����
 *  	- preHandle() : Controller ȣ���� ��ó��   
 * 			(true return ==> Controller ȣ�� / false return ==> Controller ��ȣ�� ) 
 *  	- postHandle() : Controller ȣ�� �� ��ó��
 *    	- afterCompletion() : view ������ ó��
 *    
 *    ==> �α����� ȸ���̸� Controller ȣ�� : true return
 *    ==> �� �α����� ȸ���̸� Controller �� ȣ�� : false return
 */
@Component
public class LogonCheckInterceptor implements HandlerInterceptor {
	
	///Field	
	///Constructor ��������Ʈ���� �ʿ䰡 �����
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
    	System.out.println("\n[ LogonCheckInterceptor start........]");
		
		//==> �α��� ����Ȯ��
    	HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
          //==> �α����� ȸ���̶��...
        if (user != null) {
            String uri = request.getRequestURI();
          //==> �α��� ���¿��� ���� �Ұ� URI
            if (uri.contains("addUser") || uri.contains("login") || uri.contains("checkDuplication")) {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                System.out.println("[ �α��� ����.. �α��� �� ���ʿ���.... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
                return false;
            }
            System.out.println("[ �α��� ���� ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
        } else {
        	//==> �� �α����� ȭ���̶��...
			//==> �α��� �õ� ��.....
            String uri = request.getRequestURI();

            if (uri.contains("addUser") || uri.contains("login") || uri.contains("checkDuplication")) {
            	System.out.println("[ �α� �õ� ���� .... ]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
                return true;
            }

            request.getRequestDispatcher("/index.jsp").forward(request, response);
            System.out.println("[ �α��� ���� ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
            return false;
        }
    }
}//EOC
