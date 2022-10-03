package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestRepositoryUtils.class)
class FoodApiControllerTest extends TestBase {
    private static final long DUMMY_LONG = 3829473298432L;
    private static final String DUMMY_STRING = "a09sd8fu79as0f8ß0fd0ß9asf";
    private final TestUtils testUtils = new TestUtils();
    private final TestRequestObjectConfiguration requestObjects = new TestRequestObjectConfiguration();
    @Autowired
    private TestRepositoryUtils testRepositoryUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FoodApiController foodApiController;

    protected FoodApiControllerTest() throws IOException {
    }

    @Test
    void foodBarcodeGetTest_return200() throws Exception {
        String result = requestObjects.getFoodExternal4337256112260AsString();
        mockMvc.perform(get(TestEndpointProperties.getGetExternalProviderFoodByBarcode("4337256112260"))
                        .headers(testUtils.getTokenizedHeader(
                                keycloakClientUserRoleUser.tokenManager().getAccessTokenString())))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void catalogueFoodNameGetTest_return400() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetExternalProviderFoodByBarcode(DUMMY_STRING))
                        .headers(testUtils.getTokenizedHeader(
                                keycloakClientUserRoleUser.tokenManager().getAccessTokenString())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodNameGetTest_return401() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetExternalProviderFoodByBarcode(Long.toString(DUMMY_LONG)))
                        .headers(testUtils.getTokenizedHeader(DUMMY_STRING)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void catalogueFoodNameGetTest_return403() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetExternalProviderFoodByBarcode(Long.toString(DUMMY_LONG)))
                        .headers(testUtils.getTokenizedHeader(
                                keycloakClientUserNoRole.tokenManager().getAccessTokenString())))
                .andExpect(status().isForbidden());
    }

    @Test
    void catalogueFoodNameGetTest_return404() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetExternalProviderFoodByBarcode("73223"))
                        .headers(testUtils.getTokenizedHeader(
                                keycloakClientUserRoleUser.tokenManager().getAccessTokenString())))
                .andExpect(status().isNotFound());
    }
}
