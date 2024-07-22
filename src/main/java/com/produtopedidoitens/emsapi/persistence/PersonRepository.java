package com.produtopedidoitens.emsapi.persistence;

import com.produtopedidoitens.emsapi.domain.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonEntity, UUID>, QuerydslPredicateExecutor<PersonEntity> {

}
