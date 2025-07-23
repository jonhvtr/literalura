package com.jonhvtr.literalura.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDTO(
        String name,
        @JsonProperty("birth_year") int birthYear,
        @JsonProperty("death_year") int deathYear
) {
}