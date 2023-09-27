package com.oxahex.stockdividendapi.controller;

import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    /**
     * 특정 회사의 배당금 정보 조회
     * @param companyName 조회할 회사 이름
     * @return 회사 메타 데이터 + 배당금 정보
     */
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        ScrapedResult result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }

}