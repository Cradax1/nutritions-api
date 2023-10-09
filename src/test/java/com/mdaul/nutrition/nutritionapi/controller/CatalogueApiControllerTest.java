package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@Import(TestRepositoryUtils.class)
class CatalogueApiControllerTest extends TestBase {

    private static final long DUMMY_LONG = 3829473298432L;
    private static final String DUMMY_STRING = "a09sd8fu79as0f8ß0fd0ß9asf";
    private final TestUtils testUtils = new TestUtils();
    private final TestRequestObjectConfiguration requestObjects = new TestRequestObjectConfiguration();
    @Autowired
    private TestRepositoryUtils testRepositoryUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CatalogueApiController catalogueApiController;

    protected CatalogueApiControllerTest() throws IOException {
    }

    @AfterEach
    void afterEach() {
        testRepositoryUtils.clearAllDatabaseRows();
    }

    @Test
    void catalogueFoodExternalPostTest_return201() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260AsString();
        String result = requestObjects.getFoodExternal4337256112260AsString();

        catalogueFoodExternalPostTest_return201(input, result);
    }

    void catalogueFoodExternalPostTest_return201(String input, String result) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().json(result));
    }

    @Test
    void catalogueFoodExternalPostTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodExternalPostTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260MissingAttributeNameAsString();

        mockMvc.perform(MockMvcRequestBuilders.post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodExternalPostTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260NullAttributeNameAsString();

        mockMvc.perform(MockMvcRequestBuilders.post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodExternalPostTest_return404() throws Exception {
        String input = requestObjects.getExternalFoodSubmission73223NotFoundAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueFoodExternalPostTest_return409() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260AsString();
        String result = requestObjects.getFoodExternal4337256112260AsString();

        catalogueFoodExternalPostTest_return201(input, result);

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isConflict());
    }

    @Test
    void catalogueFoodExternalPostTest_return415() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260AsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueExternalFood())
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void catalogueFoodExternalBarcodeGetTest_return200() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260AsString();
        String result = requestObjects.getFoodExternal4337256112260AsString();
        String barcode = requestObjects.getExternalFoodSubmission4337256112260().getBarcode().toString();

        catalogueFoodExternalPostTest_return201(input, result);

        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueExternalFoodByBarcode(barcode)))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void catalogueFoodExternalBarcodeGetTest_return400() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueExternalFoodByBarcode(DUMMY_STRING)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodExternalBarcodeGetTest_return404() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueExternalFoodByBarcode(Long.toString(DUMMY_LONG))))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueFoodPostTest_return201() throws Exception {
        String inputAndResult = requestObjects.getFoodAsString();

        catalogueFoodPostTest_return201(inputAndResult, inputAndResult);
    }

    @Test
    void catalogueFoodPostTest_return201_withNutrimentsDecimalVariation() throws Exception {
        String input = requestObjects.getFoodNutrimentsDecimalVariationAsString();
        String result = requestObjects.getFoodAsString();

        catalogueFoodPostTest_return201(input, result);
    }

    void catalogueFoodPostTest_return201(String input, String result) throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().json(result));
    }

    @Test
    void catalogueFoodPostTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPostTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getFoodMissingAttributeCarbohydratesAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPostTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getFoodNullAttributeCarbohydratesAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPostTest_return409() throws Exception {
        String input = requestObjects.getFoodAsString();
        catalogueFoodPostTest_return201(input, input);
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isConflict());
    }

    @Test
    void catalogueFoodPostTest_return415() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueFood())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void catalogueFoodGetTest_return200() throws Exception {
        String input = requestObjects.getFoodAsString();
        String input2 = requestObjects.getFood2AsString();
        String resultArray = requestObjects.getFoodArrayFoodFood2AsString();

        catalogueFoodPostTest_return201(input, input);
        catalogueFoodPostTest_return201(input2, input2);

        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueFoodAll()))
                .andExpect(status().isOk())
                .andExpect(content().json(resultArray));
    }

    @Test
    void catalogueFoodGetTest_return200_emptyArray() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueFoodAll()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void catalogueFoodNameGetTest_return200() throws Exception {
        String inputAndResult = requestObjects.getFoodAsString();
        String name = requestObjects.getFood().getName();

        catalogueFoodPostTest_return201(inputAndResult, inputAndResult);

        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueFoodByName(name)))
                .andExpect(status().isOk())
                .andExpect(content().json(inputAndResult));
    }

    @Test
    void catalogueFoodNameGetTest_return404() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueFoodByName(DUMMY_STRING)))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueFoodNameDeleteTest_return200() throws Exception {
        String inputAndResult = requestObjects.getFoodAsString();
        String name = requestObjects.getFood().getName();

        catalogueFoodPostTest_return201(inputAndResult, inputAndResult);

        mockMvc.perform(delete(TestEndpointProperties.getDeleteCatalogueFoodByName(name)))
                .andExpect(status().isOk());
    }

    @Test
    void catalogueFoodNameDeleteTest_return404() throws Exception {
        mockMvc.perform(delete(TestEndpointProperties.getDeleteCatalogueFoodByName(DUMMY_STRING)))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueFoodPutTest_return200() throws Exception {
        String inputAndResult = requestObjects.getFoodAsString();
        String inputAndResultUpdated = requestObjects.getFoodUpdatedAsString();

        catalogueFoodPostTest_return201(inputAndResult, inputAndResult);

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputAndResultUpdated))
                .andExpect(status().isOk())
                .andExpect(content().json(inputAndResultUpdated));
    }

    @Test
    void catalogueFoodPutTest_return400_provideExternalFood() throws Exception {
        String input = requestObjects.getExternalFoodSubmission4337256112260AsString();
        String result = requestObjects.getFoodExternal4337256112260AsString();

        catalogueFoodExternalPostTest_return201(input, result);

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPutTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPutTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getFoodMissingAttributeCarbohydratesAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPutTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getFoodNullAttributeCarbohydratesAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueFoodPutTest_return404() throws Exception {
        String input = requestObjects.getFoodAsString();
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueFoodPutTest_return415() throws Exception {
        String input = requestObjects.getFoodAsString();
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueFood())
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void catalogueFoodSearchNameGetTest_return200_matchBothItems() throws Exception {
        String namePart = "se";
        String input = requestObjects.getFoodAsString();
        String input2 = requestObjects.getFood2AsString();
        String result = requestObjects.getFoodArrayFoodFood2AsString();

        catalogueFoodPostTest_return201(input, input);
        catalogueFoodPostTest_return201(input2, input2);

        mockMvc.perform(get(TestEndpointProperties.getSearchCatalogueFoodByName(namePart)))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void catalogueFoodSearchNameGetTest_return200_matchOneItem() throws Exception {
        String namePart = "2";
        String input = requestObjects.getFoodAsString();
        String input2AndResult = requestObjects.getFood2AsString();

        catalogueFoodPostTest_return201(input, input);
        catalogueFoodPostTest_return201(input2AndResult, input2AndResult);

        mockMvc.perform(get(TestEndpointProperties.getSearchCatalogueFoodByName(namePart)))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + input2AndResult + "]"));
    }

    @Test
    void catalogueFoodSearchNameGetTest_return200_matchNoItem() throws Exception {
        String namePart = "x";
        String input = requestObjects.getFoodAsString();
        String input2 = requestObjects.getFood2AsString();

        catalogueFoodPostTest_return201(input, input);
        catalogueFoodPostTest_return201(input2, input2);

        mockMvc.perform(get(TestEndpointProperties.getSearchCatalogueFoodByName(namePart)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void catalogueMealPostTest_return201() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);

        catalogueMealPostTest_return201(inputMeal, resultMeal);
    }

    void catalogueMealPostTest_return201(String inputMeal, String resultMeal) throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputMeal))
                .andExpect(status().isCreated())
                .andExpect(content().json(resultMeal));
    }

    @Test
    void catalogueMealPostTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPostTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getMealSubmissionMissingAttributeGramAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPostTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getMealSubmissionNullAttributeGramAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPostTest_return404() throws Exception {
        String inputMeal = requestObjects.getMealSubmissionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputMeal))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueMealPostTest_return409() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);

        catalogueMealPostTest_return201(inputMeal, resultMeal);

        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputMeal))
                .andExpect(status().isConflict());
    }

    @Test
    void catalogueMealPostTest_return415() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void catalogueMealGetTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        String inputMeal2 = requestObjects.getMealSubmission2AsString();
        String resultMeal2 = requestObjects.getMeal2AsString();
        String resultMealArray = requestObjects.getMealArrayMealMeal2AsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueMealPostTest_return201(inputMeal, resultMeal);
        catalogueMealPostTest_return201(inputMeal2, resultMeal2);

        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueMealAll()))
                .andExpect(status().isOk())
                .andExpect(content().json(resultMealArray));
    }

    @Test
    void catalogueMealGetTest_return200_emptyArray() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueMealAll()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void catalogueMealNameGetTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String mealName = requestObjects.getMealSubmission().getName();
        String resultMeal = requestObjects.getMealAsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueMealPostTest_return201(inputMeal, resultMeal);

        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueMealByName(mealName)))
                .andExpect(status().isOk())
                .andExpect(content().json(resultMeal));
    }

    @Test
    void catalogueMealNameGetTest_return404() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getGetCatalogueMealByName(DUMMY_STRING)))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueMealNameDeleteTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String mealName = requestObjects.getMealSubmission().getName();
        String resultMeal = requestObjects.getMealAsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueMealPostTest_return201(inputMeal, resultMeal);

        mockMvc.perform(delete(TestEndpointProperties.getDeleteCatalogueMealByName(mealName)))
                .andExpect(status().isOk());
    }

    @Test
    void catalogueMealNameDeleteTest_return404() throws Exception {
        mockMvc.perform(delete(TestEndpointProperties.getDeleteCatalogueMealByName(DUMMY_STRING)))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueMealPutTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        String inputUpdatedMeal = requestObjects.getMealSubmissionUpdatedAsString();
        String resultUpdatedMeal = requestObjects.getMealUpdatedAsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueMealPostTest_return201(inputMeal, resultMeal);

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputUpdatedMeal))
                .andExpect(status().isOk())
                .andExpect(content().json(resultUpdatedMeal));
    }

    @Test
    void catalogueMealPutTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPutTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getMealSubmissionMissingAttributeGramAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPutTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getMealSubmissionNullAttributeGramAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void catalogueMealPutTest_return404() throws Exception {
        String input = requestObjects.getMealSubmissionAsString();
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void catalogueMealPutTest_return415() throws Exception {
        String input = requestObjects.getMealSubmissionAsString();
        mockMvc.perform(put(TestEndpointProperties.getUpdateCatalogueMeal())
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void catalogueMealSearchNameGetTest_return200() throws Exception {
        String namePart = "ea";
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        String inputMeal2 = requestObjects.getMealSubmission2AsString();
        String resultMeal2 = requestObjects.getMeal2AsString();
        String resultMealArray = requestObjects.getMealArrayMealMeal2AsString();

        catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueMealPostTest_return201(inputMeal, resultMeal);
        catalogueMealPostTest_return201(inputMeal2, resultMeal2);

        mockMvc.perform(get(TestEndpointProperties.getSearchCatalogueMealByName(namePart)))
                .andExpect(status().isOk())
                .andExpect(content().json(resultMealArray));
    }

    @Test
    void catalogueMealSearchNameGetTest_return200_emptyArray() throws Exception {
        mockMvc.perform(get(TestEndpointProperties.getSearchCatalogueMealByName(DUMMY_STRING)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}