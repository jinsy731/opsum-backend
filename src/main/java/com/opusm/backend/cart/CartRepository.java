package com.opusm.backend.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
