package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface EventRoomRepository extends JpaRepository<EventRoomEntity, UUID>, QuerydslPredicateExecutor<EventRoomEntity> {

}
