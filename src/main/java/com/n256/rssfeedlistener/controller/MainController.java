package com.n256.rssfeedlistener.controller;

import com.n256.rssfeedlistener.dto.FeedResponseDTO;
import com.n256.rssfeedlistener.persistance.entity.FeedEntity;
import com.n256.rssfeedlistener.service.RSSFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This is the main entrypoint into the application from outside. This will expose REST endpoints.
 */
@RestController
public class MainController {

    private final RSSFeedService rssFeedService;

    @Autowired
    public MainController(RSSFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    @GetMapping("items")
    public List<FeedResponseDTO> retrievePaginatedItems(@RequestParam(name = "page", defaultValue = "") String page,
                                                        @RequestParam(name = "size", defaultValue = "") String size,
                                                        @RequestParam(name = "sort", defaultValue = "") String sortBy,
                                                        @RequestParam(name = "direction", defaultValue = "") String sortDirection) {

        return rssFeedService.retrievePaginatedItems(page, size, sortBy, sortDirection);
    }
}
