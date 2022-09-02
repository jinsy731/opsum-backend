package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderProducts"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select o from Order o where o.customer.id = :customerId")
    Page<Order> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
