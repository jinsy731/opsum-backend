package com.opusm.backend.order.dto;

import com.opusm.backend.cart.CartProduct;
import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.order.PayMethod;
import com.opusm.backend.order.dto.OrderProductCreateDto.OrderProductCreateRequest;
import com.opusm.backend.order.vo.OrderProductVO;
import com.opusm.backend.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateDto {

    @Getter
    @AllArgsConstructor
    public static class OrderCreateRequest {
        Long customerId;
        Long productId;
        PayMethod paymentMethod;
        int amount;

        public Order toEntity(Customer customer, Product product, PayMethod paymentMethod) {
            return new Order(customer, new OrderProduct(amount, product), paymentMethod);
        }

        public Order toEntity(Customer customer, List<CartProduct> cartProducts, PayMethod paymentMethod) {
            return new Order(
                    customer,
                    cartProducts.stream()
                            .map(cartProduct -> new OrderProduct(cartProduct.getAmount(), cartProduct.getProduct()))
                            .collect(Collectors.toList()),
                    paymentMethod);
        }
    }

    public static class OrderCreateResponse {
        private int totalPrice;
        private List<OrderProductVO> orderProducts;

        public OrderCreateResponse(Order order) {
            ModelMappers.modelMapper.map(order, this);
        }
    }
}
