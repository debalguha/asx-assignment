package com.asx.assignment.ums;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserServiceApiProvider", hostInterface="localhost")
public class UserServicePactConsumerTest {
    @Pact(provider="UserServiceApiProvider", consumer = "userServiceConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, String> responseHeaders = new HashMap<>(headers);
        responseHeaders.put("Location", "/api/userdetails/1");

        return builder
                .given("Test create user")
                .uponReceiving("A POST request with valid json body")
                .path("/api/userdetails")
                .method("POST")
                .headers(headers)
                .body("{" +
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
                        "}")
                .willRespondWith()
                .status(HttpStatus.CREATED.value())
                .body(new PactDslJsonBody()
                        .valueFromProviderState("id", "${userId}", "1")
                        .stringType("title", "Mr.")
                        .stringValue("firstname", "John")
                        .stringValue("lastname", "Doe")
                        .stringValue("gender", "male")
                        .object("address")
                        .valueFromProviderState("id", "${addressId}", "1")
                        .stringValue("street", "16 Bridge st")
                        .stringValue("city", "Sydney")
                        .stringValue("state", "NSW")
                        .integerType("postcode", 2000)
                        .closeObject()
                )
                .matchHeader("Location", "^http:\\/\\/[a-z.0-9]+:\\d+\\/api\\/userdetails\\/\\d+$")
                .given("Test Get Valid User")
                .uponReceiving("A GET request with valid id")
                .path("/api/userdetails/1")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.OK.value())
                .headers(headers)
                .body(new PactDslJsonBody()
                        .valueFromProviderState("id", "${userId}", "1")
                        .stringValue("title", "Mr.")
                        .stringValue("firstname", "John")
                        .stringValue("lastname", "Doe")
                        .stringValue("gender", "male")
                        .object("address")
                        .valueFromProviderState("id", "${addressId}", "1")
                        .stringValue("street", "16 Bridge st")
                        .stringValue("city", "Sydney")
                        .stringValue("state", "NSW")
                        .integerType("postcode", 2000)
                        .closeObject()
                )
                .given("Test user not found")
                .uponReceiving("A GET request with valid id, that is not present")
                .path("/api/userdetails/2")
                .method("GET")
                .willRespondWith()
                .status(HttpStatus.NOT_FOUND.value())
                .given("Update user")
                .uponReceiving("A PUT request with valid json body")
                .path("/api/userdetails")
                .method("PUT")
                .headers(headers)
                .body(new PactDslJsonBody()
                        .valueFromProviderState("id", "${userId}", "1")
                        .stringValue("title", "Mr.")
                        .stringValue("firstname", "John")
                        .stringValue("lastname", "Doe")
                        .stringValue("gender", "female")
                        .object("address")
                        .valueFromProviderState("id", "${addressId}", "1")
                        .stringValue("street", "16 Bridge st")
                        .stringValue("city", "Sydney")
                        .stringValue("state", "NSW")
                        .integerType("postcode", 2000)
                        .closeObject()
                )
                .willRespondWith()
                .status(HttpStatus.ACCEPTED.value())
                .body(new PactDslJsonBody()
                        .valueFromProviderState("id", "${userId}", "1")
                        .stringValue("title", "Mr.")
                        .stringValue("firstname", "John")
                        .stringValue("lastname", "Doe")
                        .stringValue("gender", "female")
                        .object("address")
                        .valueFromProviderState("id", "${addressId}", "1")
                        .stringValue("street", "16 Bridge st")
                        .stringValue("city", "Sydney")
                        .stringValue("state", "NSW")
                        .integerType("postcode", 2000)
                        .closeObject()
                )
                .given("Invalid create")
                .uponReceiving("An invalid POST request with missing first and last name in json body")
                .path("/api/userdetails")
                .method("POST")
                .headers(headers)
                .body("{" +
                        "    \"title\": \"Mr.\"," +
                        "    \"firstname\": \"John\"," +
                        "    \"gender\": \"female\"," +
                        "    \"address\": {" +
                        "        \"street\": \"16 Bridge st\"," +
                        "        \"city\": \"Sydney\"," +
                        "        \"state\": \"NSW\"," +
                        "        \"postcode\": 2000" +
                        "    }" +
                        "}")
                .willRespondWith()
                .status(HttpStatus.BAD_REQUEST.value())
                .body("{" +
                        "    \"violations\": [" +
                        "        {" +
                        "            \"fieldName\": \"lastName\"," +
                        "            \"message\": \"must not be empty\"" +
                        "        }" +
                        "    ]" +
                        "}")
                .given("Invalid update")
                .uponReceiving("An invalid PUT request with non numeric id")
                .path("/api/userdetails")
                .method("PUT")
                .headers(headers)
                .body("{" +
                        "    \"id\": \"ABC\"," +
                        "    \"title\": \"Mr.\"," +
                        "    \"firstname\": \"John\"," +
                        "    \"lastname\": \"Wicks\"," +
                        "    \"gender\": \"female\"," +
                        "    \"address\": {" +
                        "        \"id\": \"2\"," +
                        "        \"street\": \"16 Bridge st\"," +
                        "        \"city\": \"Sydney\"," +
                        "        \"state\": \"NSW\"," +
                        "        \"postcode\": 2000" +
                        "    }" +
                        "}")
                .willRespondWith()
                .status(HttpStatus.BAD_REQUEST.value())
                .body("{" +
                        "    \"violations\": [" +
                        "        {" +
                        "            \"fieldName\": \"id\"," +
                        "            \"message\": \"Invalid id provided. Must be a positive number.\"" +
                        "        }" +
                        "    ]" +
                        "}")
                .toPact();
    }

    @Test
    @PactTestFor
    void generate_pact_test(MockServer mockServer) {

        // when
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.getForEntity(mockServer.getUrl() + "/api/userdetails/1", String.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getHeaders().get("Content-Type").contains("application/json")).isTrue();
        assertThat(response.getBody()).contains("id", "1");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<String> notFoundResponse = testRestTemplate.getForEntity(mockServer.getUrl() + "/api/userdetails/2", String.class);

        // then
        assertThat(notFoundResponse.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());

        // and

        String postJsonBody = "{" +
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

        // when
        ResponseEntity<String> postResponse = testRestTemplate.exchange(mockServer.getUrl() + "/api/userdetails", HttpMethod.POST, new HttpEntity<>(postJsonBody, httpHeaders), String.class);

        // then
        assertThat(postResponse.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());

        // when
        String putJsonBody = "{" +
                "    \"id\": \"1\"," +
                "    \"title\": \"Mr.\"," +
                "    \"firstname\": \"John\"," +
                "    \"lastname\": \"Doe\"," +
                "    \"gender\": \"female\"," +
                "    \"address\": {" +
                "        \"id\": \"2\"," +
                "        \"street\": \"16 Bridge st\"," +
                "        \"city\": \"Sydney\"," +
                "        \"state\": \"NSW\"," +
                "        \"postcode\": 2000" +
                "    }" +
                "}";
        ResponseEntity<String> putResponse = testRestTemplate.exchange(mockServer.getUrl() + "/api/userdetails", HttpMethod.PUT, new HttpEntity<>(putJsonBody, httpHeaders), String.class);

        // then
        assertThat(putResponse.getStatusCode().value()).isEqualTo(HttpStatus.ACCEPTED.value());

        String invalidPostJsonBody = "{" +
                "    \"title\": \"Mr.\"," +
                "    \"firstname\": \"John\"," +
                "    \"gender\": \"female\"," +
                "    \"address\": {" +
                "        \"street\": \"16 Bridge st\"," +
                "        \"city\": \"Sydney\"," +
                "        \"state\": \"NSW\"," +
                "        \"postcode\": 2000" +
                "    }" +
                "}";
        ResponseEntity<String> invalidPostResponse = testRestTemplate.exchange(mockServer.getUrl() + "/api/userdetails", HttpMethod.POST, new HttpEntity<>(invalidPostJsonBody, httpHeaders), String.class);

        // then
        assertThat(invalidPostResponse.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        String invalidPutJsonBody = "{" +
                "    \"id\": \"ABC\"," +
                "    \"title\": \"Mr.\"," +
                "    \"firstname\": \"John\"," +
                "    \"lastname\": \"Wicks\"," +
                "    \"gender\": \"female\"," +
                "    \"address\": {" +
                "        \"id\": \"2\"," +
                "        \"street\": \"16 Bridge st\"," +
                "        \"city\": \"Sydney\"," +
                "        \"state\": \"NSW\"," +
                "        \"postcode\": 2000" +
                "    }" +
                "}";
        ResponseEntity<String> invalidPutResponse = testRestTemplate.exchange(mockServer.getUrl() + "/api/userdetails", HttpMethod.PUT, new HttpEntity<>(invalidPutJsonBody, httpHeaders), String.class);

        // then
        assertThat(invalidPutResponse.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
