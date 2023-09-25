package com.oxahex.stockdividendapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    /**
     * 회사 검색 자동 완성
     * @param keyword
     * @return
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
        return null;
    }

    /**
     * 회사 리스트 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    /**
     * 관리자 - 특정 회사 정보 저장
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    /**
     * 관리자 - 특정 회사 정보 삭제
     * @return
     */
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}