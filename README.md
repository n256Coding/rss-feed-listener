# rss-feed-listener
A sample application to read RSS feeds and store. Application exposes a REST endpoint to get those stored feeds.

### Sample API call
```curl
curl --location --request GET 'http://localhost:8080/items?page=0&size=2&sort=updated_date&direction=desc'
```

#### API Documentation
API documentation can be accessed from `http://localhost:8080/swagger-ui/`.


### Limitations
* This supportes only RSS feed channels that include guid in each entry.
* Can be integrated with only one RSS feed channel at a time.
* Not optimized to handle large number of feeds.

### Assumptions
This application is mainly assumes that each entry in RSS feed should have a unique guid. But in reality it is not true. 
It is not mandatory to have guid. This limitation will be fixed in future release.
