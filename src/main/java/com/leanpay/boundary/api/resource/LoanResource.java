package com.leanpay.boundary.api.resource;

import com.leanpay.loan.domain.Loan;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@EqualsAndHashCode(callSuper = true)
public class LoanResource
    extends RepresentationModel<LoanResource> {

    Double monthlyPayment;

    Double totalInterest;

    public LoanResource(final Loan loan) {
        this.monthlyPayment = loan.getMonthlyPayment();
        this.totalInterest = loan.getTotalInterest();
    }

}
