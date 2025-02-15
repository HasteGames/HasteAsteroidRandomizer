package com.hastegames.asteroidrandomizer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AsteroidRegion {

    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;

    public String asFormat() {
        return minX + "," + maxX + "," + minZ + "," + maxZ;
    }

}
