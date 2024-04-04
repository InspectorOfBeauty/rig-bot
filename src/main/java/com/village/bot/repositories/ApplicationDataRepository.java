package com.village.bot.repositories;

import com.village.bot.entities.ApplicationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ApplicationDataRepository extends JpaRepository<ApplicationData, Integer> {
    @Query("SELECT ad FROM ApplicationData ad WHERE ad.id = 1")
    ApplicationData getData();

    @Query("SELECT ad.rigSurveySwitcher FROM ApplicationData ad WHERE ad.id = 1")
    boolean isSurveyEnabled();

    @Query("SELECT ad.notificationInterval FROM ApplicationData ad WHERE ad.id = 1")
    int getNotificationInterval();

    @Query("SELECT ad.rigPowerDropCoefficient FROM ApplicationData ad WHERE ad.id = 1")
    double getRigPowerDropCoefficient();

    @Modifying
    @Query("UPDATE ApplicationData ad SET ad.rigSurveySwitcher = :rigSurveySwitcher WHERE ad.id = 1")
    void updateRigSurveySwitcher(@Param("rigSurveySwitcher") boolean newRigSurveyState);

    @Modifying
    @Query("UPDATE ApplicationData ad SET ad.notificationInterval = :notificationInterval WHERE ad.id = 1")
    void updateNotificationInterval(@Param("notificationInterval") int newNotificationInterval);

    @Modifying
    @Query("UPDATE ApplicationData ad SET ad.rigPowerDropCoefficient = :rigPowerDropCoefficient WHERE ad.id = 1")
    void updateRigPowerDropCoefficient(@Param("rigPowerDropCoefficient") double newRigPowerDropCoefficient);

}
