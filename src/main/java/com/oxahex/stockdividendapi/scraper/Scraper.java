package com.oxahex.stockdividendapi.scraper;

import com.oxahex.stockdividendapi.model.Company;
import com.oxahex.stockdividendapi.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}