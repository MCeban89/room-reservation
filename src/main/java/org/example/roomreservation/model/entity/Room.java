package org.example.roomreservation.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer floor;

    private String description;

    @Builder.Default
    private Boolean hasProjector=false;

    @Builder.Default
    private Boolean hasWhiteboard=false;

    @Builder.Default
    private Boolean hasVideoConference=false;

    @Builder.Default
    private Boolean active=false;



}
