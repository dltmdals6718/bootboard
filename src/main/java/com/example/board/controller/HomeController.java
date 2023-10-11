package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.domain.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.IOException;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        // 세션 키 하나로 메시지, 멤버 다 출력 가능함!
        // Object member = request.getSession(true).getAttribute(SessionConst.LOGIN_MEMBER);
        // System.out.println("Member = " + member);
        //Object attribute = request.getSession(true).getAttribute("msg");
        return "home";
    }

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류.!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
