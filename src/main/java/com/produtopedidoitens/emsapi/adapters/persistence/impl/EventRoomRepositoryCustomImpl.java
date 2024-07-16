package com.produtopedidoitens.emsapi.adapters.persistence.impl;

import com.produtopedidoitens.emsapi.adapters.persistence.EventRoomRepositoryCustom;
import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;
import com.produtopedidoitens.emsapi.application.domain.entities.QEventRoomEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventRoomRepositoryCustomImpl implements EventRoomRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public EventRoomEntity findByRoomNameWithOptimistickLock(String roomName) {
        QEventRoomEntity eventRoomEntity = QEventRoomEntity.eventRoomEntity;

        return jpaQueryFactory.selectFrom(eventRoomEntity)
                .where(eventRoomEntity.roomName.eq(roomName))
                .setLockMode(LockModeType.OPTIMISTIC)
                .fetchOne();
    }
}
