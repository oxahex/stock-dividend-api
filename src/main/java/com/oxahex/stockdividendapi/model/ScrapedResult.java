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

    private List<Dividend> dividendEntities;

    public ScrapedResult() {
        this.dividendEntities = new ArrayList<>();
    }
}