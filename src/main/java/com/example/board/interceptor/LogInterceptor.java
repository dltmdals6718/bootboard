package com.example.board.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // 인터셉터는 호출 시점이 분리되어 있어 preHandler에서 만든 값을 postHandle, afterCompletion에서
        // 사용하려면 request에 담아두어야 한다.
        // LogInterceptor는 싱글톤처럼 사용되기에 멤머변수로 값을 저장하면 위험하다.
        request.setAttribute(LOG_ID, uuid);

        // 핸들러 매핑에 따라 핸들러 정보가 달라진
        // @Controller, @RequestMapping은 HandlerMethod가 넘어옴
        // /resources/static 같은 정적 리소스라면 ResourceHttpRequestHandler가 넘어옴
        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // true라면 정상 호출로 다음 인터셉터나 컨트롤러가 호출된다.

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}]", uuid, requestURI);
        if(ex!=null) {
            log.error("afterCompletion error!", ex);
        }

    }
}
