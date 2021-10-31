package com.n256.rssfeedlistener.task;

import com.n256.rssfeedlistener.service.RSSFeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Defines background schedulers.
 */
@Component
@Slf4j
public class RSSFeedScheduler {

    private final boolean isPollingEnabled;

    private final RSSFeedService rssFeedService;

    @Autowired
    public RSSFeedScheduler(@Value("${rssFeed.polling.enabled}") boolean isPollingEnabled,
                            RSSFeedService rssFeedService) {
        this.isPollingEnabled = isPollingEnabled;
        this.rssFeedService = rssFeedService;
    }

    /**
     * This scheduler is responsible to update and refresh RSS feeds in the database.
     */
    @Scheduled(fixedRateString = "${rssFeed.polling.rateInMillis}")
    public void pollFeeds() {

        if (isPollingEnabled) {
            rssFeedService.refreshRssFeed();

        } else {
            log.warn("Polling task is not enabled and RSS feeds will not be updated !. Please check the configuration to enable it.");
        }
    }
}
