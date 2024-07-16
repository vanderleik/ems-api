package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.application.domain.entities.CofeeSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface CofeeSpaceRepository extends JpaRepository<CofeeSpaceEntity, UUID>, QuerydslPredicateExecutor<CofeeSpaceEntity> {

}
