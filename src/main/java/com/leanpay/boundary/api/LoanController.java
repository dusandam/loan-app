package com.leanpay.boundary.api;

import com.leanpay.boundary.api.resource.AmortizationLoanResource;
import com.leanpay.boundary.api.resource.LoanResource;
import com.leanpay.loan.application.Frequency;
import com.leanpay.loan.application.TimeUnit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Loan Administration")
@RequestMapping(path = LoanController.BASE_PATH)
public interface LoanController {

    String CURRENT_VERSION_JSON = "application/vnd.com.leanpay-loan-v1+json";

    String BASE_PATH = "/api/loans";

    @Operation(summary = "Loan calculator")
    @PostMapping(produces = {CURRENT_VERSION_JSON, APPLICATION_JSON_VALUE})
    LoanResource calculate(@RequestParam final Double amount,
                           @RequestParam final Double interestRate,
                           @RequestParam final Integer loanTerm,
                           @RequestParam final TimeUnit timeUnit);

    @Operation(summary = "Amortization schedule calculator")
    @PostMapping(value = "/amortization", produces = {CURRENT_VERSION_JSON, APPLICATION_JSON_VALUE})
    AmortizationLoanResource calculateAmortization(@RequestParam final Double amount,
                                                   @RequestParam final Double interestRate,
                                                   @RequestParam final Integer numberOfPayments,
                                                   @RequestParam final Frequency frequency);
}
