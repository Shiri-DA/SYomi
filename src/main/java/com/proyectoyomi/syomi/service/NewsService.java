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


}
