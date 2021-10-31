package com.n256.rssfeedlistener;

import com.n256.rssfeedlistener.controller.MainController;
import com.n256.rssfeedlistener.persistance.repository.FeedRepository;
import com.n256.rssfeedlistener.service.RSSFeedService;
import com.n256.rssfeedlistener.task.RSSFeedScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RssFeedListenerApplicationTests {

	@Autowired
	private RSSFeedService rssFeedService;

	@Autowired
	private MainController controller;

	@Autowired
	private FeedRepository repository;

	@Autowired
	private RSSFeedScheduler scheduler;

	@Test
	void contextLoads() {
		assertThat(rssFeedService).isNotNull();
		assertThat(controller).isNotNull();
		assertThat(repository).isNotNull();
		assertThat(scheduler).isNotNull();
	}

}
