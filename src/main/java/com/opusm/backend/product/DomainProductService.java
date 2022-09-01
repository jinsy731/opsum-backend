package com.opusm.backend.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainProductService implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }
}
