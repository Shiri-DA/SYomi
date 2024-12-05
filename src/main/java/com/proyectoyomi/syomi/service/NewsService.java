package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.NewsDao;
import com.proyectoyomi.syomi.entity.News;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private final NewsDao newsDao;

    public NewsService(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    // Find by URL. Throws error if input null or empty
    public News findByUrl(String url) {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException(
                    "url cannot be null or empty");

        return newsDao.findByUrl(url);
    }

    // Add new news, if url exists send back error saying it exists
    public News addNews(News news) {
        // TODO Controller catch findbyurl Exception and reply to FE
        String newsUrl = news.getUrl();
        News findByUrl = newsDao.findByUrl(newsUrl);
        if (findByUrl != null) throw new IllegalArgumentException(
                "This url already exists");
        return newsDao.save(news);
    }
}
