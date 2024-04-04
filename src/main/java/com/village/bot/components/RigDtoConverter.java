package com.village.bot.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.village.bot.entities.RigData;
import com.village.bot.entities.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.village.bot.util.HashUtil.megaHashToHash;

@Component
@RequiredArgsConstructor
public class RigDtoConverter {
    private final ObjectMapper mapper = new ObjectMapper();
    public RigData parseRigData(String rigDataJsonDto) {
        RigData rigData = new RigData();
        try {
            JsonNode node = mapper.readTree(rigDataJsonDto);
            ArrayNode arrayNode = (ArrayNode) node.get("data").get("workerDatas");
            JsonNode workerData = arrayNode.get(0);

            String hashRate = workerData.get("h15m").asText();
            String dayHashRate = workerData.get("h1d").asText();
            RequestStatus status = switch (workerData.get("status").asInt()) {
                case 3 -> RequestStatus.RIG_BROKEN;
                case 2 -> RequestStatus.RIG_OFFLINE;
                default -> RequestStatus.RIG_ONLINE;
            };

            if (status == RequestStatus.RIG_ONLINE) {
                rigData.setHashRate(megaHashToHash(new BigDecimal(hashRate.substring(0, hashRate.indexOf(" ")).trim())));
            } else {
                rigData.setHashRate(null);
            }

            rigData.setDayHashRate(megaHashToHash(new BigDecimal(dayHashRate.substring(0, hashRate.indexOf(" ")).trim())));
            rigData.setStatus(status);
            rigData.setDataTime(LocalDateTime.now(ZoneOffset.UTC));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rigData;
    }
}
