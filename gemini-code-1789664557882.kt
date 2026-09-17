package com.fixedreturn.india.domain.parser

import java.math.BigDecimal

data class ParsedIntent(
    val schemeId: String?,
    val amount: BigDecimal?,
    val tenureYears: Int?
)

class NaturalLanguageParser {

    fun parse(input: String): ParsedIntent {
        val query = input.lowercase()
        
        // Scheme Matching
        val schemeId = when {
            query.contains("ppf") || query.contains("provident") -> "PPF"
            query.contains("scss") || query.contains("senior") -> "SCSS"
            query.contains("sukanya") || query.contains("ssy") -> "SSY"
            query.contains("nsc") -> "NSC"
            query.contains("kvp") -> "KVP"
            else -> null
        }

        // Extract Amount Parsing Indian terms
        var amount: BigDecimal? = null
        val lakhRegex = Regex("([0-9]+(\\.[0-9]+)?)\\s*(lakh|lakhs|l)")
        val croreRegex = Regex("([0-9]+(\\.[0-9]+)?)\\s*(crore|crores|cr)")
        val kRegex = Regex("([0-9]+(\\.[0-9]+)?)\\s*(k|thousand)")

        if (lakhRegex.containsMatchIn(query)) {
            val match = lakhRegex.find(query)
            val valStr = match?.groupValues?.get(1)
            if (valStr != null) amount = BigDecimal(valStr).multiply(BigDecimal("100000"))
        } else if (croreRegex.containsMatchIn(query)) {
            val match = croreRegex.find(query)
            val valStr = match?.groupValues?.get(1)
            if (valStr != null) amount = BigDecimal(valStr).multiply(BigDecimal("10000000"))
        } else if (kRegex.containsMatchIn(query)) {
            val match = kRegex.find(query)
            val valStr = match?.groupValues?.get(1)
            if (valStr != null) amount = BigDecimal(valStr).multiply(BigDecimal("1000"))
        }

        // Tenure Matching
        val tenureRegex = Regex("([0-9]+)\\s*(years|year|yrs)")
        val tenure = tenureRegex.find(query)?.groupValues?.get(1)?.toIntOrNull()

        return ParsedIntent(schemeId = schemeId, amount = amount, tenureYears = tenure)
    }
}