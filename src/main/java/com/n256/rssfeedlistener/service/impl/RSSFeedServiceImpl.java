package com.n256.rssfeedlistener.service.impl;

import com.n256.rssfeedlistener.constant.SortingField;
import com.n256.rssfeedlistener.dto.FeedResponseDTO;
import com.n256.rssfeedlistener.persistance.entity.FeedEntity;
import com.n256.rssfeedlistener.persistance.repository.FeedRepository;
import com.n256.rssfeedlistener.service.RSSFeedService;
import com.n256.rssfeedlistener.util.InputValidatorUtil;
import com.n256.rssfeedlistener.util.IntegrityUtil;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RSSFeedServiceImpl implements RSSFeedService {

    private final String rssFeedSourceUrl;
    private final int defaultPageSize;
    private final String defaultSortingField;
    private final String defaultSortingDirection;
    private final int maxAllowedFeedsToPersist;

    private final FeedRepository feedRepository;

    @Autowired
    public RSSFeedServiceImpl(@Value("${rssFeed.source.url}") String rssFeedSourceUrl,
                              @Value("${rssFeed.api.defaultPageSize}") int defaultPageSize,
                              @Value("${rssFeed.api.defaultSortingField}") String defaultSortingField,
                              @Value("${rssFeed.api.defaultSortingDirection}") String defaultSortingDirection,
                              @Value("${rssFeed.maxAllowedFeedsToPersist}") int maxAllowedFeedsToPersist,
                              FeedRepository feedRepository) {
        this.rssFeedSourceUrl = rssFeedSourceUrl;
        this.defaultPageSize = defaultPageSize;
        this.defaultSortingField = defaultSortingField;
        this.defaultSortingDirection = defaultSortingDirection;
        this.maxAllowedFeedsToPersist = maxAllowedFeedsToPersist;
        this.feedRepository = feedRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeedResponseDTO> retrievePaginatedItems(String pageString, String sizeString, String sortBy, String sortDirection) {

        int page = InputValidatorUtil.validatePage(pageString, 0);
        int size = InputValidatorUtil.validatePageSize(sizeString, defaultPageSize);
        SortingField sortingField = InputValidatorUtil.validateSortBy(sortBy, defaultSortingField);
        Sort.Direction direction = InputValidatorUtil.validateSortingDirection(sortDirection, defaultSortingDirection);

        return getFeedResponseList(page, size, Sort.by(direction, sortingField.getEntityField()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshRssFeed() {

        List<SyndEntry> latestEntries = fetchLatestEntries(maxAllowedFeedsToPersist);
        List<String> newIdList = latestEntries.stream().map(SyndEntry::getUri).collect(Collectors.toList());

        List<FeedEntity> existingEntities = feedRepository.findByIdentifierIn(newIdList);
        List<String> commonIdList = existingEntities.stream().map(FeedEntity::getIdentifier).collect(Collectors.toList());

        int updatedCount = updateModifiedFeeds(latestEntries, existingEntities);
        log.info("{} feeds updated", updatedCount);

        int insertedCount = insertNewRecords(latestEntries, commonIdList);
        log.info("{} new feeds added", insertedCount);

        cleanOldFeeds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanOldFeeds() {

        List<Integer> allowedItems = feedRepository.findAll(PageRequest.of(0, maxAllowedFeedsToPersist,
                Sort.Direction.DESC,
                SortingField.UPDATED_DATE.getEntityField())).toList().stream()
                .map(FeedEntity::getId)
                .collect(Collectors.toList());

        feedRepository.deleteByIdNotIn(allowedItems);

        log.info("Cleaning old entries completed");
    }

    /**
     * Inserts new feeds into the database.
     *
     * @param latestEntries list of latest entries taken from RSS feed channel.
     * @param commonIdList list of identifier that are already existing in database and in latest feed entry list.
     * @return number of entries that has been inserted
     */
    private int insertNewRecords(List<SyndEntry> latestEntries, List<String> commonIdList) {

        MutableInt insertedCount = new MutableInt(0);

        latestEntries.forEach(entry -> {

            if(!commonIdList.contains(entry.getUri())) {

                insertedCount.increment();

                FeedEntity entity = new FeedEntity();
                entity.setIdentifier(entry.getUri());
                entity.setIntegrityChecksum(IntegrityUtil.calcMd5SumOf(entry));
                entity.setTitle(entry.getTitle());
                entity.setDescription(entry.getDescription().getValue());
                entity.setPublishedDate(entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                entity.setUpdateDate(LocalDateTime.now());

                feedRepository.save(entity);
            }
        });

        return insertedCount.intValue();
    }

    /**
     * If existing feed has been updated, reflect new content into the database by updating existing records.
     *
     * @param latestEntries list of latest entries taken from RSS feed channel.
     * @param existingEntities list of existing records in database which are matching with {@code latestEntries}.
     * @return number of items that has been updated.
     */
    private int updateModifiedFeeds(List<SyndEntry> latestEntries, List<FeedEntity> existingEntities) {

//        Using mutableInt since immutable integers cannot be incremented within lambda functions
        MutableInt updatedCount = new MutableInt(0);

        existingEntities.forEach(feedEntity -> {
            Optional<SyndEntry> entry = latestEntries.stream().filter(x -> x.getUri().equals(feedEntity.getIdentifier())).findAny();

            if (entry.isPresent() && feedEntity.getIntegrityChecksum().equals(IntegrityUtil.calcMd5SumOf(entry.get()))) {

                updatedCount.increment();

                feedEntity.setIntegrityChecksum(IntegrityUtil.calcMd5SumOf(
                        feedEntity.getTitle(),
                        feedEntity.getDescription(),
                        feedEntity.getPublishedDate())
                );
                feedEntity.setUpdateDate(LocalDateTime.now());
                feedEntity.setTitle(entry.get().getTitle());
                feedEntity.setDescription(entry.get().getDescription().getValue());
                feedEntity.setPublishedDate(entry.get().getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                feedRepository.save(feedEntity);
            }
        });

        return updatedCount.intValue();
    }

    /**
     * Connect to the RSS feed channel and fetch results.
     *
     * @param maximumLimit Maximum number of results needs to to fetched.
     * @return list of RSS entries from the web.
     */
    private List<SyndEntry> fetchLatestEntries(int maximumLimit) {

        try {

            try (XmlReader reader = new XmlReader(new URL(rssFeedSourceUrl))) {
                SyndFeed feed = new SyndFeedInput().build(reader);

                return feed.getEntries().stream().limit(maximumLimit).collect(Collectors.toList());
            }

        } catch (Exception e) {
            log.error("Error occurred while reading the feed", e);
        }

        return Collections.emptyList();
    }

    /**
     * Get list of records in DTO format from the database for given parameters.
     *
     * @param page Number of the page that needs to be returned.
     * @param size Size of the page.
     * @param sort Sorting properties of the results.
     * @return list of records from database in DTO format.
     */
    private List<FeedResponseDTO> getFeedResponseList(int page, int size, Sort sort) {

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return feedRepository.findAll(pageRequest).toList().stream()
                .map(FeedEntity::toDto)
                .collect(Collectors.toList());
    }
}
