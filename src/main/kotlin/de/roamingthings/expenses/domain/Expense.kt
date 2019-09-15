package de.roamingthings.expenses.domain

import java.util.*

data class MoneyAmount(val amount: Double, val currency: Currency) {
    val amountInCent: Int
        get() = (this.amount * 100).toInt()
}

data class Expense(val description: String, val amount: MoneyAmount)
