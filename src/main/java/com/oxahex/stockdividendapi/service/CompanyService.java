package com.oxahex.stockdividendapi.service;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.persist.CompanyRepository;
import com.oxahex.stockdividendapi.persist.DividendRepository;
import com.oxahex.stockdividendapi.persist.entity.CompanyEntity;
import com.oxahex.stockdividendapi.persist.entity.DividendEntity;
import com.oxahex.stockdividendapi.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    // Bean 등록 후 주입 받아 사용
    private final Scraper yahooFinanceScrapper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    /**
     * 특정 회사의 메타 정보와, 배당금 정보를 DB에 저장
     * @param ticker 저장하려는 회사의 ticker 명
     * @return 저장된 회사의 정보 인스턴스
     */
    public Company save(String ticker) {
        // DB에 저장되어 있지 않은 회사일 때만 DB에 정보를 저장하도록 구현
        boolean existsByTicker = this.companyRepository.existsByTicker(ticker);
        if (existsByTicker) {
            throw new RuntimeException("Already Exists Ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    /**
     * 저장된 회사 목록 조회
     * @param pageable 조회 조건
     * @return 조건에 해당 하는 회사 목록
     */
    public Page<CompanyEntity> getAllCompany(final Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    /**
     * 특정 회사의 전체 배당금 정보를 DB에 저장
     * @param ticker 저장할 히사의 ticker 명
     * @return 저장한 회사의 Company 인스턴스
     */
    private Company storeCompanyAndDividend(String ticker) {
        // ticker 정보로 회사의 정보를 스크래핑
        Company company = this.yahooFinanceScrapper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("Fail to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재하는 경우 해당 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScrapper.scrap(company);

        // 스크래핑 결과를 저장하고 회사 인스턴스를 저장
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntityList = scrapedResult.getDividendList().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntityList);

        return company;
    }
}