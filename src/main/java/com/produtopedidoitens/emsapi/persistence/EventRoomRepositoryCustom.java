package com.produtopedidoitens.emsapi.persistence;

import com.produtopedidoitens.emsapi.domain.entities.EventRoomEntity;

public interface EventRoomRepositoryCustom {

    EventRoomEntity findByRoomNameWithOptimistickLock(String roomName);

}
