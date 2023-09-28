package com.mdaul.nutrition.nutritionapi.controller;

import com.mdaul.nutrition.nutritionapi.service.CatalogueFoodService;
import com.mdaul.nutrition.nutritionapi.service.CatalogueMealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TemplateController implements ApiController {

    private final CatalogueFoodService catalogueFoodService;
    private final CatalogueMealService catalogueMealService;

    public TemplateController(CatalogueFoodService catalogueFoodService, CatalogueMealService catalogueMealService) {
        this.catalogueFoodService = catalogueFoodService;
        this.catalogueMealService = catalogueMealService;
    }

    @GetMapping("/nutrition")
    public String showNutritionForm(){
        return "nutrition";
    }

    @GetMapping("/food")
    public String showFoodForm(Model model){
        model.addAttribute("foodList", catalogueFoodService.getFood(getUserId()).orElse(List.of()));
        return "food";
    }

    @GetMapping("/dishes")
    public String showDishesForm(Model model){
        model.addAttribute("dishList", catalogueMealService.getMeal(getUserId()).orElse(List.of()));
        return "dishes";
    }
}
