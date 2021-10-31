package com.n256.rssfeedlistener.service;

import com.n256.rssfeedlistener.dto.FeedResponseDTO;

import java.util.List;

/**
 * Entrypoint into the service layer.
 */
public interface RSSFeedService {

    /**
     * Returns list of results matching the request.
     *
     * @param pageString    page number that is required to retrieve
     * @param sizeString    size of the page
     * @param sortBy        result should be sorted by this field
     * @param sortDirection result should be sorted by this direction
     * @return list of results matching the request query.
     */
    List<FeedResponseDTO> retrievePaginatedItems(String pageString, String sizeString, String sortBy, String sortDirection);

    /**
     * Lookup the RSS feed channel and update/refresh the database with latest data
     */
    void refreshRssFeed();

    /**
     * Maintain the size of records in the database. Will cleanup old data that exceeds
     * the max allowed record limit in the database.
     * <p>
     * see: rssFeed.maxAllowedFeedsToPersist property
     */
    void cleanOldFeeds();
}
