package com.village.bot.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "application_data")
@Data
public class ApplicationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "rig_survey_switcher")
    private boolean rigSurveySwitcher;

    @Nullable
    @Column(name = "since_rig_offline")
    private LocalDateTime sinceRigOffline;

    @Nullable
    @Column(name = "prev_rig_offline_notification")
    private LocalDateTime prevRigOfflineNotification;

    @Nullable
    @Column(name = "since_rig_drop")
    private LocalDateTime sinceRigDrop;

    @Nullable
    @Column(name = "prev_rig_drop_notification")
    private LocalDateTime prevRigDropNotification;

    @Column(name = "notification_interval")
    private int notificationInterval;

    @Column(name = "rig_power_drop_coefficient")
    private double rigPowerDropCoefficient;
}
