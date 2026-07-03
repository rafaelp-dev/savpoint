package com.savpoint.savpoint.dtos.rawg;

import java.util.List;

public record GameDetailsResponse(
        String id,
        String slug,
        String name,
        String released,
        String background_image,
        Integer playtime,
        List<PlatformDTO> platforms,
        List<GenreDTO> genres
){}