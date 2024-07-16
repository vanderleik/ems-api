package com.produtopedidoitens.emsapi.adapters.persistence;

import com.produtopedidoitens.emsapi.application.domain.entities.EventRoomEntity;

public interface EventRoomRepositoryCustom {

    EventRoomEntity findByRoomNameWithOptimistickLock(String roomName);

}
