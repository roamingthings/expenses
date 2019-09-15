package de.roamingthings.expenses.ports.driven.database

import de.roamingthings.expenses.domain.Expense
import de.roamingthings.expenses.domain.MoneyAmount
import java.util.*
import javax.persistence.*

@Entity(name = "Expense")
class DbExpense(
        @Id
        @GeneratedValue
        var expenseId: String?,

        var description: String?,

        var amount: Int?,

        var currencyCode: String?
) {
    fun toExpense(): Expense {
        return Expense(this.description!!, MoneyAmount((amount!!.toDouble() / 100.0), Currency.getInstance(this.currencyCode)))
    }

    companion object {
        fun fromExpense(expense: Expense): DbExpense {
            return DbExpense(null, expense.description, expense.amount.amountInCent, expense.amount.currency.currencyCode)
        }
    }
}
