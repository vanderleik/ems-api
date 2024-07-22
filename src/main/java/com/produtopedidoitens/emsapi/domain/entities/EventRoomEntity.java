package com.produtopedidoitens.emsapi.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
//@DynamicUpdate == usar quanto quando temos bloqueio otimista sem versão
@Entity
@Table(name = "tbeventroom")
public class EventRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "eventroomid")
    private UUID eventRoomId;

    @NotBlank(message = "{eventroom.roomname.notblank}")
    @Column(name = "roomname")
    private String roomName;

    @Column(name = "capacity")
    @NotNull(message = "{eventroom.capacity.notblank}")
    @Positive(message = "{eventroom.capacity.positive}")
    private int capacity;

    @Column(name = "isfull")
    private boolean isFull;

    @Column(name = "dthreg")
    private LocalDateTime dthreg;

    @Column(name = "dthalt")
    private LocalDateTime dthalt;

    @Version
    private int version;

    @PrePersist
    protected void onCreate() {
        dthreg = LocalDateTime.now();
        dthalt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dthalt = LocalDateTime.now();
    }

}
