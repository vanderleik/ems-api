package com.produtopedidoitens.emsapi.persistence;

import com.produtopedidoitens.emsapi.domain.entities.CofeeSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface CofeeSpaceRepository extends JpaRepository<CofeeSpaceEntity, UUID>, QuerydslPredicateExecutor<CofeeSpaceEntity> {

}
