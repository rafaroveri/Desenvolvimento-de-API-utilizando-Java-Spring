package com.petshop.api.service;

import com.petshop.api.dto.request.ProductRequest;
import com.petshop.api.dto.response.CategoryResponse;
import com.petshop.api.dto.response.ProductResponse;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.Category;
import com.petshop.api.model.Product;
import com.petshop.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Long categoryId) {
        List<Product> products = (categoryId != null)
                ? productRepository.findByCategoryId(categoryId)
                : productRepository.findAll();
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(findProductById(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.findCategoryById(request.getCategoryId());
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProductById(id);
        Category category = categoryService.findCategoryById(request.getCategoryId());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        Product product = findProductById(id);

        // Regra de negócio: não permite deletar produto com pedidos
        if (productRepository.hasOrders(id)) {
            throw new BusinessException(
                "Não é possível excluir o produto '" + product.getName() + "' pois ele possui pedidos associados.");
        }
        productRepository.delete(product);
    }

    // ── Métodos auxiliares ──────────────────────────────────────────────────────

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    private ProductResponse toResponse(Product product) {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .description(product.getCategory().getDescription())
                .build();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .category(categoryResponse)
                .build();
    }
}
