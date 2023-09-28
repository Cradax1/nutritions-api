package com.mdaul.nutrition.nutritionapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdaul.nutrition.nutritionapi.configuration.FoodInformationProviderConfiguration;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderClientException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderItemException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderNotReachableException;
import com.mdaul.nutrition.nutritionapi.exception.FoodInformationProviderStatusException;
import com.mdaul.nutrition.nutritionapi.model.foodinformationprovider.InformationProviderResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class FoodInformationProviderClient {

    //available status codes documented by provider (but 200 and 301)
    private static final Set<Integer> SERVER_DOWN_STATUS_CODES = Set.of(500, 502, 503);
    private final FoodInformationProviderConfiguration providerConfiguration;
    private final ObjectMapper mapper = new ObjectMapper();
    @Getter
    @Setter
    private int socketTimeout = 60000;

    public FoodInformationProviderClient(FoodInformationProviderConfiguration providerConfiguration) {
        this.providerConfiguration = providerConfiguration;
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(socketTimeout)
                        .setConnectTimeout(60000)
                        .build())
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }

    public Optional<InformationProviderResult> getFoodInformation(long barcode) {
        String url = providerConfiguration.getNutritionFactsUrl(barcode);
        log.info("---> Get FoodInformation of barcode {} from {}", barcode, url);
        try (CloseableHttpClient httpClient = getHttpClient()) {
            CloseableHttpResponse httpResponse = callProvider(httpClient, new HttpGet(url));
            log.info("<--- Successfully retrieved FoodInformation of barcode {} from {}", barcode, url);
            validateStatus(httpResponse);
            log.debug("Information provider status validation was successful");
            Optional<InformationProviderResult> informationProviderResult =
                    buildInformationProviderResult(httpResponse);
            log.debug("Successfully built InformationProviderResult");
            return informationProviderResult;
        } catch (IOException ex) {
            throw new FoodInformationProviderClientException(ex.getMessage(), ex.getCause());
        }
    }

    private CloseableHttpResponse callProvider(CloseableHttpClient httpClient, HttpRequestBase requestBase) throws ClientProtocolException {
        try {
            return httpClient.execute(requestBase);
        } catch (ClientProtocolException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new FoodInformationProviderNotReachableException(ex);
        }
    }

    private void validateStatus(CloseableHttpResponse response) {
        int statusCode = extractStatusCode(response);
        if (SERVER_DOWN_STATUS_CODES.contains(statusCode)) {
            String message = String.format("Information provider status indicates that servers are down. Status code:" +
                    " %s", statusCode);
            throw new FoodInformationProviderStatusException(message);
        }
        if (statusCode != HttpStatus.SC_OK) {
            String message = String.format("Unknown status failure. Status code: %s", statusCode);
            throw new FoodInformationProviderStatusException(message);
        }
    }

    private Optional<InformationProviderResult> buildInformationProviderResult(CloseableHttpResponse httpResponse) {
        try {
            String responseBody = extractResponseBody(httpResponse);
            log.debug("Information provider response body: {}", responseBody);
            InformationProviderResult informationProviderResult = mapper.readValue(responseBody,
                    InformationProviderResult.class);
            if (informationProviderResult.getStatus() == InformationProviderResult.STATUS_ITEM_NOT_FOUND) {
                return Optional.empty();
            }
            if (informationProviderResult.getStatus() == InformationProviderResult.STATUS_ITEM_FOUND) {
                return Optional.of(informationProviderResult);
            }
            String message = String.format("The status %d of the item (not http status) was not expected",
                    informationProviderResult.getStatus());
            throw new FoodInformationProviderItemException(message);
        } catch (IOException ex) {
            throw new FoodInformationProviderClientException(ex.getMessage(), ex.getCause());
        }
    }

    private String extractResponseBody(CloseableHttpResponse httpResponse) throws IOException {
        return new String(httpResponse.getEntity()
                .getContent()
                .readAllBytes());
    }

    private int extractStatusCode(CloseableHttpResponse httpResponse) {
        return httpResponse.getStatusLine()
                .getStatusCode();
    }
}
