package com.leanpay.loan.application;

import com.leanpay.loan.domain.Loan;
import lombok.NonNull;

import java.util.List;

public interface LoanService {

    Loan calculateLoan(
            @NonNull final Double amount,
            @NonNull final Double interestRate,
            @NonNull final Integer loanTerm,
            @NonNull final TimeUnit timeUnit);

    Loan calculateAmortization(
            @NonNull final Double amount, @NonNull final Double interestRate,
            @NonNull final Integer numberOfPayments, @NonNull final Frequency frequency);

    List<AmortizationLoanDto> getAmortizationList(@NonNull Double loanAmount,
                                                  @NonNull Integer numberOfPayments,
                                                  @NonNull Frequency frequency,
                                                  @NonNull Double interestRate,
                                                  @NonNull Double totalPayment);
}
