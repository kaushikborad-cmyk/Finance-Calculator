package com.fixedreturn.india.domain.rules

import com.fixedreturn.india.domain.model.CalculationResult
import com.fixedreturn.india.domain.model.YearlyBreakdown
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class SCSSCalculator {

    fun calculate(
        principal: BigDecimal,
        rate: Double = 8.2,
        tenureYears: Int = 5,
        startDate: LocalDate = LocalDate.now()
    ): CalculationResult {
        val annualRate = BigDecimal.valueOf(rate).divide(BigDecimal("100"), 10, RoundingMode.HALF_UP)
        val quarterlyInterest = principal.multiply(annualRate).divide(BigDecimal("4"), 2, RoundingMode.HALF_UP)
        val yearlyInterest = quarterlyInterest.multiply(BigDecimal("4"))

        val totalInterest = yearlyInterest.multiply(BigDecimal(tenureYears))
        val maturityValue = principal.add(totalInterest)

        val breakdown = (1..tenureYears).map { year ->
            YearlyBreakdown(
                year = year,
                openingBalance = principal,
                totalContribution = if (year == 1) principal else BigDecimal.ZERO,
                interestEarned = yearlyInterest,
                closingBalance = principal
            )
        }

        return CalculationResult(
            schemeId = "SCSS",
            totalInvested = principal,
            totalInterest = totalInterest,
            maturityValue = maturityValue,
            maturityDate = startDate.plusYears(tenureYears.toLong()),
            yearlyBreakdown = breakdown,
            explanation = "Interest is paid quarterly (₹$quarterlyInterest per quarter). Principal remains unchanged."
        )
    }
}