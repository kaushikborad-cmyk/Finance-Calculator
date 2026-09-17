package com.fixedreturn.india.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fixedreturn.india.domain.rules.PPFCalculator
import com.fixedreturn.india.utils.CurrencyFormatter
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
ComposablePPFCalculatorScreen() {
    var annualDeposit by remember { mutableFloatStateOf(150000f) }
    var depositDay by remember { mutableIntStateOf(1) }

    val calculator = remember { PPFCalculator() }
    val result = remember(annualDeposit, depositDay) {
        calculator.calculate(
            annualContribution = BigDecimal(annualDeposit.toDouble()),
            depositDayOfMonth = depositDay
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("PPF Calculator") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Maturity Value (15 Yrs)", style = MaterialTheme.typography.labelMedium)
                    Text(
                        CurrencyFormatter.formatToINR(result.maturityValue),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Invested: ${CurrencyFormatter.formatToINR(result.totalInvested)}")
                        Text("Interest: ${CurrencyFormatter.formatToINR(result.totalInterest)}")
                    }
                }
            }

            // Input Controls
            Text("Annual Deposit: ${CurrencyFormatter.formatToINR(BigDecimal(annualDeposit.toDouble()))}")
            Slider(
                value = annualDeposit,
                onValueChange = { annualDeposit = it },
                valueRange = 500f..150000f,
                steps = 29
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Deposit timing: ")
                FilterChip(
                    selected = depositDay <= 5,
                    onClick = { depositDay = 1 },
                    label = { Text("On/Before 5th") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = depositDay > 5,
                    onClick = { depositDay = 10 },
                    label = { Text("After 5th") }
                )
            }

            // Disclaimer & Explanation
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = result.explanation,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}