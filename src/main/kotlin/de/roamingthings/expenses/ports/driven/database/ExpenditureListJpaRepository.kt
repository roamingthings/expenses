package de.roamingthings.expenses.ports.driven.database

import org.springframework.data.jpa.repository.JpaRepository

interface ExpenditureListJpaRepository: JpaRepository<DbExpenditureList, String>
