package com.produtopedidoitens.emsapi.application.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "tbperson")
public class PersonEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personid")
    private UUID personId;

    @NotBlank(message = "{person.firstname.notblank}")
    @Column(name = "firstname")
    private String firstName;

    @NotBlank(message = "{person.lastname.notblank}")
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "isactive")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "eventroomid")
    private EventRoomEntity eventRoom;

    @ManyToOne
    @JoinColumn(name = "cofeespaceid")
    private CofeeSpaceEntity cofeeSpace;

    @Column(name = "dthreg")
    private LocalDateTime dthReg;

    @Column(name = "dthalt")
    private LocalDateTime dthAlt;

    @Version
    private long version;

}
