package com.leanpay.loan.domain

import com.leanpay.boundary.api.LoanController
import com.leanpay.loan.application.Frequency
import com.leanpay.loan.application.TimeUnit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.math.RoundingMode

@SpringBootTest
class LoanControllerSpec extends Specification {

    @Autowired
    private LoanController loanController

    def "Controller simple loan"() {
        given:
        def amount = 1000.0
        def interestRate = 5.0
        def term = 10

        when:
        def loanResource = loanController.calculate(
                amount,
                interestRate,
                term,
                TimeUnit.MONTHS)

        then: "Methods are invoked"
        assert new BigDecimal(loanResource.getMonthlyPayment()).setScale(2, RoundingMode.HALF_UP) == 102.31
        assert new BigDecimal(loanResource.getTotalInterest()).setScale(2, RoundingMode.HALF_UP) == 23.06
    }


    def "Controller amortization loan"() {
        given:
        def amount = 1000.0
        def interestRate = 5.0
        def payments = 10
        def frequency = Frequency.MONTHLY

        when:
        def amortizationLoanResource = loanController.calculateAmortization(
                amount,
                interestRate,
                payments,
                frequency)

        then: "Results are correct"
        assert new BigDecimal(amortizationLoanResource.getLoanResource().getTotalInterest()).setScale(2, RoundingMode.HALF_UP) == 23.06
        def firstElement = amortizationLoanResource.getAmortization().get(0)
        assert new BigDecimal(firstElement.principal()).setScale(2, RoundingMode.HALF_UP) == 98.14

    }


}
