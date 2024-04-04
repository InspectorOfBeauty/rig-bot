package com.village.bot.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rig_data")
@Data
public class RigData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "hashrate")
    private BigDecimal hashRate;

    @Column(name = "day_hashrate")
    private BigDecimal dayHashRate;

    @Column(name = "data_time")
    private LocalDateTime dataTime;
}
