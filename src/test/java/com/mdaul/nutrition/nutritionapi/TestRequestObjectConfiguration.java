package com.mdaul.nutrition.nutritionapi;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mdaul.nutrition.nutritionapi.api.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestRequestObjectConfiguration {

    private static final String folder = "requestObjects";
    private static final String subFolderMissingAttributes = "missingAttributes";
    private static final String subFolderNullAttributes = "nullAttributes";

    // ---------- Valid Objects ---------- //
    private static final String food = "food.json";
    private static final String food2 = "food2.json";
    private static final String foodNutrimentsDecimalVariation = "food_nutrimentsDecimalVariation.json";
    private static final String foodUpdated = "foodUpdated.json";
    private static final String foodArrayFoodFood2 = "foodArray_food_food2.json";
    private static final String foodExternal_4337256112260 = "foodExternal_4337256112260.json";
    private static final String externalFoodSubmission_4337256112260 = "externalFoodSubmission_4337256112260.json";
    private static final String externalFoodSubmission_73223_notFound = "externalFoodSubmission_73223_notFound.json";
    private static final String meal = "meal.json";
    private static final String meal2 = "meal2.json";
    private static final String mealUpdated = "mealUpdated.json";
    private static final String mealSubmission = "mealSubmission.json";
    private static final String mealSubmission2 = "mealSubmission2.json";
    private static final String mealSubmissionUpdated = "mealSubmissionUpdated.json";
    private static final String mealArrayMealMeal2 = "mealArray_meal_meal2.json";
    private static final String diaryFoodEntrySubmission = "diaryFoodEntrySubmission.json";
    private static final String diaryFoodEntrySubmission2 = "diaryFoodEntrySubmission2.json";
    private static final String diaryFoodEntrySubmissionUpdated = "diaryFoodEntrySubmissionUpdated.json";
    private static final String diaryFoodEntryDeleteSubmission = "diaryFoodEntryDeleteSubmission.json";
    private static final String diaryMealEntrySubmission = "diaryMealEntrySubmission.json";
    private static final String diaryMealEntrySubmissionUpdated = "diaryMealEntrySubmissionUpdated.json";
    private static final String diaryMealEntryDeleteSubmission = "diaryMealEntryDeleteSubmission.json";
    private static final String diaryEntryEmpty = "diaryEntry_empty.json";
    private static final String diaryEntryFood = "diaryEntry_food.json";
    private static final String diaryEntryFood2 = "diaryEntry_food2.json";
    private static final String diaryEntryFoodUpdated = "diaryEntry_foodUpdated.json";
    private static final String diaryEntryMeal = "diaryEntry_meal.json";
    private static final String diaryEntryMealUpdated = "diaryEntry_mealUpdated.json";
    private static final String diaryEntryFoodMeal = "diaryEntry_food_meal.json";

    // ---------- Invalid Objects ---------- //
    // missing attributes
    private static final String diaryFoodEntrySubmissionMissingAttributeGram =
            "diaryFoodEntrySubmission_missingAttributeGram.json";
    private static final String diaryFoodEntryDeleteSubmissionMissingAttributeName =
            "diaryFoodEntryDeleteSubmission_missingAttributeName.json";
    private static final String diaryMealEntrySubmissionMissingAttributePortion =
            "diaryMealEntrySubmission_missingAttributePortion.json";
    private static final String diaryMealEntryDeleteSubmissionMissingAttributeName =
            "diaryMealEntryDeleteSubmission_missingAttributeName.json";
    private static final String externalFoodSubmission4337256112260MissingAttributeName =
            "externalFoodSubmission_4337256112260_missingAttributeName.json";
    private static final String foodMissingAttributeCarbohydrates = "food_missingAttributeCarbohydrates.json";
    private static final String mealSubmissionMissingAttributeGram = "mealSubmission_missingAttributeGram.json";

    //null attributes
    private static final String diaryFoodEntrySubmissionNullAttributeGram =
            "diaryFoodEntrySubmission_nullAttributeGram.json";
    private static final String diaryFoodEntryDeleteSubmissionNullAttributeName =
            "diaryFoodEntryDeleteSubmission_nullAttributeName.json";
    private static final String diaryMealEntrySubmissionNullAttributePortion =
            "diaryMealEntrySubmission_nullAttributePortion.json";
    private static final String diaryMealEntryDeleteSubmissionNullAttributeName =
            "diaryMealEntryDeleteSubmission_nullAttributeName.json";
    private static final String externalFoodSubmission4337256112260NullAttributeName =
            "externalFoodSubmission_4337256112260_nullAttributeName.json";
    private static final String foodNullAttributeCarbohydrates = "food_nullAttributeCarbohydrates.json";
    private static final String mealSubmissionNullAttributeGram = "mealSubmission_nullAttributeGram.json";

    private final TestUtils testUtils = new TestUtils();
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    // ---------- Valid Objects ---------- //

    public Food getFood() throws IOException {
        return objectMapper.readValue(getFoodAsString(), Food.class);
    }

    public String getFoodAsString() throws IOException {
        return testUtils.getResource(getFilePath(food));
    }

    public Food getFood2() throws IOException {
        return objectMapper.readValue(getFood2AsString(), Food.class);
    }

    public String getFood2AsString() throws IOException {
        return testUtils.getResource(getFilePath(food2));
    }

    public Food getFoodNutrimentsDecimalVariation() throws IOException {
        return objectMapper.readValue(getFoodNutrimentsDecimalVariationAsString(), Food.class);
    }

    public String getFoodNutrimentsDecimalVariationAsString() throws IOException {
        return testUtils.getResource(getFilePath(foodNutrimentsDecimalVariation));
    }

    public Food getFoodUpdated() throws IOException {
        return objectMapper.readValue(getFoodUpdatedAsString(), Food.class);
    }

    public String getFoodUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(foodUpdated));
    }

    public List<Food> getFoodArrayFoodFood2() throws IOException {
        return Arrays.asList(
                objectMapper.readValue(getFoodArrayFoodFood2AsString(), Food[].class));
    }

    public String getFoodArrayFoodFood2AsString() throws IOException {
        return testUtils.getResource(getFilePath(foodArrayFoodFood2));
    }

    public Food getFoodExternal4337256112260() throws IOException {
        return objectMapper.readValue(getFoodExternal4337256112260AsString(), Food.class);
    }

    public String getFoodExternal4337256112260AsString() throws IOException {
        return testUtils.getResource(getFilePath(foodExternal_4337256112260));
    }

    public Meal getMeal() throws IOException {
        return objectMapper.readValue(getMealAsString(), Meal.class);
    }

    public ExternalFoodSubmission getExternalFoodSubmission4337256112260() throws IOException {
        return objectMapper.readValue(getExternalFoodSubmission4337256112260AsString(), ExternalFoodSubmission.class);
    }

    public String getExternalFoodSubmission4337256112260AsString() throws IOException {
        return testUtils.getResource(getFilePath(externalFoodSubmission_4337256112260));
    }

    public ExternalFoodSubmission getExternalFoodSubmission73223NotFound() throws IOException {
        return objectMapper.readValue(getExternalFoodSubmission73223NotFoundAsString(), ExternalFoodSubmission.class);
    }

    public String getExternalFoodSubmission73223NotFoundAsString() throws IOException {
        return testUtils.getResource(getFilePath(externalFoodSubmission_73223_notFound));
    }

    public String getMealAsString() throws IOException {
        return testUtils.getResource(getFilePath(meal));
    }

    public MealSubmission getMealSubmission() throws IOException {
        return objectMapper.readValue(getMealSubmissionAsString(), MealSubmission.class);
    }

    public String getMealSubmissionAsString() throws IOException {
        return testUtils.getResource(getFilePath(mealSubmission));
    }

    public Meal getMeal2() throws IOException {
        return objectMapper.readValue(getMeal2AsString(), Meal.class);
    }

    public String getMeal2AsString() throws IOException {
        return testUtils.getResource(getFilePath(meal2));
    }

    public MealSubmission getMealSubmission2() throws IOException {
        return objectMapper.readValue(getMealSubmission2AsString(), MealSubmission.class);
    }

    public String getMealSubmission2AsString() throws IOException {
        return testUtils.getResource(getFilePath(mealSubmission2));
    }

    public List<Meal> getMealArrayMealMeal2() throws IOException {
        return Arrays.asList(
                objectMapper.readValue(getMealArrayMealMeal2AsString(), Meal[].class));
    }

    public Meal getMealUpdated() throws IOException {
        return objectMapper.readValue(getMealUpdatedAsString(), Meal.class);
    }

    public String getMealUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(mealUpdated));
    }

    public MealSubmission getMealSubmissionUpdated() throws IOException {
        return objectMapper.readValue(getMealSubmissionUpdatedAsString(), MealSubmission.class);
    }

    public String getMealSubmissionUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(mealSubmissionUpdated));
    }

    public List<Meal> getMealArrayMeal2() throws IOException {
        return Arrays.asList(objectMapper.readValue(getMealArrayMealMeal2AsString(), Meal[].class));
    }

    public String getMealArrayMealMeal2AsString() throws IOException {
        return testUtils.getResource(getFilePath(mealArrayMealMeal2));
    }

    public DiaryFoodEntrySubmission getDiaryFoodEntrySubmission() throws IOException {
        return objectMapper.readValue(getDiaryFoodEntrySubmissionAsString(), DiaryFoodEntrySubmission.class);
    }

    public String getDiaryFoodEntrySubmissionAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryFoodEntrySubmission));
    }

    public DiaryFoodEntrySubmission getDiaryFoodEntrySubmission2() throws IOException {
        return objectMapper.readValue(getDiaryFoodEntrySubmission2AsString(), DiaryFoodEntrySubmission.class);
    }

    public String getDiaryFoodEntrySubmission2AsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryFoodEntrySubmission2));
    }

    public DiaryFoodEntrySubmission getDiaryFoodEntrySubmissionUpdated() throws IOException {
        return objectMapper.readValue(getDiaryFoodEntrySubmissionUpdatedAsString(), DiaryFoodEntrySubmission.class);
    }

    public String getDiaryFoodEntrySubmissionUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryFoodEntrySubmissionUpdated));
    }

    public DiaryFoodEntryDeleteSubmission getDiaryFoodEntryDeleteSubmission() throws IOException {
        return objectMapper.readValue(getDiaryFoodEntryDeleteSubmissionAsString(), DiaryFoodEntryDeleteSubmission.class);
    }

    public String getDiaryFoodEntryDeleteSubmissionAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryFoodEntryDeleteSubmission));
    }

    public DiaryMealEntrySubmission getDiaryMealEntrySubmission() throws IOException {
        return objectMapper.readValue(getDiaryMealEntrySubmissionAsString(), DiaryMealEntrySubmission.class);
    }

    public String getDiaryMealEntrySubmissionAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryMealEntrySubmission));
    }

    public DiaryMealEntrySubmission getDiaryMealEntrySubmissionUpdated() throws IOException {
        return objectMapper.readValue(getDiaryMealEntrySubmissionUpdatedAsString(), DiaryMealEntrySubmission.class);
    }

    public String getDiaryMealEntrySubmissionUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryMealEntrySubmissionUpdated));
    }

    public DiaryMealEntryDeleteSubmission getDiaryMealEntryDeleteSubmission() throws IOException {
        return objectMapper.readValue(getDiaryMealEntryDeleteSubmissionAsString(), DiaryMealEntryDeleteSubmission.class);
    }

    public String getDiaryMealEntryDeleteSubmissionAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryMealEntryDeleteSubmission));
    }

    public DiaryEntry getDiaryEntryEmpty(LocalDate day) throws IOException {
        return objectMapper.readValue(getDiaryEntryEmptyAsString(day), DiaryEntry.class);
    }

    public String getDiaryEntryEmptyAsString(LocalDate day) throws IOException {
        return String.format(testUtils.getResource(getFilePath(diaryEntryEmpty)), day.toString());
    }

    public DiaryEntry getDiaryEntryFood() throws IOException {
        return objectMapper.readValue(getDiaryEntryFoodAsString(), DiaryEntry.class);
    }

    public String getDiaryEntryFoodAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryFood));
    }

    public DiaryEntry getDiaryEntryFood2() throws IOException {
        return objectMapper.readValue(getDiaryEntryFood2AsString(), DiaryEntry.class);
    }

    public String getDiaryEntryFood2AsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryFood2));
    }

    public DiaryEntry getDiaryEntryFoodUpdated() throws IOException {
        return objectMapper.readValue(getDiaryEntryFoodUpdatedAsString(), DiaryEntry.class);
    }

    public String getDiaryEntryFoodUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryFoodUpdated));
    }

    public DiaryEntry getDiaryEntryMeal() throws IOException {
        return objectMapper.readValue(getDiaryEntryMealAsString(), DiaryEntry.class);
    }

    public String getDiaryEntryMealAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryMeal));
    }

    public DiaryEntry getDiaryEntryMealUpdated() throws IOException {
        return objectMapper.readValue(getDiaryEntryMealUpdatedAsString(), DiaryEntry.class);
    }

    public String getDiaryEntryMealUpdatedAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryMealUpdated));
    }

    public DiaryEntry getDiaryEntryFoodMeal() throws IOException {
        return objectMapper.readValue(getDiaryEntryFoodMealAsString(), DiaryEntry.class);
    }

    public String getDiaryEntryFoodMealAsString() throws IOException {
        return testUtils.getResource(getFilePath(diaryEntryFoodMeal));
    }

    // ---------- Invalid Objects ---------- //
    // missing attributes
    public String getDiaryFoodEntrySubmissionMissingAttributeGramAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(diaryFoodEntrySubmissionMissingAttributeGram));
    }

    public String getDiaryFoodEntryDeleteSubmissionMissingAttributeNameAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(diaryFoodEntryDeleteSubmissionMissingAttributeName));
    }

    public String getDiaryMealEntrySubmissionMissingAttributePortionAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(diaryMealEntrySubmissionMissingAttributePortion));
    }

    public String getDiaryMealEntryDeleteSubmissionMissingAttributeNameAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(diaryMealEntryDeleteSubmissionMissingAttributeName));
    }

    public String getExternalFoodSubmission4337256112260MissingAttributeNameAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(externalFoodSubmission4337256112260MissingAttributeName));
    }

    public String getFoodMissingAttributeCarbohydratesAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(foodMissingAttributeCarbohydrates));
    }

    public String getMealSubmissionMissingAttributeGramAsString() throws IOException {
        return testUtils.getResource(getMissingAttributesFilePath(mealSubmissionMissingAttributeGram));
    }

    //null attributes
    public String getDiaryFoodEntrySubmissionNullAttributeGramAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(diaryFoodEntrySubmissionNullAttributeGram));
    }
    public String getDiaryFoodEntryDeleteSubmissionNullAttributeNameAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(diaryFoodEntryDeleteSubmissionNullAttributeName));
    }
    public String getDiaryMealEntrySubmissionNullAttributePortionAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(diaryMealEntrySubmissionNullAttributePortion));
    }
    public String getDiaryMealEntryDeleteSubmissionNullAttributeNameAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(diaryMealEntryDeleteSubmissionNullAttributeName));
    }
    public String getExternalFoodSubmission4337256112260NullAttributeNameAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(externalFoodSubmission4337256112260NullAttributeName));
    }
    public String getFoodNullAttributeCarbohydratesAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(foodNullAttributeCarbohydrates));
    }
    public String getMealSubmissionNullAttributeGramAsString() throws IOException {
        return testUtils.getResource(getNullAttributesFilePath(mealSubmissionNullAttributeGram));
    }

    private String getNullAttributesFilePath(String file) {
        return getFilePath(subFolderNullAttributes + "/" + file);
    }

    private String getMissingAttributesFilePath(String file) {
        return getFilePath(subFolderMissingAttributes + "/" + file);
    }

    private String getFilePath(String file) {
        return folder + "/" + file;
    }
}
