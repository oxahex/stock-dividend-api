package com.oxahex.stockdividendapi.controller;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

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
     * @param request 저장하려는 회사 정보(ticker)
     * @return 해당 회사의 정보
     */
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("Ticker is Empty");
        }

        Company savedCompany = this.companyService.save(ticker);

        return ResponseEntity.ok(savedCompany);
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