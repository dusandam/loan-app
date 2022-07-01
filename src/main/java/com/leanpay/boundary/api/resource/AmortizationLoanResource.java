package com.leanpay.boundary.api.resource;

import com.leanpay.loan.application.AmortizationLoanDto;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class AmortizationLoanResource
    extends RepresentationModel<AmortizationLoanResource> {

    LoanResource loanResource;

    List<AmortizationLoanDto> amortization;

}
