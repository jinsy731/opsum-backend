package com.opusm.backend.api.controller;

import com.opusm.backend.common.support.test.BaseApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.opusm.backend.customer.dto.CustomerCreateDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerApiControllerTest extends BaseApiTest {

    @Test
    void 고객_생성_성공() throws Exception {

        CustomerCreateRequest req = new CustomerCreateRequest("customer1", 1000, 23300);

        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.name").value(req.getName()),
                        jsonPath("$.assets").value(1000),
                        jsonPath("$.points").value(23300),
                        jsonPath("$.createdDate").isNotEmpty()
                );
    }

}