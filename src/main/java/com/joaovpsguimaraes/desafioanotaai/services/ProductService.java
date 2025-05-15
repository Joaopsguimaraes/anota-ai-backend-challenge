package com.joaovpsguimaraes.desafioanotaai.services;

import com.joaovpsguimaraes.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.joaovpsguimaraes.desafioanotaai.domain.product.Product;
import com.joaovpsguimaraes.desafioanotaai.domain.product.ProductDTO;
import com.joaovpsguimaraes.desafioanotaai.domain.product.exceptions.ProductException;
import com.joaovpsguimaraes.desafioanotaai.repositories.ProductRepository;
import com.joaovpsguimaraes.desafioanotaai.services.aws.AwsSnsService;
import com.joaovpsguimaraes.desafioanotaai.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AwsSnsService awsSnsService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, AwsSnsService awsSnsService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.awsSnsService = awsSnsService;
    }

    public Product create(ProductDTO productDTO) {
        this.categoryService.findById(productDTO.categoryId()).orElseThrow(CategoryNotFoundException::new);

        Product product = new Product(productDTO);

        this.productRepository.save(product);

        this.awsSnsService.publishMessage(new MessageDTO(product.toString()));

        return product;
    }

    public Product findById(String id) {
        return this.productRepository.findById(id).orElseThrow(ProductException::new);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Product update(String id, ProductDTO productDTO) {
        Product product = this.productRepository.findById(id).orElseThrow(ProductException::new);

        if (!productDTO.title().isEmpty()) product.setTitle(productDTO.title());
        if (!productDTO.description().isEmpty()) product.setDescription(productDTO.description());
        if (!(productDTO.price() == null)) product.setPrice(productDTO.price());

        if (!productDTO.categoryId().isEmpty()) {
            this.categoryService.findById(productDTO.categoryId()).orElseThrow(CategoryNotFoundException::new);

            product.setCategoryId(productDTO.categoryId());
        }
        this.productRepository.save(product);

        this.awsSnsService.publishMessage(new MessageDTO(product.toString()));

        return product;
    }

    public void delete(String id) {
        Product product = this.productRepository.findById(id).orElseThrow(ProductException::new);

        this.productRepository.delete(product);
    }

}
