package com.joaovpsguimaraes.desafioanotaai.services;

import com.joaovpsguimaraes.desafioanotaai.domain.category.Category;
import com.joaovpsguimaraes.desafioanotaai.domain.category.CategoryDTO;
import com.joaovpsguimaraes.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.joaovpsguimaraes.desafioanotaai.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);
        this.categoryRepository.save(category);
        return category;
    }

    public Optional<Category> findById(String id) {
        return this.categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Category update(String id, CategoryDTO categoryDTO) {
        Category category = this.categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (!categoryDTO.title().isEmpty()) category.setTitle(categoryDTO.title());
        if (!categoryDTO.description().isEmpty()) category.setDescription(categoryDTO.description());

        this.categoryRepository.save(category);
        return category;
    }

    public void delete(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        this.categoryRepository.delete(category);
    }
}
