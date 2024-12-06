package com.proyectoyomi.syomi.controller;

import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News savedNews = newsService.addNews(news);
        return new ResponseEntity<>(savedNews, HttpStatus.CREATED);
    }

}
