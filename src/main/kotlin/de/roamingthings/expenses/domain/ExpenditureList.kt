package de.roamingthings.expenses.domain

import java.util.*

class ExpenditureList(
        val expenditureListUuid: ExpenditureListUuid = ExpenditureListUuid(),
        val name: String,
        private val expenses: MutableList<Expense> = mutableListOf()
) {
    fun putExpenseInto(expense: Expense): ExpenditureList {
        expenses.add(expense)
        return this
    }

    fun content(): List<Expense> {
        return expenses.toList()
    }
}

data class ExpenditureListUuid(val uuid: UUID = UUID.randomUUID()) {

    constructor(uuid: String) : this(UUID.fromString(uuid))

    override fun toString(): String = uuid.toString()
}
