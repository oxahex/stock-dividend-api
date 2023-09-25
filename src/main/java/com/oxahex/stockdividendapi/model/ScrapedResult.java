package com.oxahex.stockdividendapi.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ScrapedResult {

    private Company company;

    private List<Dividend> dividendList;

    public ScrapedResult() {
        this.dividendList = new ArrayList<>();
    }
}