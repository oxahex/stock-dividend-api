package com.oxahex.stockdividendapi.service;

import com.oxahex.stockdividendapi.model.Auth;
import com.oxahex.stockdividendapi.persist.MemberRepository;
import com.oxahex.stockdividendapi.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 유저이름으로 DB에서 유저 정보를 찾고, 존재하는 경우 반환
     * @param username the username identifying the user whose data is required.
     * @return 해당 유저 정보
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    /**
     * 회원 가입
     * @param member 입력 받은 정보
     * @return DB에 저장한 유저 정보
     */
    public MemberEntity register(Auth.SignUp member) {
        // 해당 멤버 정보와 동일한 아이디가 있는지 확인
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());

        if (exists) {
            throw new RuntimeException("이미 사용 중인 아이디 입니다.");
        }

        // 중복된 아이디가 없는 경우 회원 가입 처리
        // 비밀번호는 암호화 처리 후 DB 저장 -> 반환
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    // 로그인 시 인증 구현
    public MemberEntity authenticate(Auth.SignIn member) {
        // 아이디로 유저를 가져옴(유저 이름)
        MemberEntity user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        // 유저의 pw는 인코딩 된 pw 이므로 디코딩 필요.
        if (this.passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}