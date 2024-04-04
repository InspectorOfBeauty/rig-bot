package com.village.bot.components;

import com.village.bot.entities.RigData;
import com.village.bot.entities.RequestStatus;
import com.village.bot.repositories.ApplicationDataRepository;
import com.village.bot.repositories.RigDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RigComponent {
    private final RigDataRepository rigDataRepository;
    private final RigLoader rigLoader;
    private final RigDtoConverter rigDtoConverter;
    private final ApplicationDataRepository applicationDataRepository;

    public boolean isRigOffline() {
        List<RigData> rigData = rigDataRepository.findByDataTimeBetween(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(3), LocalDateTime.now());
        int offlineCount = 0;
        for (RigData fD : rigData) {
            if (fD.getStatus() != RequestStatus.RIG_ONLINE) {
                offlineCount++;
            }
        }

        return offlineCount == rigData.size();
    }

    public boolean isRigPowerDropped(double coefficient) {
        boolean result = false;
        List<RigData> rigData = rigDataRepository.findByDataTimeBetween(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(30), LocalDateTime.now());
        int counter = 0;
        for (RigData fD : rigData) {
            if (fD.getStatus() == RequestStatus.RIG_ONLINE && fD.getHashRate().compareTo(fD.getDayHashRate().multiply(BigDecimal.valueOf(coefficient))) < 0) {
                counter++;
            }
        }
        if (counter == rigData.size()) {
            result = true;
        }

        return result;
    }

    public RigData loadRigData() {
        return rigDtoConverter.parseRigData(rigLoader.loadCurrentRigData());
    }

    public List<RigData> getRigDataHistoryFilteredByIntervalAndPeriod(int historyDepthInHours, LocalDateTime now) {
        List<RigData> lastRigData = rigDataRepository.findByDataTimeBetween(now.minusHours(historyDepthInHours), LocalDateTime.now(ZoneOffset.UTC));
        lastRigData.sort(Comparator.comparing(RigData::getDataTime));
        Collections.reverse(lastRigData);
        return lastRigData;
    }

    public void changeRigSurveySwitcher() {
        applicationDataRepository.updateRigSurveySwitcher(!applicationDataRepository.isSurveyEnabled());
    }

    public void getAndSaveRigData() {
        RigData rigData;
        try {
            rigData = rigDtoConverter.parseRigData(rigLoader.loadCurrentRigData());
        } catch (Exception e) {
            log.error("Error rig data request", e);
            rigData = new RigData();
            rigData.setStatus(RequestStatus.REQUEST_ERROR);
            rigData.setDayHashRate(BigDecimal.ZERO);
            rigData.setStatus(RequestStatus.REQUEST_ERROR);
        }

        rigDataRepository.save(rigData);
    }
}
