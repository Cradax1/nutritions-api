package com.mdaul.nutrition.nutritionapi;

import lombok.Getter;

import java.time.LocalDate;

public class TestEndpointProperties {

    // ---------- external provider ---------- //
    private static final String getExternalProviderFoodByBarcode = "/food/%s";
    // ---------- catalogue - food ---------- //
    private static final String getCatalogueExternalFoodByBarcode = "/catalogue/food/external/%s";
    @Getter
    private static final String createCatalogueExternalFood = "/catalogue/food/external";
    private static final String getCatalogueFoodByName = "/catalogue/food/%s";
    private static final String deleteCatalogueFoodByName = "/catalogue/food/%s";
    @Getter
    private static final String getCatalogueFoodAll = "/catalogue/food";
    @Getter
    private static final String createCatalogueFood = "/catalogue/food";
    @Getter
    private static final String updateCatalogueFood = "/catalogue/food";
    private static final String searchCatalogueFoodByName = "/catalogue/food/search/%s";
    // ---------- catalogue - meal ---------- //
    private static final String getCatalogueMealByName = "/catalogue/meal/%s";
    private static final String deleteCatalogueMealByName = "/catalogue/meal/%s";
    @Getter
    private static final String getCatalogueMealAll = "/catalogue/meal";
    @Getter
    private static final String createCatalogueMeal = "/catalogue/meal";
    @Getter
    private static final String updateCatalogueMeal = "/catalogue/meal";
    private static final String searchCatalogueMealByName = "/catalogue/meal/search/%s";
    // ---------- diary ---------- //
    private static final String getDiaryByDay = "/diary/%s";
    // ---------- diary - food ---------- //
    private static final String createDiaryFoodByDay = "/diary/food/%s";
    private static final String updateDiaryFoodByDay = "/diary/food/%s";
    private static final String deleteDiaryFoodByDay = "/diary/food/%s";
    // ---------- diary - meal ---------- //
    private static final String createDiaryMealByDay = "/diary/meal/%s";
    private static final String updateDiaryMealByDay = "/diary/meal/%s";
    private static final String deleteDiaryMealByDay = "/diary/meal/%s";

    // ---------- external provider ---------- //
    public static String getGetExternalProviderFoodByBarcode(String barcode) {
        return String.format(getExternalProviderFoodByBarcode, barcode);
    }

    // ---------- catalogue - food ---------- //
    public static String getGetCatalogueExternalFoodByBarcode(String barcode) {
        return String.format(getCatalogueExternalFoodByBarcode, barcode);
    }

    public static String getGetCatalogueFoodByName(String name) {
        return String.format(getCatalogueFoodByName, name);
    }

    public static String getDeleteCatalogueFoodByName(String name) {
        return String.format(deleteCatalogueFoodByName, name);
    }

    public static String getSearchCatalogueFoodByName(String name) {
        return String.format(searchCatalogueFoodByName, name);
    }

    // ---------- catalogue - meal ---------- //
    public static String getGetCatalogueMealByName(String name) {
        return String.format(getCatalogueMealByName, name);
    }

    public static String getDeleteCatalogueMealByName(String name) {
        return String.format(deleteCatalogueMealByName, name);
    }

    public static String getSearchCatalogueMealByName(String name) {
        return String.format(searchCatalogueMealByName, name);
    }

    // ---------- diary ---------- //
    public static String getGetDiaryByDay(LocalDate day) {
        return String.format(getDiaryByDay, day.toString());
    }

    // ---------- diary - food ---------- //
    public static String getCreateDiaryFoodByDay(LocalDate day) {
        return String.format(createDiaryFoodByDay, day.toString());
    }

    public static String getUpdateDiaryFoodByDay(LocalDate day) {
        return String.format(updateDiaryFoodByDay, day.toString());
    }

    public static String getDeleteDiaryFoodByDay(LocalDate day) {
        return String.format(deleteDiaryFoodByDay, day.toString());
    }

    // ---------- diary - meal ---------- //
    public static String getCreateDiaryMealByDay(LocalDate day) {
        return String.format(createDiaryMealByDay, day.toString());
    }

    public static String getUpdateDiaryMealByDay(LocalDate day) {
        return String.format(updateDiaryMealByDay, day.toString());
    }

    public static String getDeleteDiaryMealByDay(LocalDate day) {
        return String.format(deleteDiaryMealByDay, day.toString());
    }
}
