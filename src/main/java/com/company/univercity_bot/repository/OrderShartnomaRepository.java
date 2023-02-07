package com.company.univercity_bot.repository;

import com.company.univercity_bot.model.OrderShartnoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderShartnomaRepository extends JpaRepository<OrderShartnoma, Integer> {

}
