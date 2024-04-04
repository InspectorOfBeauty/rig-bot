package com.village.bot.components;

import com.village.bot.util.ContentLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RigLoader {
    @Value("${rig.data.url}")
    private final String rigDataUrl;

    public String loadCurrentRigData() {
        try {
            return ContentLoader.loadContent(rigDataUrl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
