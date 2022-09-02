package com.opusm.backend.order;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.common.exception.ErrorCode;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.order.dto.OrderCreateDto.OrderCreateRequest;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.opusm.backend.common.exception.Preconditions.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DomainOrderService implements OrderService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    @Override
    public Order singleOrder(OrderCreateRequest req) {
        Customer customer = customerService.findById(req.getCustomerId());
        Product product = productService.findById(req.getProductId());
        Order order = req.toEntity(customer, product, req.getPaymentMethod());

        // 재고, 자산, 포인트 validation
        validate(product.getStock() >= req.getAmount(), ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        if(order.getPaymentMethod() == PayMethod.ASSET) validate(customer.getAssets() >= order.getTotalPrice(), ErrorCode.CUSTOMER_NOT_ENOUGH_ASSETS);
        if(order.getPaymentMethod() == PayMethod.POINT) validate(customer.getPoints() >= order.getTotalPrice(), ErrorCode.CUSTOMER_NOT_ENOUGH_POINTS);

        // 고객 자산 및 포인트 업데이트, 상품 재고 업데이트
        customer.updateAssetsAndPointByOrder(order);
        product.deductStock(req.getAmount());

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order cartOrder(OrderCreateRequest req) {
        Customer customer = customerService.findById(req.getCustomerId());
        Cart cart = customer.getCart();
        Order order = req.toEntity(customer, cart.getCartProducts(), req.getPaymentMethod());

        // 재고, 자산, 포인트 validation
        order.getOrderProducts().stream().forEach(it -> validate(it.getProduct().getStock() >= it.getAmount(), ErrorCode.PRODUCT_NOT_ENOUGH_STOCK));
        if(order.getPaymentMethod() == PayMethod.ASSET) validate(customer.getAssets() >= order.getTotalPrice(), ErrorCode.CUSTOMER_NOT_ENOUGH_ASSETS);
        if(order.getPaymentMethod() == PayMethod.POINT) validate(customer.getPoints() >= order.getTotalPrice(), ErrorCode.CUSTOMER_NOT_ENOUGH_POINTS);

        // 고객 자산 및 포인트 업데이트, 상품 재고 업데이트, 장바구니 초기화
        customer.updateAssetsAndPointByOrder(order);
        order.getOrderProducts().stream().forEach(it -> it.getProduct().deductStock(it.getAmount()));
        cart.clearCart();

        orderRepository.save(order);
        return order;
    }
}
