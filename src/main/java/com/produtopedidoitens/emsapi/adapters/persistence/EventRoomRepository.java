package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface EventRoomRepository extends JpaRepository<EventRoomEntity, UUID>, QuerydslPredicateExecutor<EventRoomEntity> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT e FROM EventRoomEntity e WHERE e.roomName = :roomName")
    EventRoomEntity findByRoomName(String roomName);

}
