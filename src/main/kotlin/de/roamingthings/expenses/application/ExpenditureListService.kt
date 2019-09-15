package de.roamingthings.expenses.application

import de.roamingthings.expenses.domain.ExpenditureList
import de.roamingthings.expenses.ports.driven.database.ExpenditureListRepositoryPort
import org.springframework.stereotype.Service

@Service
class ExpenditureListService(
        private val expenditureListRepositoryPort: ExpenditureListRepositoryPort
) {
    fun takeNewExpenditureList(): ExpenditureList {
        val expenditureList = ExpenditureList(name = "New Expenditure List")
        expenditureListRepositoryPort.save(expenditureList)
        return expenditureList
    }
}
