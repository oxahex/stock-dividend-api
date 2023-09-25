package com.oxahex.stockdividendapi.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Dividend {

    private LocalDateTime date;
    private String dividend;
}