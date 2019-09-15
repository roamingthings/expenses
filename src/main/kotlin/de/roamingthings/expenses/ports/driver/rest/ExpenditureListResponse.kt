package de.roamingthings.expenses.ports.driver.rest

import de.roamingthings.expenses.ports.driver.rest.Method.GET
import de.roamingthings.expenses.ports.driver.rest.Method.PUT
import java.net.URI
import java.util.*

data class Expense(
        val description: String,
        val amount: Double,
        val currencyCode: String
)

data class ExpenditureListResponse(
        val uuid: UUID,
        val name: String,
        val expenses: List<Expense>
) : Linked<ExpenditureListResponse>() {

    private fun withLinks(): ExpenditureListResponse {
        return addLink("self", GET, URI("/expenditure-lists/${uuid}"))
                .addLink("addExpense", PUT, URI("/expenditure-lists/${uuid}/expenses"))
    }
}
