package com.opusm.backend.api.controller;

import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import com.opusm.backend.product.dto.ProductCreateDto;
import com.opusm.backend.product.dto.ProductUpdateDto;
import com.opusm.backend.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.opusm.backend.product.dto.ProductCreateDto.*;
import static com.opusm.backend.product.dto.ProductUpdateDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductVO product(@PathVariable Long productId) {
        Product product = productService.findById(productId);

        return new ProductVO(product);
    }

    @GetMapping
    public List<ProductVO> products(Pageable pageable) {
        List<Product> products = productService.findAllWithPage(pageable);

        return products.stream().map(product -> new ProductVO(product)).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductCreateResponse create(@RequestBody final ProductCreateRequest req) {
        Product product = productService.create(req);

        return new ProductCreateResponse(product);
    }

    @PatchMapping("/{productId}")
    public ProductUpdateResponse update(@PathVariable Long productId, @RequestBody final ProductUpdateRequest req) {
        Product product = productService.update(productId, req);

        return new ProductUpdateResponse(product);
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId) {
        productService.delete(productId);
    }
}
