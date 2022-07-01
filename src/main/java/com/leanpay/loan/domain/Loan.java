package com.leanpay.loan.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Calculated loan with the purpose to record generated loan calculations
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Loan {

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Column()
    private Double amount;

    @Getter
    @Column()
    private Double interestRate;

    @Getter
    @Column()
    private Double months;

    @Getter
    @Column()
    private Double monthlyPayment;

    @Getter
    @Column()
    private Double totalInterest;

    public Loan(final Double amount,
                final Double interestRate,
                final Double months,
                final Double monthlyPayment,
                final Double totalInterest) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.months = months;
        this.monthlyPayment = monthlyPayment;
        this.totalInterest = totalInterest;
    }
}
