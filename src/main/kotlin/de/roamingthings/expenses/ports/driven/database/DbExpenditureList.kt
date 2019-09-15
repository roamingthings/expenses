package de.roamingthings.expenses.ports.driven.database

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "ExpenditureList")
data class DbExpenditureList(
        @Id
        var expenditureListUuid: String?,

        var name: String?,

        @OneToMany(fetch = EAGER, cascade = [ALL])
        var expenses: MutableList<DbExpense>?
)
