package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.domain.MemberType;
import com.example.board.domain.SessionConst;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final String tokenUri = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUri = "https://kapi.kakao.com/v2/user/me";

    private final MemberService memberService;

    @Value("${client_id}")
    private String client_id;

    @Value("${secret_id}")
    private String secret_id; // 보안 추가

    @GetMapping("/kakaoLogin")
    public String getToken(@RequestParam("code") String code, HttpServletRequest request) {

        // Token 발급
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", "http://localhost:8080/kakaoLogin");
        body.add("code", code);
        body.add("client_secret", secret_id);


        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(body, headers);
        String token = (String) restTemplate.exchange(tokenUri, HttpMethod.POST, httpEntity, Map.class).getBody().get("access_token");

        // 회원 정보 호출
        Member memberInfo = getInfo(token);
        Member saveMember = memberService.save(memberInfo);

        // 세션에 회원 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, saveMember);

        return "redirect:/";
    }

    public Member getInfo(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer "+token);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        String response = restTemplate.exchange(userInfoUri, HttpMethod.GET, httpEntity, String.class).getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
        JSONObject profile = kakaoAccount.getJSONObject("profile");
        String name = profile.getString("nickname");
        Long snsIdentifier = jsonObject.getLong("id");
        String email = kakaoAccount.getString("email");
        MemberType memberType = MemberType.KAKAO;

        Member member = new Member();
        member.setSnsIdentifier(snsIdentifier);
        member.setName(name);
        member.setEmail(email);
        member.setMemberType(memberType);
        return member;
    }

}
