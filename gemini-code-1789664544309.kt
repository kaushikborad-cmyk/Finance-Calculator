package com.fixedreturn.india.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class YearlyBreakdown(
    val year: Int,
    val openingBalance: BigDecimal,
    val totalContribution: BigDecimal,
    val interestEarned: BigDecimal,
    val closingBalance: BigDecimal
)

data class CalculationResult(
    val schemeId: String,
    val totalInvested: BigDecimal,
    val totalInterest: BigDecimal,
    val maturityValue: BigDecimal,
    val maturityDate: LocalDate,
    val yearlyBreakdown: List<YearlyBreakdown>,
    val explanation: String
)