package com.neewrobert.shopping.infrastructure.web.controller;

import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.infrastructure.RepositoryTestConfiguration;
import com.neewrobert.shopping.infrastructure.security.adapter.SpringPasswordEncoderAdapter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.neewrobert.shopping.utils.CommonMethods.getAdmin;
import static com.neewrobert.shopping.utils.CommonMethods.getUser;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest extends RepositoryTestConfiguration {

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRE_SQL_CONTAINER.stop();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SpringPasswordEncoderAdapter passwordEncoderAdapter;

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void shouldSignUp() {

        given()
                .contentType("application/json")
                .body("{\"name\":\"Newton Roberto\",\"email\":\"neewrobert@testUser.com\",\"password\":\"123456\"}")
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(201)
                .body("username", equalTo("neewrobert@testUser.com"))
                .body("token", notNullValue());
    }

    @Test
    void shouldNotSignUp_409_emailAlreadyExists() {

        var rawPassword = "123456";
        var user = getUser().withPassword(passwordEncoderAdapter.encode(rawPassword));
        userRepository.save(user);
        var request = "{\"name\":\"" + user.name() + "\",\"email\":\"" + user.email() + "\",\"password\":\"" + rawPassword + "\"}";

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(409)
                .body("message", equalTo("Email already exists:" + user.email()));
    }

    @Test
    void shouldNotSignUp_400_invalidEmail() {

        var request = "{\"name\":\"Newton Roberto\",\"email\":\"neewrobert\",\"password\":\"123456\"}";

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(400)
                .body("message", equalTo("Validation failed"))
                .body("errors.email", equalTo("Email should be valid"));
    }

    @Test
    void shouldNotSignUp_400_blankData() {

        var request = "{}";

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(400)
                .body("message", equalTo("Validation failed"))
                .body("errors.email", equalTo("Email must not be blank"))
                .body("errors.password", equalTo("Password must not be blank"))
                .body("errors.name", equalTo("Name is required"));

    }

    @Test
    void shouldSignIn() {

        var rawPassword = "123456";
        var user = getUser().withPassword(passwordEncoderAdapter.encode(rawPassword));
        userRepository.save(user);

        var request = "{\"email\":\"" + user.email() + "\",\"password\":\"" + rawPassword + "\"}";
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signing")
                .then()
                .statusCode(200)
                .body("username", equalTo(user.email()))
                .body("token", notNullValue());

    }

    @Test
    void shouldAccessUserEndpoint() {

        var rawPassword = "123456";
        var user = getUser().withPassword(passwordEncoderAdapter.encode(rawPassword));
        userRepository.save(user);

        var request = "{\"email\":\"" + user.email() + "\",\"password\":\"" + rawPassword + "\"}";

        String token = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signing")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        given().
                header("Authorization", "Bearer " + token).
                when()
                .get("/api/user")
                .then()
                .statusCode(200)
                .body(equalTo("Hello USER"));
    }

    @Test
    void shouldNotAccessUserEndpointWhenNotAuthenticated_403() {

        given().
                when()
                .get("/api/user")
                .then()
                .statusCode(403);
    }

//    @Test
//    void shouldAccessAdminEndpoint() {
//
//        var rawPassword = "123456";
//        var userAdmin = getAdmin().withPassword(passwordEncoderAdapter.encode(rawPassword));
//        userRepository.save(userAdmin);
//
//        var request = "{\"email\":\"" + userAdmin.email() + "\",\"password\":\"" + rawPassword + "\"}";
//
//        String token = given()
//                .contentType("application/json")
//                .body(request)
//                .when()
//                .post("/api/auth/signing")
//                .then()
//                .statusCode(200)
//                .extract().jsonPath().getString("token");
//
//        given().
//                header("Authorization", "Bearer " + token).
//                when()
//                .get("/api/admin")
//                .then()
//                .statusCode(200)
//                .body(equalTo("Hello ADMIN"));
//
//    }

    @Test
    void shouldNotAccessAdminEndpointWhenNotAuthenticated_403() {

        given().
                when()
                .get("/api/admin")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldNotAccessAdminEndpointWithoutAdminCredentials() {

        var rawPassword = "123456";
        var user = getUser().withPassword(passwordEncoderAdapter.encode(rawPassword));
        userRepository.save(user);

        var request = "{\"email\":\"" + user.email() + "\",\"password\":\"" + rawPassword + "\"}";

        String token = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/auth/signing")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        given().
                header("Authorization", "Bearer " + token).
                when()
                .get("/api/admin")
                .then()
                .statusCode(403);
    }


}
