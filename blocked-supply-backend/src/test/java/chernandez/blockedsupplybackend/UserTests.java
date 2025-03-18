package chernandez.blockedsupplybackend;

import chernandez.blockedsupplybackend.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateUser() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(new User("John Doe", "john@example.com", "hashedPassword123", List.of("ADMIN", "CUSTOMER")));
        String shipmentInputJson = objectMapper.writeValueAsString(userRegisterDTO);

        MvcResult result = this.mockMvc.perform(post("/api/user/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);

        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertTrue(user.getPassword().startsWith("$"));
        assertEquals(List.of("ADMIN", "CUSTOMER"), user.getRoles());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    public void testGetUserById() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(new User("Johan Cruyff", "john@example.com", "hashedPassword123", List.of("ADMIN", "CUSTOMER")));
        String shipmentInputJson = objectMapper.writeValueAsString(userRegisterDTO);

        MvcResult result = this.mockMvc.perform(post("/api/user/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);
        long userId = user.getId();

        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Johan Cruyff", user.getName());
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(new User("John Doe", "john@example.com", "hashedPassword123", List.of("ADMIN", "CUSTOMER")));
        String shipmentInputJson = objectMapper.writeValueAsString(userRegisterDTO);

        MvcResult result = this.mockMvc.perform(post("/api/user/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);
        long userId = user.getId();

        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }

    @Test
    public void testSetUserAddress() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(new User("John Doe", "john@example.com", "hashedPassword123", List.of("ADMIN", "CUSTOMER")));
        String shipmentInputJson = objectMapper.writeValueAsString(userRegisterDTO);

        MvcResult result = this.mockMvc.perform(post("/api/user/create")
                        .contentType("application/json")
                        .content(shipmentInputJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);
        long userId = user.getId();

        String userAddress = "0x10";

        UserAddressInput userAddressInput = new UserAddressInput(userAddress);
        String addressJson = objectMapper.writeValueAsString(userAddressInput);

        result = mockMvc.perform(put("/api/user/address/" + userId)
                        .contentType("application/json")
                        .content(addressJson))
                .andExpect(status().isOk())
                .andReturn();

        responseContent = result.getResponse().getContentAsString();
        user = objectMapper.readValue(responseContent, User.class);

        assertEquals(userAddress, user.getUserAddress());
    }

}
