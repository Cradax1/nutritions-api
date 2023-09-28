package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import({TestRepositoryUtils.class, CatalogueApiControllerTest.class})
class DiaryApiControllerTest extends TestBase {

    private static final LocalDate DUMMY_LOCAL_DATE = LocalDate.of(1783, 6, 15);
    private static final String DUMMY_STRING = "a09sd8fu79as0f8ß0fd0ß9asf";
    private final TestUtils testUtils = new TestUtils();
    private final TestRequestObjectConfiguration requestObjects = new TestRequestObjectConfiguration();
    @Autowired
    private TestRepositoryUtils testRepositoryUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CatalogueApiControllerTest catalogueApiControllerTest;
    @Autowired
    private DiaryApiController diaryApiController;

    protected DiaryApiControllerTest() throws IOException {
    }

    @AfterEach
    void afterEach() {
        testRepositoryUtils.clearAllDatabaseRows();
    }

    @Test
    void diaryDayGetTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        LocalDate dayDiaryFood = requestObjects.getDiaryEntryFood().getAssignedDay();
        String inputDiaryFood = requestObjects.getDiaryFoodEntrySubmissionAsString();
        String resultDiaryFoodEntry = requestObjects.getDiaryEntryFoodAsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        LocalDate dayDiaryMeal= requestObjects.getDiaryEntryMeal().getAssignedDay();
        String inputDiaryMeal = requestObjects.getDiaryMealEntrySubmissionAsString();
        LocalDate resultDiaryDay = requestObjects.getDiaryEntryFoodMeal().getAssignedDay();
        String resultDiaryEntry = requestObjects.getDiaryEntryFoodMealAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueApiControllerTest.catalogueMealPostTest_return201(inputMeal, resultMeal);
        diaryFoodDayPostTest_return201(dayDiaryFood, inputDiaryFood, resultDiaryFoodEntry);
        diaryMealDayPostTest_return201(dayDiaryMeal, inputDiaryMeal, resultDiaryEntry);

