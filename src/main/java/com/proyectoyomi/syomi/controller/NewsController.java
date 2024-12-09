package com.proyectoyomi.syomi.controller;

import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(path = "/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Create and returns the info of the new news, if url exists returns error
    // TODO: error should return news info
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News savedNews = newsService.addNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNews);
    }

    // Get all the news
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

    // Filters by reviewed true or false, return empty list if there is nothing
    @GetMapping(value = "/reviewed",produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<News>> getNewsByReviewed(
            @RequestParam boolean reviewed) {
        List<News> newsList = newsService.findByReviewed(reviewed);
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

    // Find by url, if it is null or empty throws error if it does not find it returns 404
    @GetMapping(params = "url", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<News> getNewsByUrl(@RequestParam String url) {
        News news = newsService.findByUrl(url);
        return ResponseEntity.status(HttpStatus.OK).body(news);
    }

    // Update news. If it does not find id throw error. Return new + location
    @PutMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<News> updateNews(@RequestBody News news) {
        News updatedNews = newsService.updateNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedNews);
    }
}
