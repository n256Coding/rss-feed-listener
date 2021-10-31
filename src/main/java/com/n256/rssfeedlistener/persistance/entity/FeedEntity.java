package com.n256.rssfeedlistener.persistance.entity;

import com.n256.rssfeedlistener.dto.FeedResponseDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class to represent 'feed' table.
 */
@Entity
@Table(name = "feed")
@Data
public class FeedEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "identifier", unique = true)
    private String identifier;

    @Column(name = "integrity_checksum", unique = true)
    private String integrityChecksum;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    /**
     * Will convert this entity into the corresponding DTO representation.
     *
     * @return DTO representation of this entity class.
     */
    public FeedResponseDTO toDto() {
        FeedResponseDTO dto = new FeedResponseDTO();
        BeanUtils.copyProperties(this, dto);

        return dto;
    }
}
