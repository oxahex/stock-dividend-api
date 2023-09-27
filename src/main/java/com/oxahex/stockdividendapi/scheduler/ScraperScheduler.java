package com.oxahex.stockdividendapi.scheduler;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.model.constants.CacheKey;
import com.oxahex.stockdividendapi.persist.CompanyRepository;
import com.oxahex.stockdividendapi.persist.DividendRepository;
import com.oxahex.stockdividendapi.persist.entity.CompanyEntity;
import com.oxahex.stockdividendapi.persist.entity.DividendEntity;
import com.oxahex.stockdividendapi.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyEntityList =
                this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity companyEntity : companyEntityList) {
            log.info("Scraping Scheduler is Started -> " + companyEntity.getName());

            ScrapedResult scrapedResult =
                    yahooFinanceScraper.scrap(
                            new Company(companyEntity.getName(), companyEntity.getTicker())
                    );

            // 스크래핑한 배당금 정보 중 DB에 없는 값은 저장
                // dividend 모델 -> 엔티티로 매핑
                // 하나씩 DB에 저장
            scrapedResult.getDividendList().stream()
                    .map(e -> new DividendEntity(companyEntity.getId(), e))
                    .forEach(e -> {
                        boolean exists = this.dividendRepository
                                .existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) this.dividendRepository.save(e);
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 넣지 않도록 일시 정지
            try {
                Thread.sleep(3000);     // 3초간 정지
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();     // 현재 스레드에 interrupt
            }
        }

    }
}