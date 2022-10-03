package com.mdaul.nutrition.nutritionapi;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.mockserver.client.MockServerClient;
import org.mockserver.mock.Expectation;
import org.mockserver.model.HttpError;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NutritionApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class TestBase {
    private static final String DEFAULT_PASSWORD = "secretpassword";
    private static final String KEYCLOAK_REALM = "nutritionApi";
    private static final String KEYCLOAK_CLIENT = "nutrition_client";
    private static final String KEYCLOAK_USER_ROLE_NONE_NAME = "user_role_none";
    private static final String KEYCLOAK_USER_ROLE_USER_1_NAME = "user_role_user1";
    private static final String DB_NAME = "nutritiontest";
    private static final String DB_USER = "testuser";
    private static final int DB_PORT = 5432;
    private static final String mockedEndpointNameGetFoodByBarcode = "getFoodByBarcode";
    private static final String foodInformationProviderResponsePath = "foodInformationProvider/responseMocks";

    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:19.0.0")
            .withRealmImportFile("/keycloak/realm-import.json")
            .withAdminUsername("myKeycloakAdminUser")
            .withAdminPassword(DEFAULT_PASSWORD);

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER)
            .withPassword(DEFAULT_PASSWORD)
            .withExposedPorts(DB_PORT);

    static MockServerContainer foodInformationProviderMock =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.14.0"));

    static {
        keycloak.start();
        postgres.start();
        foodInformationProviderMock.start();
    }

    private final TestUtils testUtils = new TestUtils();

    public Keycloak keycloakClientUserRoleUser = KeycloakBuilder.builder()
            .serverUrl(keycloak.getAuthServerUrl())
            .realm(KEYCLOAK_REALM)
            .clientId(KEYCLOAK_CLIENT)
            .clientSecret(DEFAULT_PASSWORD)
            .username(KEYCLOAK_USER_ROLE_USER_1_NAME)
            .password(DEFAULT_PASSWORD)
            .build();

    public Keycloak keycloakClientUserNoRole = KeycloakBuilder.builder()
            .serverUrl(keycloak.getAuthServerUrl())
            .realm(KEYCLOAK_REALM)
            .clientId(KEYCLOAK_CLIENT)
            .clientSecret(DEFAULT_PASSWORD)
            .username(KEYCLOAK_USER_ROLE_NONE_NAME)
            .password(DEFAULT_PASSWORD)
            .build();

    public Expectation[] item = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/4337256112260"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/4337256112260.json")));

    public Expectation[] itemNotFound = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/73223"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/73223_notFound.json")));

    public Expectation[] noBarcode = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/0"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/0_noBarcode.json")));

    public Expectation[] nullBarcode = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/1"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/1_nullBarcode.json")));

    public Expectation[] barcodeAttributeMissing = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/2"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/2_barcodeAttributeMissing.json")));

    public Expectation[] invalidStatusNegativeNumber = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/63433"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/63433_invalidStatusNegativeNumber.json")));

    public Expectation[] invalidStatusPositiveNumber = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/63444"))
            .respond(response()
                    .withBody(testUtils.getResource(foodInformationProviderResponsePath + "/63444_invalidStatusPositiveNumber.json")));

    public Expectation[] statusCode301 = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/301"))
            .respond(response().withStatusCode(301)
                    .withHeader("Location", "http://" + foodInformationProviderMock.getHost() + ":"
                            + foodInformationProviderMock.getServerPort() + "/" + mockedEndpointNameGetFoodByBarcode
                            + "/4337256112260"));

    public Expectation[] statusCode301WithoutRedirectHeader = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/30100"))
            .respond(response().withStatusCode(301));

    public Expectation[] statusCode500 = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/500"))
            .respond(response().withStatusCode(500));

    public Expectation[] statusCode502 = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/502"))
            .respond(response().withStatusCode(502));

    public Expectation[] statusCode503 = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/503"))
            .respond(response().withStatusCode(503));

    public Expectation[] dropConnection = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/3"))
            .error(HttpError.error().withDropConnection(true));

    public Expectation[] connectionTimeout = new MockServerClient(
            foodInformationProviderMock.getHost(), foodInformationProviderMock.getServerPort())
            .when(request()
                    .withPath("/" + mockedEndpointNameGetFoodByBarcode + "/4"))
            .respond(response()
                    .withBody("this_body_should_not_be_relevant")
                    .withDelay(TimeUnit.SECONDS, 70));

    protected TestBase() throws IOException {
    }

    @DynamicPropertySource
    static void setPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s", postgres.getFirstMappedPort(), postgres.getDatabaseName()));
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @DynamicPropertySource
    static void setKeycloakProperties(DynamicPropertyRegistry registry) {
        registry.add("keycloak.auth-server-url", () -> keycloak.getAuthServerUrl());
        registry.add("keycloak.realm", () -> KEYCLOAK_REALM);
        registry.add("keycloak.resource", () -> KEYCLOAK_CLIENT);
    }

    @DynamicPropertySource
    static void setFoodInformationProviderProperties(DynamicPropertyRegistry registry) {
        registry.add("food-information-provider.base-url",
                () -> "http://" + foodInformationProviderMock.getHost() + ":" + foodInformationProviderMock.getServerPort());
        registry.add("food-information-provider.endpoints.nutrition-facts", () -> mockedEndpointNameGetFoodByBarcode);
    }
}
