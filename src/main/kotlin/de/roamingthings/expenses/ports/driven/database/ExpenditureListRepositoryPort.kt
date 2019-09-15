package de.roamingthings.expenses.ports.driven.database

import de.roamingthings.expenses.domain.ExpenditureList
import de.roamingthings.expenses.domain.ExpenditureListUuid
import org.springframework.stereotype.Component
import java.util.*

@Component
class ExpenditureListRepositoryPort(private val expenditureListRepository: ExpenditureListJpaRepository) {
    fun save(expenditureList: ExpenditureList) {

        val expenseList: MutableList<DbExpense> = expenditureList.content()
                .map { expense -> DbExpense.fromExpense(expense) }
                .toMutableList()

        val dbExpenditureList = DbExpenditureList(
                expenditureList.expenditureListUuid.toString(),
                expenditureList.name,
                expenseList
        )

        expenditureListRepository.save(dbExpenditureList)
    }

    fun load(expenditureListUuid: ExpenditureListUuid): Optional<ExpenditureList> {
        val optional: Optional<DbExpenditureList> = expenditureListRepository.findById(expenditureListUuid.toString())

        return optional
                .map { dbExpenditureList ->
                    val expenseList = dbExpenditureList.expenses!!.map { dbExpense -> dbExpense.toExpense() }.toList().toMutableList()
                    ExpenditureList(ExpenditureListUuid(dbExpenditureList.expenditureListUuid!!), dbExpenditureList.name!!, expenseList)
                }
    }
}
