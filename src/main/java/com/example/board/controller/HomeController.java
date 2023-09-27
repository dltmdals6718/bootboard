package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.domain.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            model.addAttribute("isLogin", false);
        }
        else {
            model.addAttribute("isLogin", true);
            model.addAttribute("member", loginMember);
        }

        // fragment로 하자니 생각해보니 모든 페이지마다 th:block th:if 분기로 만들기엔 코드가 복잡해짐.
        // layout 적용이 좋을듯

        return "home";
    }
}
