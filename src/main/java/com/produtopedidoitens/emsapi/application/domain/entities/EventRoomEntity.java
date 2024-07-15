package com.produtopedidoitens.emsapi.application.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
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
    @NotBlank(message = "{eventroom.capacity.notblank}")
    @Positive(message = "{eventroom.capacity.positive}")
    private int capacity;

    @Column(name = "isfull")
    private boolean isFull;

    @Column(name = "dthreg")
    private LocalDateTime dthReg;

    @Column(name = "dthalt")
    private LocalDateTime dthAlt;

    @Version
    private long version;

}
