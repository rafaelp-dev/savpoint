package com.savpoint.savpoint.dtos.rawg;

import java.util.List;

public record GameSearchResponse(
        List<GameDetailsResponse> results
) {}