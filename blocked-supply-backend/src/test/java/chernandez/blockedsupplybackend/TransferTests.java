package chernandez.blockedsupplybackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testTransferShipment() throws Exception {

    }

    @Test
    public void testTransferShipment_InvalidShipmentId() throws Exception {

    }

    @Test
    public void testGetTransferHistory() throws Exception {

    }
}
