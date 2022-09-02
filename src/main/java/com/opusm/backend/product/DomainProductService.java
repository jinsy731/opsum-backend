package com.opusm.backend.product;

import com.opusm.backend.common.support.convert.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.opusm.backend.product.dto.ProductCreateDto.*;
import static com.opusm.backend.product.dto.ProductUpdateDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainProductService implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    @Override
    public Product create(ProductCreateRequest req) {
        Product product = req.toEntity();
        return productRepository.save(product);
    }

    @Override
    public Product update(Long productId, ProductUpdateRequest req) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.update(req);

        return product;
    }

    @Override
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> findAllWithPage(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }
}
