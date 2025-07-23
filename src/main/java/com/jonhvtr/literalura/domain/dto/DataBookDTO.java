package com.jonhvtr.literalura.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBookDTO(List<BookDTO> results) {
}
