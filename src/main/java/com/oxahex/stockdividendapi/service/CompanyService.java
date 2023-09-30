package com.oxahex.stockdividendapi.service;

import com.oxahex.stockdividendapi.exception.impl.AlreadyExistCompanyException;
import com.oxahex.stockdividendapi.exception.impl.NoCompanyException;
import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.persist.CompanyRepository;
import com.oxahex.stockdividendapi.persist.DividendRepository;
import com.oxahex.stockdividendapi.persist.entity.CompanyEntity;
import com.oxahex.stockdividendapi.persist.entity.DividendEntity;
import com.oxahex.stockdividendapi.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    // Bean 등록 후 주입 받아 사용
    private final Scraper yahooFinanceScrapper;     // 클래스 내부에서 빈 등록
    private final Trie<String, String> trie;        // config 패키지에서 @Configuration 등록

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
            throw new AlreadyExistCompanyException();
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

    /**
     * 회사 명 자동 완성
     * @param keyword 검색 키워드
     * @return 해당 키워드로 시작하는 회사 명 리스트 반환
     */
    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntityList =
                this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);

        return companyEntityList.stream()
                .map(CompanyEntity::getName)
                .collect(Collectors.toList());

    }

    public void addAutoCompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autoComplete(String keyword) {
        return this.trie.prefixMap(keyword).keySet()
                        .stream().limit(10)
                        .collect(Collectors.toList());
    }

    public void deleteAutoCompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    /**
     * 특정 회사의 데이터를 DB에서 삭제
     * @param ticker 삭제할 회사의 ticker
     * @return 삭제한 회사 이름
     */
    public String deleteCompany(String ticker) {

        // 삭제하려는 회사가 DB에 존재하는지 확인
        CompanyEntity companyEntity = this.companyRepository.findByTicker(ticker)
                .orElseThrow(NoCompanyException::new);

        // 해당 회사의 배당금 정보 삭제(회사 ID로)
        this.dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        // 해당 회사의 메타 데이터 삭제
        this.companyRepository.delete(companyEntity);
        // 자동 완성 제거: trie에 등록된 회사 이름 삭제
        this.deleteAutoCompleteKeyword(companyEntity.getName());

        return companyEntity.getName();
    }
}