package de.roamingthings.expenses.ports.driver.rest

import de.roamingthings.expenses.application.ExpenditureListService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import java.net.URI

@Controller
class ExpenditureListController(private val expenditureListService: ExpenditureListService) {

    @PostMapping("/api/expenditure-lists")
    fun createExpenditureList(): ResponseEntity<ExpenditureListResponse> {
        val list = expenditureListService.takeNewExpenditureList()
        val uri = URI("/api/expenditure-lists/${list.expenditureListUuid}")

        return ResponseEntity.created(uri).build()
    }
}
