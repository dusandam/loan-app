package com.leanpay.loan.application;

import com.leanpay.loan.domain.Loan;
import com.leanpay.loan.domain.LoanRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    public static final int MONTHS_IN_YEAR = 12;

    private final LoanRepository repository;

    @Override
    public Loan calculateLoan(@NonNull Double amount,
                              @NonNull Double interestRate,
                              @NonNull Integer loanTerm,
                              @NonNull TimeUnit timeUnit) {
        final Double months = timeUnit.equals(TimeUnit.MONTHS) ? Double.valueOf(loanTerm) : Double.valueOf(loanTerm * MONTHS_IN_YEAR);
        final Double monthlyInterest = calculateMonthlyInterest(interestRate);
        final Double monthlyPayment = calculateMonthlyPayment(amount, monthlyInterest, months);

        final Loan loan = new Loan(
                amount,
                interestRate,
                months,
                monthlyPayment,
                monthlyPayment * months - amount
        );
        return repository.save(loan);
    }

    private Double calculateMonthlyInterest(final Double interestRate) {
        return interestRate / (100 * 12);
    }


    @Override
    public Loan calculateAmortization(@NonNull Double amount,
                                      @NonNull Double interestRate,
                                      @NonNull Integer numberOfPayments,
                                      @NonNull Frequency frequency) {
        final Double years = convertToYears(numberOfPayments, frequency);
        final Double months = years * 12;
        final Double monthlyInterest = calculateMonthlyInterest(interestRate);
        final Double payment = calculateMonthlyPayment(amount, monthlyInterest, months);

        return repository.save(new Loan(
                amount,
                interestRate,
                months,
                payment,
                payment * months - amount
        ));
    }

    @Override
    public List<AmortizationLoanDto> getAmortizationList(@NonNull Double loanAmount,
                                                         @NonNull Integer numberOfPayments,
                                                         @NonNull Frequency frequency,
                                                         @NonNull Double interestRate,
                                                         @NonNull Double totalPayment) {
        final List<AmortizationLoanDto> loanDtos = new ArrayList<>();
        double balanceOwed = totalPayment;
        final double monthlyAmount = totalPayment / numberOfPayments;
        for (int i = 0; i < numberOfPayments; i++) {
            final double interest = getUnitInterest(loanAmount, numberOfPayments, frequency, interestRate, i);
            final AmortizationLoanDto dto = new AmortizationLoanDto(
                    i + 1,
                    monthlyAmount,
                    monthlyAmount - interest,
                    interest,
                    new BigDecimal(balanceOwed - monthlyAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()
            );
            balanceOwed -= monthlyAmount;
            loanDtos.add(dto);
        }
        return loanDtos;
    }

    private double getUnitInterest(final Double loanAmount, final Integer numberOfPayments,
                                   final Frequency frequency, final Double interestRate, final int i) {
        return (interestRate / 100) * (loanAmount * convertFrequency(frequency))
                * (numberOfPayments - i) / numberOfPayments;
    }


    private Double convertToYears(final Integer numberOfPayments, final Frequency frequency) {
        Double years = convertFrequency(frequency);

        return years * numberOfPayments;
    }

    private Double convertFrequency(final Frequency frequency) {
        return switch (frequency) {
            case DAILY -> ((double) 1 / (double) 365);
            case WEEKLY -> ((double) 1 / (double) 52);
            case BIWEEKLY -> ((double) 1 / (double) 26);
            case SEMIMONTHLY -> ((double) 1 / (double) 24);
            case BIMONTHLY -> ((double) 1 / (double) 6);
            case SEMIANNUAL -> ((double) 1 / (double) 2);
            case ANNUAL -> (double) (1);
            default -> ((double) 1 / (double) 12); //monthly
        };
    }

    private Double calculateMonthlyPayment(final Double amount, final Double interestRate, final Double months) {
        return amount * interestRate * Math.pow((1 + interestRate), months)
                / (Math.pow((1 + interestRate), months) - 1);
    }
}
