package chernandez.blockedsupplybackend;

import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.TransferInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void deployContract(@NotNull @Autowired MockMvc mockMvc, @NotNull @Autowired ObjectMapper objectMapper) throws Exception {
        mockMvc.perform(post("/api/deploy"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0x")))
                .andReturn();

        ShipmentInput shipmentInput = new ShipmentInput("Test Name", "Test Description", "Test Origin", "Test Destination", 10, 5);
        String shipmentInputJson = objectMapper.writeValueAsString(shipmentInput);

        mockMvc.perform(post("/api/shipment/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testTransferShipment() throws Exception {
        TransferInput transferInput = new TransferInput(1, "0xNewOwnerAddress", 1, "Transferred to new owner");
        String transferInputJson = objectMapper.writeValueAsString(transferInput);

        this.mockMvc.perform(post("/api/transfer/create")
                        .contentType("application/json")
                        .content(transferInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testTransferShipment_InvalidShipmentId() throws Exception {
        TransferInput transferInput = new TransferInput(-1, "0xNewOwnerAddress", 2, "Invalid shipment ID");
        String transferInputJson = objectMapper.writeValueAsString(transferInput);

        this.mockMvc.perform(post("/api/transfer/create")
                        .contentType("application/json")
                        .content(transferInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid shipment ID")))
                .andReturn();
    }

    @Test
    public void testGetTransferHistory() throws Exception {
        TransferInput transferInput = new TransferInput(1, "0xNewOwnerAddress", 2, "Transferred to new owner");
        String transferInputJson = objectMapper.writeValueAsString(transferInput);

        this.mockMvc.perform(post("/api/transfer/create")
                        .contentType("application/json")
                        .content(transferInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result = this.mockMvc.perform(get("/api/transfer/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<?> transferHistory = objectMapper.readValue(responseContent, List.class);

        assertEquals(1, transferHistory.size());
    }
}
