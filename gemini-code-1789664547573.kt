package com.fixedreturn.india.domain.rules

import com.fixedreturn.india.domain.model.CalculationResult
import com.fixedreturn.india.domain.model.YearlyBreakdown
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class PPFCalculator {

    fun calculate(
        annualContribution: BigDecimal,
        depositDayOfMonth: Int = 1, // Default deposit on 1st of month
        rate: Double = 7.1,
        tenureYears: Int = 15,
        startDate: LocalDate = LocalDate.now()
    ): CalculationResult {
        val annualRate = BigDecimal.valueOf(rate)
        val monthlyRate = annualRate.divide(BigDecimal("1200"), 10, RoundingMode.HALF_UP)
        
        var currentBalance = BigDecimal.ZERO
        var totalInvested = BigDecimal.ZERO
        val breakdown = mutableListOf<YearlyBreakdown>()

        val monthlyDeposit = annualContribution.divide(BigDecimal("12"), 2, RoundingMode.HALF_UP)

        for (year in 1..tenureYears) {
            val openingBalance = currentBalance
            var yearContribution = BigDecimal.ZERO
            var interestAccumulatedThisYear = BigDecimal.ZERO

            for (month in 1..12) {
                // If deposit occurs after the 5th of the month, interest is NOT accrued for that month's deposit
                val eligibleDepositForMonth = if (depositDayOfMonth <= 5) {
                    monthlyDeposit
                } else {
                    BigDecimal.ZERO
                }

                // Balance eligible for monthly interest calculation
                val eligibleMonthlyBalance = currentBalance.add(eligibleDepositForMonth)
                val monthInterest = eligibleMonthlyBalance.multiply(monthlyRate)
                
                interestAccumulatedThisYear = interestAccumulatedThisYear.add(monthInterest)
                currentBalance = currentBalance.add(monthlyDeposit)
                yearContribution = yearContribution.add(monthlyDeposit)
            }

            // Interest compounded annually at end of Financial Year
            val roundedYearlyInterest = interestAccumulatedThisYear.setScale(0, RoundingMode.HALF_UP)
            currentBalance = currentBalance.add(roundedYearlyInterest)
            totalInvested = totalInvested.add(yearContribution)

            breakdown.add(
                YearlyBreakdown(
                    year = year,
                    openingBalance = openingBalance.setScale(2, RoundingMode.HALF_UP),
                    totalContribution = yearContribution.setScale(2, RoundingMode.HALF_UP),
                    interestEarned = roundedYearlyInterest,
                    closingBalance = currentBalance.setScale(2, RoundingMode.HALF_UP)
                )
            )
        }

        val totalInterest = currentBalance.subtract(totalInvested)
        val explanation = if (depositDayOfMonth <= 5) {
            "Contributions deposited on or before the 5th earn full interest for that month."
        } else {
            "Deposits made after the 5th lose monthly interest accrual for that month per PPF rules."
        }

        return CalculationResult(
            schemeId = "PPF",
            totalInvested = totalInvested.setScale(2, RoundingMode.HALF_UP),
            totalInterest = totalInterest.setScale(2, RoundingMode.HALF_UP),
            maturityValue = currentBalance.setScale(2, RoundingMode.HALF_UP),
            maturityDate = startDate.plusYears(tenureYears.toLong()),
            yearlyBreakdown = breakdown,
            explanation = explanation
        )
    }
}