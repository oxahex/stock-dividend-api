package com.oxahex.stockdividendapi.scraper;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.Dividend;
import com.oxahex.stockdividendapi.model.ScrapedResult;
import com.oxahex.stockdividendapi.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {

    // heap 영역에 할당
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86499;   // 60 * 60 * 24

    public ScrapedResult scrap(Company company) {

        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;   // 초 단위

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            System.out.println("url: " + url);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsedDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element table = parsedDivs.get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) continue;

                String[] splits = txt.split(" ");

                int mo = Month.strToNumber(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                // Validation:  잘못된 월 이름
                if (mo < 0) {
                    throw new RuntimeException("Unexpected Month Enum Value: " + splits[0]);
                }

                // Dividend 객체 생성
                dividends.add(
                        Dividend.builder()
                                .date(LocalDateTime.of(year, mo, day, 0, 0))
                                .dividend(dividend).build()
                );
            }

            scrapedResult.setDividendEntities(dividends);

        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        return scrapedResult;
    }

    /**
     * 특정 회사의 메타 정보 스크랩
     * @param ticker 회사 ticker
     * @return 해당 회사 정보
     */
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleElem = document.getElementsByTag("h1").get(0);

            // 모바일인 경우 -, 데스크톱 환경인 경우 ()
            // TODO: Jsoup에서 스크래핑 시 사용하는 내부 브라우저를 모바일 또는 데스크톱으로 고정할 수 있는지 확인
            String title;
            if (titleElem.text().contains("-")) {
                title = titleElem.text().split("-")[1].trim();
            } else {
                title = titleElem.text().split("\\(")[0].trim();
            }
            System.out.println(title);
            return Company.builder()
                    .ticker(ticker)
                    .name(title).build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}