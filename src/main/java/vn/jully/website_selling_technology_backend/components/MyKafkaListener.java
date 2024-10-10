package vn.jully.website_selling_technology_backend.components;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.jully.website_selling_technology_backend.entities.Category;

import java.util.List;

@Component
@KafkaListener(id = "groupA", topics = {"get-all-categories", "insert-a-category" })
public class MyKafkaListener {

    @KafkaHandler
    public void listenCategory (Category category) {
        System.out.println("Received: " + category);
    }

    @KafkaHandler(isDefault = true)
    public void unknown (Object object) {
        System.out.println("Received unknown: " + object);
    }

    @KafkaHandler
    public void listenListOfCategories (List<Category> categories) {
        System.out.println("Received: " + categories);
    }
}