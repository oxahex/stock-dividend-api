package com.oxahex.stockdividendapi.controller;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.persist.entity.CompanyEntity;
import com.oxahex.stockdividendapi.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 회사 검색 자동 완성
     * @param keyword 회사 이름 키워드
     * @return 해당 키워드로 시작하는 회사명 리스트
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
//        List<String> result = this.companyService.autoComplete(keyword);
        List<String> result = this.companyService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 회사 리스트 조회
     * @param pageable 조회 조건
     * @return 조건에 해당하는 회사 목록
     */
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(Pageable pageable) {
        Page<CompanyEntity> companyEntityList = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntityList);
    }

    /**
     * 관리자 - 특정 회사 정보 저장
     * @param request 저장하려는 회사 정보(ticker)
     * @return 해당 회사의 정보
     */
    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("Ticker is Empty");
        }

        Company savedCompany = this.companyService.save(ticker);

        // 저장하는 시점에 검색 가능하도록 keyword 저장
        this.companyService.addAutoCompleteKeyword(savedCompany.getName());

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