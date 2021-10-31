package com.n256.rssfeedlistener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {

    @JsonProperty(value = "error_reason")
    private String message;
}
