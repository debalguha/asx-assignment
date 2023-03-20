package com.asx.assignment.ums;

import com.asx.assignment.ums.UserManagementServiceApplication;
import com.asx.assignment.ums.model.Gender;
import com.asx.assignment.ums.rest.dto.AddressDTO;
import com.asx.assignment.ums.rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserManagementServiceApplication.class)
@AutoConfigureMockMvc
class UserManagementServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    EasyRandom easyRandom;

    @BeforeEach
    public void setup() {
        easyRandom = new EasyRandom(
                new EasyRandomParameters().excludeField(
                        FieldPredicates.named("id")
                                .and(FieldPredicates.ofType(String.class))
                                .and(FieldPredicates.inClass(UserDTO.class))
                ).excludeField(FieldPredicates.named("id")
                        .and(FieldPredicates.ofType(String.class))
                        .and(FieldPredicates.inClass(AddressDTO.class))
                )
        );
    }

    @Test
    void testUserCreated_POST() throws Exception {
        UserDTO userDTO = getUserDTO();
        String content = mockMvc.perform(post("/userdetails")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        UserDTO responseDTO = objectMapper.readValue(content, UserDTO.class);
        assertValues(userDTO, responseDTO);
    }

    @Test
    void testUserUpdate_PUT() throws Exception {
        UserDTO userDTO = getUserDTO();
        String postContent = mockMvc.perform(post("/userdetails")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        UserDTO createResponse = objectMapper.readValue(postContent, UserDTO.class);
        UserDTO changedResponse = new UserDTO(createResponse.getId(), createResponse.getTitle(), createResponse.getFirstName() + "_Update",
                createResponse.getLastName(), createResponse.getGender(), createResponse.getAddress());
        String putContent = mockMvc.perform(put("/userdetails")
                .content(objectMapper.writeValueAsString(changedResponse))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();
        UserDTO updateResponse = objectMapper.readValue(putContent, UserDTO.class);
        assertThat(changedResponse).isEqualTo(updateResponse);
    }

    @Test
    void testFindUser_GET() throws Exception {
        UserDTO userDTO = getUserDTO();
        String postContent = mockMvc.perform(post("/userdetails")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        UserDTO createResponse = objectMapper.readValue(postContent, UserDTO.class);
        String getContent = mockMvc.perform(get("/userdetails/" + createResponse.getId())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDTO getResponse = objectMapper.readValue(getContent, UserDTO.class);
        assertThat(createResponse).isEqualTo(getResponse);
    }

    @Test
    void testFindUser_GET_NotExists() throws Exception {
        mockMvc.perform(get("/userdetails/999999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testInvalidFirstAndLastName() throws Exception {
        String userDtoWithInvalidGender = "{\"id\":null,\"title\":\"eOMtThyhVNLWUZNRcBaQKxI\",\"firstname\":null,\"lastname\":null,\"gender\":\"MALE\",\"address\":{\"street\":\"RYtGKbgicZaHCBRQDSx\",\"city\":\"VLhpfQGTMDYpsBZxvfBoeygjb\",\"state\":\"ACT\",\"postCode\":2000}}";
        mockMvc.perform(post("/userdetails")
                .content(userDtoWithInvalidGender)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations[?(@.fieldName == 'firstName')].message", contains("must not be empty")))
                .andExpect(jsonPath("$.violations[?(@.fieldName == 'lastName')].message", contains("must not be empty")));
    }

    @Test
    void testUserUpdate_InvalidId() throws Exception {
        UserDTO userDTO = getUserDTO();
        String postContent = mockMvc.perform(post("/userdetails")
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        UserDTO createResponse = objectMapper.readValue(postContent, UserDTO.class);
        UserDTO changedResponse = new UserDTO("BLAH", createResponse.getTitle(), createResponse.getFirstName() + "_Update",
                createResponse.getLastName(), createResponse.getGender(), createResponse.getAddress());
        mockMvc.perform(put("/userdetails")
                .content(objectMapper.writeValueAsString(changedResponse))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[?(@.fieldName == 'id')].message", contains("Invalid id provided. Must be a positive number.")));
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
        userDTO.getAddress().setPostCode(2000);
        return userDTO;
    }

    void assertValues(UserDTO expected, UserDTO actual) {
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getAddress().getStreet()).isEqualTo(expected.getAddress().getStreet());
        assertThat(actual.getAddress().getCity()).isEqualTo(expected.getAddress().getCity());
        assertThat(actual.getAddress().getState()).isEqualTo(expected.getAddress().getState());
        assertThat(actual.getAddress().getPostCode()).isEqualTo(expected.getAddress().getPostCode());
    }

}