        mockMvc.perform(MockMvcRequestBuilders.get(TestEndpointProperties.getGetDiaryByDay(resultDiaryDay)))
                .andExpect(status().isOk())
                .andExpect(content().json(resultDiaryEntry));
    }

    @Test
    void diaryDayGetTest_return200_emptyDay() throws Exception {
        String diaryDayEmpty = requestObjects.getDiaryEntryEmptyAsString(DUMMY_LOCAL_DATE);

        mockMvc.perform(get(TestEndpointProperties.getGetDiaryByDay(DUMMY_LOCAL_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().json(diaryDayEmpty));
    }

    @Test
    void diaryFoodDayPostTest_return201() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        LocalDate dayDiaryFood = requestObjects.getDiaryEntryFood().getAssignedDay();
        String inputDiaryFood = requestObjects.getDiaryFoodEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryFoodAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        diaryFoodDayPostTest_return201(dayDiaryFood, inputDiaryFood, resultDiaryEntry);
    }

    private void diaryFoodDayPostTest_return201(LocalDate day, String input, String result) throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(day))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().json(result));
    }

    @Test
    void diaryFoodDayPostTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPostTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionMissingAttributeGramAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPostTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionNullAttributeGramAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPostTest_return404() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void diaryFoodDayPostTest_return409() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        LocalDate dayDiaryFood = requestObjects.getDiaryEntryFood().getAssignedDay();
        String inputDiaryFood = requestObjects.getDiaryFoodEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryFoodAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        diaryFoodDayPostTest_return201(dayDiaryFood, inputDiaryFood, resultDiaryEntry);

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(dayDiaryFood))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryFood))
                .andExpect(status().isConflict());
    }

    @Test
    void diaryFoodDayPostTest_return415() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void diaryFoodDayPutTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        LocalDate dayDiaryFood = requestObjects.getDiaryEntryFood().getAssignedDay();
        String inputDiaryFood = requestObjects.getDiaryFoodEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryFoodAsString();
        String inputDiaryFoodUpdated = requestObjects.getDiaryFoodEntrySubmissionUpdatedAsString();
        String resultDiaryFoodUpdated = requestObjects.getDiaryEntryFoodUpdatedAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        diaryFoodDayPostTest_return201(dayDiaryFood, inputDiaryFood, resultDiaryEntry);

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(dayDiaryFood))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryFoodUpdated))
                .andExpect(status().isOk())
                .andExpect(content().json(resultDiaryFoodUpdated));
    }

    @Test
    void diaryFoodDayPutTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPutTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionMissingAttributeGramAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPutTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionNullAttributeGramAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayPutTest_return404() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void diaryFoodDayPutTest_return415() throws Exception {
        String input = requestObjects.getDiaryFoodEntrySubmissionAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void diaryFoodDayDeleteTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        LocalDate dayDiaryFood = requestObjects.getDiaryEntryFood().getAssignedDay();
        String inputDiaryFood = requestObjects.getDiaryFoodEntrySubmissionAsString();
        String resultDiaryEntryFood = requestObjects.getDiaryEntryFoodAsString();
        String inputDiaryFoodDelete = requestObjects.getDiaryFoodEntryDeleteSubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryEmptyAsString(dayDiaryFood);

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        diaryFoodDayPostTest_return201(dayDiaryFood, inputDiaryFood, resultDiaryEntryFood);

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryFoodByDay(dayDiaryFood))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryFoodDelete))
                .andExpect(status().isOk())
                .andExpect(content().json(resultDiaryEntry));
    }

    @Test
    void diaryFoodDayDeleteTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayDeleteTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntryDeleteSubmissionMissingAttributeNameAsString();

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayDeleteTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryFoodEntryDeleteSubmissionNullAttributeNameAsString();

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryFoodDayDeleteTest_return404() throws Exception {
        String input = requestObjects.getDiaryFoodEntryDeleteSubmissionAsString();
        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryFoodByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void diaryMealDayPostTest_return201() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        LocalDate dayDiaryMeal= requestObjects.getDiaryEntryMeal().getAssignedDay();
        String inputDiaryMeal = requestObjects.getDiaryMealEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryMealAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueApiControllerTest.catalogueMealPostTest_return201(inputMeal, resultMeal);
        diaryMealDayPostTest_return201(dayDiaryMeal, inputDiaryMeal, resultDiaryEntry);
    }

    void diaryMealDayPostTest_return201(LocalDate day, String input, String result) throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(day))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().json(result));
    }

    @Test
    void diaryMealDayPostTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPostTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionMissingAttributePortionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPostTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionNullAttributePortionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPostTest_return404() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void diaryMealDayPostTest_return409() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        LocalDate dayDiaryMeal= requestObjects.getDiaryEntryMeal().getAssignedDay();
        String inputDiaryMeal = requestObjects.getDiaryMealEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryMealAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueApiControllerTest.catalogueMealPostTest_return201(inputMeal, resultMeal);
        diaryMealDayPostTest_return201(dayDiaryMeal, inputDiaryMeal, resultDiaryEntry);

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(dayDiaryMeal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryMeal))
                .andExpect(status().isConflict());
    }

    @Test
    void diaryMealDayPostTest_return415() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionAsString();

        mockMvc.perform(post(TestEndpointProperties.getCreateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void diaryMealDayPutTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        LocalDate dayDiaryMeal= requestObjects.getDiaryEntryMeal().getAssignedDay();
        String inputDiaryMeal = requestObjects.getDiaryMealEntrySubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryMealAsString();
        String inputDiaryMealUpdated = requestObjects.getDiaryMealEntrySubmissionUpdatedAsString();
        String resultDiaryMealUpdated = requestObjects.getDiaryEntryMealUpdatedAsString();

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueApiControllerTest.catalogueMealPostTest_return201(inputMeal, resultMeal);
        diaryMealDayPostTest_return201(dayDiaryMeal, inputDiaryMeal, resultDiaryEntry);

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(dayDiaryMeal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryMealUpdated))
                .andExpect(status().isOk())
                .andExpect(content().json(resultDiaryMealUpdated));
    }

    @Test
    void diaryMealDayPutTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPutTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionMissingAttributePortionAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPutTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionNullAttributePortionAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayPutTest_return404() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionUpdatedAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isNotFound());
    }

    @Test
    void diaryMealDayPutTest_return415() throws Exception {
        String input = requestObjects.getDiaryMealEntrySubmissionUpdatedAsString();

        mockMvc.perform(put(TestEndpointProperties.getUpdateDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_XML)
                        .content(input))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void diaryMealDayDeleteTest_return200() throws Exception {
        String inputAndResultFood = requestObjects.getFoodAsString();
        String inputAndResultFood2 = requestObjects.getFood2AsString();
        String inputMeal = requestObjects.getMealSubmissionAsString();
        String resultMeal = requestObjects.getMealAsString();
        LocalDate dayDiaryMeal= requestObjects.getDiaryEntryMeal().getAssignedDay();
        String inputDiaryMeal = requestObjects.getDiaryMealEntrySubmissionAsString();
        String resultDiaryEntryMeal = requestObjects.getDiaryEntryMealAsString();
        String inputDiaryMealDelete = requestObjects.getDiaryMealEntryDeleteSubmissionAsString();
        String resultDiaryEntry = requestObjects.getDiaryEntryEmptyAsString(dayDiaryMeal);

        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood, inputAndResultFood);
        catalogueApiControllerTest.catalogueFoodPostTest_return201(inputAndResultFood2, inputAndResultFood2);
        catalogueApiControllerTest.catalogueMealPostTest_return201(inputMeal, resultMeal);
        diaryMealDayPostTest_return201(dayDiaryMeal, inputDiaryMeal, resultDiaryEntryMeal);

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryMealByDay(dayDiaryMeal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryMealDelete))
                .andExpect(status().isOk())
                .andExpect(content().json(resultDiaryEntry));
    }

    @Test
    void diaryMealDayDeleteTest_return400_wrongFormattedContent() throws Exception {
        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DUMMY_STRING))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayDeleteTest_return400_missingRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntryDeleteSubmissionMissingAttributeNameAsString();

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayDeleteTest_return400_nullRequiredAttribute() throws Exception {
        String input = requestObjects.getDiaryMealEntryDeleteSubmissionNullAttributeNameAsString();

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diaryMealDayDeleteTest_return404() throws Exception {
        String inputDiaryMealDelete = requestObjects.getDiaryMealEntryDeleteSubmissionAsString();

        mockMvc.perform(delete(TestEndpointProperties.getDeleteDiaryMealByDay(DUMMY_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputDiaryMealDelete))
                .andExpect(status().isNotFound());
    }
}
