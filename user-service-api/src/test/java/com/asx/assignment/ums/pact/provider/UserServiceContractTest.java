package com.asx.assignment.ums.pact.provider;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import com.asx.assignment.ums.rest.dto.UserDTO;
import com.asx.assignment.ums.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRestPactRunner.class)
@Provider("UserServiceApiProvider")
@PactFolder("src/test/resources/pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("Pact")
public class UserServiceContractTest {
    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @State("Test Get Valid User")
    public Map<String, String> user1Exists() throws JsonProcessingException {
        return createAnUser();
    }
    @State("Test user not found")
    public void userNotFound() {}
    @State("Test create user")
    public void createUser() {}
    @State("Update user")
    public Map<String, String> updateUser() throws JsonProcessingException {
        return createAnUser();
    }
    @State("Invalid create")
    public void invalidCreateUser() {}
    @State("Invalid update")
    public void invalidUpdateUser() {}

    private Map<String, String> createAnUser() throws JsonProcessingException {
        final String userJson = "{" +
                "    \"title\": \"Mr.\"," +
                "    \"firstname\": \"John\"," +
                "    \"lastname\": \"Doe\"," +
                "    \"gender\": \"male\"," +
                "    \"address\": {" +
                "        \"street\": \"16 Bridge st\"," +
                "        \"city\": \"Sydney\"," +
                "        \"state\": \"NSW\"," +
                "        \"postcode\": 2000" +
                "    }" +
                "}";
        final UserDTO userDTO = userService.saveOrUpdate(objectMapper.readValue(userJson, UserDTO.class));
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("userId", userDTO.getId());
        valueMap.put("addressId", userDTO.getAddress().getId());
        return valueMap;
    }


}
