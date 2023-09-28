package com.mdaul.nutrition.nutritionapi.configuration;

import com.mdaul.nutrition.nutritionapi.util.DishTemplateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateConfiguration {

    @Bean
    public DishTemplateUtils dishTemplateUtils() {
        return new DishTemplateUtils();
    }
}
