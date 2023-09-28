package com.mdaul.nutrition.nutritionapi.client;

import com.mdaul.nutrition.nutritionapi.TestBase;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderClientException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderItemException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderNotReachableException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderStatusException;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Nutriments;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
The following responses and requests are mocked in the class TestBase.
Every barcode has different mocked responses to test different scenarios.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodInformationProviderClientTest extends TestBase {

    @Autowired
    private FoodInformationProviderClient foodInformationProviderClient;
    private int defaultSocketTimeout;

    private final InformationProviderResult validResult_4337256112260 = InformationProviderResult.builder()
            .code(4337256112260L)
            .status(1)
            .statusVerbose("product found")
            .product(Product.builder()
                    .productName("Beeren Müsli")
                    .brands("Rewe,Rewe Bio")
                    .nutriments(Nutriments.builder()
                            .calories(354)
                            .carbohydrates(new BigDecimal(62))
                            .proteins(new BigDecimal(10))
                            .fat(new BigDecimal(5))
                            .fiber(new BigDecimal("10.6"))
                            .build())
                    .build())
            .build();

    protected FoodInformationProviderClientTest() throws IOException {
    }

    @BeforeAll
    void beforeAll() {
        defaultSocketTimeout = foodInformationProviderClient.getSocketTimeout();
    }

    @BeforeEach
    void beforeEach() {
        foodInformationProviderClient.setSocketTimeout(defaultSocketTimeout);
    }

    @Test
    void getFoodInformation_validResponse() {
        Optional<InformationProviderResult> informationProviderResult =
                foodInformationProviderClient.getFoodInformation(4337256112260L);

        assertThat(informationProviderResult).isNotEmpty();
        assertThat(informationProviderResult.get()).usingRecursiveComparison()
                .isEqualTo(validResult_4337256112260);
    }

    @Test
    void getFoodInformation_invalidResponse_noBarcode() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(0))
                .isInstanceOf(FoodInformationProviderClientException.class);
    }

    @Test
    void getFoodInformation_invalidResponse_nullBarcode() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(1))
                .isInstanceOf(FoodInformationProviderClientException.class);
    }

    @Test
    void getFoodInformation_invalidResponse_barcodeAttributeMissing() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(2))
                .isInstanceOf(FoodInformationProviderClientException.class);
    }

    @Test
    void getFoodInformation_invalidResponse_invalidItemStatusNumberPositive() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(63444))
                .isInstanceOf(FoodInformationProviderItemException.class);
    }

    @Test
    void getFoodInformation_invalidResponse_invalidItemStatusNumberNegative() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(63433))
                .isInstanceOf(FoodInformationProviderItemException.class);
    }

    @Test
    void getFoodInformation_validResponse_afterRedirect() {
        Optional<InformationProviderResult> informationProviderResult =
                foodInformationProviderClient.getFoodInformation(301);

        assertThat(informationProviderResult).isNotEmpty();
        assertThat(informationProviderResult.get()).usingRecursiveComparison()
                .isEqualTo(validResult_4337256112260);
    }

    @Test
    void getFoodInformation_throwException_statusCode301WithoutHeader() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(30100))
                .isInstanceOf(FoodInformationProviderClientException.class);
    }

    @Test
    void getFoodInformation_throwException_statusCode500() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(500))
                .isInstanceOf(FoodInformationProviderStatusException.class);
    }

    @Test
    void getFoodInformation_throwException_statusCode502() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(502))
                .isInstanceOf(FoodInformationProviderStatusException.class);
    }

    @Test
    void getFoodInformation_throwException_statusCode503() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(503))
                .isInstanceOf(FoodInformationProviderStatusException.class);
    }

    @Test
    void getFoodInformation_throwException_dropConnection() {
        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(3))
                .isInstanceOf(FoodInformationProviderNotReachableException.class);
    }

    @Test
    void getFoodInformation_throwException_socketTimeout() {
        foodInformationProviderClient.setSocketTimeout(1);

        assertThatThrownBy(() ->
                foodInformationProviderClient.getFoodInformation(4))
                .isInstanceOf(FoodInformationProviderNotReachableException.class);
    }
}
