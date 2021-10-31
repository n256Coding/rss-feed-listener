package com.n256.rssfeedlistener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * This is a DTO class which is used to respond for REST API requests.
 */
@Data
public class FeedResponseDTO {

    @JsonProperty(value = "title")
    String title;

    @JsonProperty(value = "description")
    String description;

    @JsonProperty(value = "published_date")
    LocalDateTime publishedDate;

    @JsonProperty(value = "updated_date")
    LocalDateTime updateDate;
}
