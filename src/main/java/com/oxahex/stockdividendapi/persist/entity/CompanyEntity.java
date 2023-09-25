package com.oxahex.stockdividendapi.persist.entity;

import com.oxahex.stockdividendapi.model.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString
@Entity(name = "COMPANY")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String ticker;

    public CompanyEntity(Company company) {
        this.name = company.getName();
        this.ticker = company.getTicker();
    }
}