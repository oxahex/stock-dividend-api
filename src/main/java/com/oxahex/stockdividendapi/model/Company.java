package com.oxahex.stockdividendapi.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Company {

    private String ticker;
    private String name;
}