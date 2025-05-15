package com.joaovpsguimaraes.desafioanotaai.services;

import com.joaovpsguimaraes.desafioanotaai.domain.category.Category;
import com.joaovpsguimaraes.desafioanotaai.domain.category.CategoryDTO;
import com.joaovpsguimaraes.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.joaovpsguimaraes.desafioanotaai.repositories.CategoryRepository;
import com.joaovpsguimaraes.desafioanotaai.services.aws.AwsSnsService;
import com.joaovpsguimaraes.desafioanotaai.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AwsSnsService awsSnsService;

    public CategoryService(CategoryRepository categoryRepository, AwsSnsService awsSnsService) {
        this.categoryRepository = categoryRepository;
        this.awsSnsService = awsSnsService;
    }

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);
        this.categoryRepository.save(category);

        this.awsSnsService.publishMessage(new MessageDTO(category.toString()));

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

        this.awsSnsService.publishMessage(new MessageDTO(category.toString()));

        return category;
    }

    public void delete(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        this.categoryRepository.delete(category);
    }
}
