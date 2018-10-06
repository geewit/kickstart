package io.geewit.business.location.enums;

import io.geewit.core.enums.Name;

/**
 * @author geewit
 */
public enum LocationType implements Name {
    COUNTRY("国家"),
    PROVINCE("省州"),
    CITY("城市"),
    DISTRICT("区县");

    LocationType(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
