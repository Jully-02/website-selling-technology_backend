package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.CategoryDTO;

@RestController
@RequestMapping("api/v1/categories")
@Validated
public class CategoryController {

    @GetMapping("")
    public ResponseEntity<String> getAllCategories (
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("getAllCategories with page = " + page + " and limit = " + limit);
    }

    @PostMapping("")
    public ResponseEntity<String> insertCategory (@Valid  @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok("insert category with data = " + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory (@PathVariable long id) {
        return ResponseEntity.ok("updateCategory with ID = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory (@PathVariable long id) {
        return ResponseEntity.ok("deleteCategory with ID = " + id);
    }
}
