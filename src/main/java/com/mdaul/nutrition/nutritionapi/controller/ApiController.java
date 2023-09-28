package com.mdaul.nutrition.nutritionapi.controller;

interface ApiController {

    default String getUserId() {
        return "abc";
    }
}
