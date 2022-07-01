package com.leanpay.loan.api;

import com.leanpay.boundary.api.LoanController;
import com.leanpay.boundary.api.resource.AmortizationLoanResource;
import com.leanpay.boundary.api.resource.LoanResource;
import com.leanpay.loan.application.AmortizationLoanDto;
import com.leanpay.loan.application.Frequency;
import com.leanpay.loan.application.LoanService;
import com.leanpay.loan.application.TimeUnit;
import com.leanpay.loan.domain.Loan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoanControllerImpl implements LoanController {

    private final LoanService loanService;

    @Override
    public LoanResource calculate(final Double amount,
                                  final Double interestRate,
                                  final Integer loanTerm,
                                  final TimeUnit timeUnit) {
        validateLoanParameters(amount,
                interestRate,
                loanTerm);
        final Loan loan = loanService.calculateLoan(
                amount,
                interestRate,
                loanTerm,
                timeUnit);
        return new LoanResource(loan);
    }

    @Override
    public AmortizationLoanResource calculateAmortization(final Double amount, final Double interestRate,
                                                          final Integer numberOfPayments, final Frequency frequency) {
        validateLoanParameters(amount, interestRate, numberOfPayments);
        final Loan loan = loanService.calculateAmortization(
                amount,
                interestRate,
                numberOfPayments,
                frequency);
        final List<AmortizationLoanDto> dtos = loanService.getAmortizationList(
                amount,
                numberOfPayments,
                frequency,
                interestRate,
                amount + loan.getTotalInterest());
        return new AmortizationLoanResource(new LoanResource(loan), dtos);
    }

    private void validateLoanParameters(final Double amount,
                                        final Double interestRate,
                                        final Integer loanTerm) {
        if (amount <= 0.0) {
            throw new ValidationException("Loan amount must be bigger that zero");
        } else if (interestRate <= 0.0) {
            throw new ValidationException("Loan interest rate must be bigger that zero");
        } else if (loanTerm <= 0) {
            throw new ValidationException("Loan term rate must be bigger that zero");
        }
    }
}
