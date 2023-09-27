package com.oxahex.stockdividendapi.service;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.Dividend;
import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.model.constants.CacheKey;
import com.oxahex.stockdividendapi.persist.CompanyRepository;
import com.oxahex.stockdividendapi.persist.DividendRepository;
import com.oxahex.stockdividendapi.persist.entity.CompanyEntity;
import com.oxahex.stockdividendapi.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    /**
     * 특정 회사의 배당금 정보 반환
     * @param companyName 조회할 회사 이름
     * @return 회사 메타 데이터 + 해당 회사의 배당금 정보
     */
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 회사 명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재 하지 않는 회사명 입니다."));

        // 조회된 회사의 id로 배당금 조회
        List<DividendEntity> dividendEntityList =
                this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 회사 메타 정보와 배당금 정보를 조합해 반환
        List<Dividend> dividendList =
                dividendEntityList.stream()
                        .map(e -> new Dividend(e.getDate(), e.getDividend()))
                        .collect(Collectors.toList());

        return new ScrapedResult(
                new Company(companyEntity.getTicker(), companyEntity.getName()),
                dividendList
        );
    }
}