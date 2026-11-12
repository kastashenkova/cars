package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@Sql(scripts = "/sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(roles = "ADMIN")
class CarCreationFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcClient jdbc;

    @Test
    void createCar_ShouldStoreRow() throws Exception {
        mockMvc.perform(post("/api/cars").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "carId": "1",
                                    "ownerEmail": "k.astashenkova@ukma.edu.ua",
                                    "vehicleIdentificationNumber": "1HGCM82633A004352",
                                    "model": "KIA",
                                    "color": "White",
                                    "carYear": 2020,
                                    "licensePlate": "АА1111ВВ",
                                    "fuelType": "PETROL",
                                    "transmission": "MANUAL"
                                }
                                """))
                .andExpect(status().isCreated());

        String ownerEmail = jdbc.sql("select owner_email from cars where car_id = ?")
                .param("1")
                .query(String.class)
                .single();
        assertThat(ownerEmail).isEqualTo("k.astashenkova@ukma.edu.ua");
    }

    @Test
    void createCarWithInvalidFields_ShouldShowFieldError() throws Exception {
        Integer rowsBefore = jdbc.sql("select count(*) from cars")
                .query(Integer.class)
                .single();

        mockMvc.perform(post("/api/cars").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "carId": "2",
                                    "ownerEmail": "invalid-email",
                                    "vehicleIdentificationNumber": "1",
                                    "model": "",
                                    "color": "",
                                    "carYear": 1885,
                                    "licensePlate": "1",
                                    "fuelType": "PETROL",
                                    "transmission": "MANUAL"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Data validation error")));

        int rowsChanged = jdbc.sql("select count(*) from cars")
                .query(Integer.class)
                .single();

        assertThat(rowsChanged).isEqualTo(rowsBefore);
    }
}