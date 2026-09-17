package com.fixedreturn.india

import com.fixedreturn.india.domain.rules.PPFCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class FinancialEngineTest {

    @Test
    fun verify_ppf_calculation_accrual() {
        val calculator = PPFCalculator()
        // Max PPF contribution per year: 1,50,000 for 15 years @ 7.1%
        val result = calculator.calculate(
            annualContribution = BigDecimal("150000"),
            depositDayOfMonth = 1,
            rate = 7.1,
            tenureYears = 15
        )

        val expectedInvested = BigDecimal("2250000.00")
        assertEquals(expectedInvested, result.totalInvested)

        // Verify total maturity falls within standard non-taxable PPF ledger limits (~40.68 Lakhs)
        assert(result.maturityValue > BigDecimal("4000000"))
    }
}