package chernandez.blockedsupplybackend;

import chernandez.blockedsupplybackend.domain.State;
import chernandez.blockedsupplybackend.domain.dto.ShipmentInput;
import chernandez.blockedsupplybackend.domain.dto.ShipmentOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
class ShipmentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void deployContract(@NotNull @Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/deploy"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0x")))
                .andReturn();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateShipment() throws Exception {
        ShipmentInput shipmentInput = new ShipmentInput("Test Name", "Test Description", "Test Origin", "Test Destination", 10, 5);
        String shipmentInputJson = objectMapper.writeValueAsString(shipmentInput);

        this.mockMvc.perform(post("/api/shipment/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testCreateShipment_NegativeUnits() throws Exception {
        ShipmentInput shipmentInput = new ShipmentInput("Test Name", "Test Description", "Test Origin", "Test Destination", -10, 5);
        String shipmentInputJson = objectMapper.writeValueAsString(shipmentInput);

        this.mockMvc.perform(post("/api/shipment/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Units and weight must be non-negative.")))
                .andReturn();
    }

    @Test
    public void testCreateShipment_NegativeWeight() throws Exception {
        ShipmentInput shipmentInput = new ShipmentInput("Test Name", "Test Description", "Test Origin", "Test Destination", 10, -5);
        String shipmentInputJson = objectMapper.writeValueAsString(shipmentInput);

        this.mockMvc.perform(post("/api/shipment/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Units and weight must be non-negative.")))
                .andReturn();
    }

    @Test
    public void testGetShipment() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/shipment/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ShipmentOutput shipmentOutput = objectMapper.readValue(responseContent, ShipmentOutput.class);

        assertEquals(1, shipmentOutput.getId());
        assertEquals("Test Name", shipmentOutput.getName());
        assertEquals("Test Description", shipmentOutput.getDescription());
        assertEquals("Test Origin", shipmentOutput.getOrigin());
        assertEquals("Test Destination", shipmentOutput.getDestination());
        assertEquals(10, shipmentOutput.getUnits());
        assertEquals(5, shipmentOutput.getWeight());
        assertEquals(State.CREATED, shipmentOutput.getCurrentState());
    }



}
