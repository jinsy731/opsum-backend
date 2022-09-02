package com.opusm.backend.product;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.opusm.backend.product.dto.ProductCreateDto.*;
import static com.opusm.backend.product.dto.ProductUpdateDto.*;

public interface ProductService {

    Product findById(Long productId);

    @Transactional
    Product create(ProductCreateRequest req);

    @Transactional
    Product update(Long productId, ProductUpdateRequest req);

    @Transactional
    void delete(Long productId);

    List<Product> findAllWithPage(Pageable pageable);
}
