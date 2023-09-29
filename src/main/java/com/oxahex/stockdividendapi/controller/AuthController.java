package com.oxahex.stockdividendapi.controller;

import com.oxahex.stockdividendapi.model.Auth;
import com.oxahex.stockdividendapi.persist.entity.MemberEntity;
import com.oxahex.stockdividendapi.security.TokenProvider;
import com.oxahex.stockdividendapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    /**
     * 회원 가입
     * @param request 회원 가입 시 필요한 정보
     * @return 가입한 회원 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        MemberEntity registeredMember = this.memberService.register(request);
        return ResponseEntity.ok(registeredMember);
    }

    /**
     * 로그인
     * @param request 로그인 정보
     * @return JWT 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {

        // 아이디와 패스워드가 일치하는지 확인
        MemberEntity user = this.memberService.authenticate(request);

        // 토큰 프로바이더로 JWT 토큰을 생성해 반환
        String token = this.tokenProvider.generateToken(user.getUsername(), user.getRoles());

        log.info("user login -> " + request.getUsername());

        return ResponseEntity.ok(token);
    }
}