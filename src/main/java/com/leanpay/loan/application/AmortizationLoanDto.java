package com.leanpay.loan.application;


public record AmortizationLoanDto(Integer iteration, Double amount, Double principal,
                                  Double interest, Double balanceOwed) {

}
