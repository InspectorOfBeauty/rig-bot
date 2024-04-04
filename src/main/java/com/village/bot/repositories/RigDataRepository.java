package com.village.bot.repositories;

import com.village.bot.entities.RigData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RigDataRepository extends JpaRepository<RigData, Integer> {
    List<RigData> findByDataTimeBetween(LocalDateTime dateTime1, LocalDateTime dateTime2);

    void deleteByDataTimeLessThan(LocalDateTime dateTime);
}
