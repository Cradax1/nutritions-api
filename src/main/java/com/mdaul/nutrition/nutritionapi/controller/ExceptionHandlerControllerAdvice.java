package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DatabaseEntryAlreadyExistingException.class)
    protected ResponseEntity<String> handleDatabaseEntryAlreadyExistingException(HttpServletResponse response,
                                                                         DatabaseEntryAlreadyExistingException ex) throws IOException {
        log.debug(ex.getMessage());
        return new ResponseEntity<>("The item already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FoodWithNameAlreadyExistsException.class)
    protected ResponseEntity<String> handleFoodWithNameAlreadyExistsException(HttpServletResponse response,
                                                                              FoodWithNameAlreadyExistsException ex) throws IOException {
        log.debug(ex.getMessage());
        return new ResponseEntity<>("An item with this name is already catalogued.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExternalFoodAlreadyMappedToUserException.class)
    protected ResponseEntity<String> handleExternalFoodAlreadyMappedToUserException(HttpServletResponse response,
                                                                                    ExternalFoodAlreadyMappedToUserException ex) throws IOException {
        log.debug(ex.getMessage());
        return new ResponseEntity<>("An item with the given barcode is already catalogued.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FoodInformationProviderStatusException.class)
    protected ResponseEntity<String> handleFoodInformationProviderStatusException(HttpServletResponse response,
                                                                FoodInformationProviderStatusException ex) throws IOException {
        log.error(ex.getMessage());
        return new ResponseEntity<>("External food information provider sent wrong status code", HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(FoodInformationProviderNotReachableException.class)
    protected ResponseEntity<String> handleFoodInformationProviderNotReachableException(HttpServletResponse response,
                                                                      FoodInformationProviderNotReachableException ex) throws IOException {
        log.error(ex.getMessage());
        return new ResponseEntity<>("External food information provider not reachable", HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(FoodInformationProviderItemException.class)
    protected ResponseEntity<Void> handleFoodInformationProviderItemException(HttpServletResponse response,
                                                              FoodInformationProviderItemException ex) throws IOException {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FoodInformationProviderClientException.class)
    protected ResponseEntity<Void> handleFoodInformationProviderClientException(HttpServletResponse response,
                                                                FoodInformationProviderClientException ex) throws IOException {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityException.class)
    protected ResponseEntity<Void> handleDataIntegrityException(HttpServletResponse response,
                                                DataIntegrityException ex) throws IOException {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> handleException(HttpServletResponse response, Exception ex) throws Exception {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
