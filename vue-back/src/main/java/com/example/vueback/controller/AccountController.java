package com.example.vueback.controller;

import com.example.vueback.entity.Member;
import com.example.vueback.repository.MemberRepository;
import com.example.vueback.service.JwtService;
import com.example.vueback.service.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
public class AccountController {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Autowired
    public AccountController(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    /**
     * 로그인
     *
     * @param params
     * @param res
     * @return
     */
    @PostMapping("/api/account/login")
    public ResponseEntity login(
            @RequestBody Map<String, String> params,
            HttpServletResponse res
    ) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if(member != null){

            int id = member.getId();
            String token = jwtService.getToken("id",id);
            System.err.println("token : "+token);

            Cookie cookie = new Cookie("token",token);
            cookie.setHttpOnly(true); // 스크립트로 접근 불가
            cookie.setPath("/");

            res.addCookie(cookie);

            return new ResponseEntity<>(id,HttpStatus.OK);

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 로그아웃
     *
     * @param res
     * @return
     */
    @PostMapping("/api/account/logout")
    public ResponseEntity logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token){
        Claims claims = jwtService.getClaims(token);

        if(claims != null){
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.OK);

    }

}
