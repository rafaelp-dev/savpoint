package com.savpoint.savpoint.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {
    private static final ZoneId BRASILIA_ZONE = ZoneId.of("America/Sao_Paulo");

    public static LocalDateTime nowBrasilia() {
        return LocalDateTime.now(BRASILIA_ZONE);
    }
}
