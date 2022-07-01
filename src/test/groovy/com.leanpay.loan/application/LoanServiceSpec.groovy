package com.leanpay.loan.application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.math.RoundingMode

@SpringBootTest
class LoanServiceSpec extends Specification {

    @Autowired
    private LoanService loanService

    def "Simple loan calculation is correct"() {
        given:
        def amount = 1000.0
        def interestRate = 5.0
        def term = 10

        when:
        def loan = loanService.calculateLoan(
                amount,
                interestRate,
                term,
                TimeUnit.MONTHS)

        then: "Payment and interest are calculated correctly"
        assert new BigDecimal(loan.getMonthlyPayment()).setScale(2, RoundingMode.HALF_UP) == 102.31
        assert new BigDecimal(loan.getTotalInterest()).setScale(2, RoundingMode.HALF_UP) == 23.06
    }

    def "Amortization loan calculation is correct"() {
        given:
        def amount = 1000.0
        def interestRate = 5.0
        def payments = 10
        def frequency = Frequency.MONTHLY

        when:
        def loan = loanService.calculateAmortization(
                amount,
                interestRate,
                payments,
                frequency)

        then: "Calculations correct"
        assert new BigDecimal(loan.getTotalInterest()).setScale(2, RoundingMode.HALF_UP) == 23.06
    }

    def "Amortization table calculation is correct"() {
        given:
        def amount = 1000.0
        def interestRate = 5.0
        def payments = 10
        def frequency = Frequency.MONTHLY
        def totalPayment = 1023.06

        when:
        def amortizationList = loanService.getAmortizationList(
                amount,
                payments,
                frequency,
                interestRate,
                totalPayment)

        then: "Calculations correct"
        def firstElement = amortizationList.get(0)
        assert new BigDecimal(firstElement.principal()).setScale(2, RoundingMode.HALF_UP) == 98.14

        def thirdElement = amortizationList.get(2)
        assert new BigDecimal(thirdElement.interest()).setScale(2, RoundingMode.HALF_UP) == 3.33

        def lastElement = amortizationList.get(payments - 1)
        assert new BigDecimal(lastElement.interest()).setScale(2, RoundingMode.HALF_UP) == 0.42
    }


}
