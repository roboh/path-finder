package com.pwc.test.pathfinder.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Country {

    @JsonProperty("cca3")
    private final String code;

    @JsonProperty("borders")
    private final List<String> borders;

    @JsonCreator
    public Country(@JsonProperty("cca3") final String code, @JsonProperty("borders") final List<String> borders) {
        this.code = code;
        this.borders = borders;
    }
}
