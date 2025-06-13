package com.shirish.repository;

import com.shirish.modal.Coin;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CoinRepository extends JpaRepository<Coin,String> {
}
