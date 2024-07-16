package com.produtopedidoitens.emsapi.application.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@DynamicUpdate == usar quanto quando temos bloqueio otimista sem versão
@Entity
@Table(name = "tbcofeespace")
public class CofeeSpaceEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cofeespaceid")
    private UUID cofeeSpaceId;

    @NotBlank(message = "{cofeespace.cofeespacename.notblank}")
    @Column(name = "cofeespacename")
    private String cofeeSpaceName;

    @NotBlank(message = "{cofeespace.capacity.notblank}")
    @Positive(message = "{cofeespace.capacity.positive}")
    @Column(name = "capacity")
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
