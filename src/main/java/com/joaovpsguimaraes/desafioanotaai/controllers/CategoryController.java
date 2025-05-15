package com.joaovpsguimaraes.desafioanotaai.controllers;

import com.joaovpsguimaraes.desafioanotaai.domain.category.Category;
import com.joaovpsguimaraes.desafioanotaai.domain.category.CategoryDTO;
import com.joaovpsguimaraes.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.joaovpsguimaraes.desafioanotaai.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> insert(@RequestBody CategoryDTO categoryDTO) {
        Category category = this.categoryService.create(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = this.categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable String id) {
        Category category = this.categoryService.findById(id).orElseThrow(CategoryNotFoundException::new);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        Category updateCategory = this.categoryService.update(id, categoryDTO);

        return ResponseEntity.ok().body(updateCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
