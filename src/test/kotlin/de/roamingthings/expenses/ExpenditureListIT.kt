package de.roamingthings.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import de.roamingthings.expenses.domain.ExpenditureListUuid
import de.roamingthings.expenses.ports.driven.database.ExpenditureListRepositoryPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class ExpenditureListIT {

    @Autowired
    lateinit var expenditureListRepositoryPort: ExpenditureListRepositoryPort

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return new expenditure list`() {
        val requestResponse = mockMvc.perform(post("/api/expenditure-lists"))

        val location = requestResponse
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .getHeader("Location")

        assertThat(location).isNotNull()
        val uuid = location!!.split("/").last()
        assertThat(expenditureListRepositoryPort.load(ExpenditureListUuid(uuid))).isPresent
    }
}
