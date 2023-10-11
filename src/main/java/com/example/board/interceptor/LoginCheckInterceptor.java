package com.example.board.interceptor;

import com.example.board.domain.SessionConst;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.io.PrintWriter;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);
        if(session==null||session.getAttribute(SessionConst.LOGIN_MEMBER)==null) {
            log.info("미인증 사용자 요청");

            //방법1) 메시지창 띄우고 redirect
            response.setContentType("text/html; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('로그인이 필요합니다.'); location.href='/?redirectURL=" + requestURI +"'</script>");

            /*
            방법2) 세션으로 메시지 관리
            request.getSession(true).setAttribute("msg", "메시지");
            response.sendRedirect("/?redirectURL="+requestURI);
            */

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[loginCheckInterceptor] postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("[loginCheckInterceptor] afterCompletion");
    }
}
