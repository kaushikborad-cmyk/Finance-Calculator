package com.fixedreturn.india.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    fun formatToINR(amount: BigDecimal): String {
        val locale = Locale("en", "IN")
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}