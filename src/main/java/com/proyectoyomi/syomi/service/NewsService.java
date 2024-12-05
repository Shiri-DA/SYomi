package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.NewsDao;
import com.proyectoyomi.syomi.entity.News;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Find list of news with reviewed conditional
    public List<News> findByReviewed(boolean reviewed) {
        return newsDao.findByReviewed(reviewed);
    }

    // Delete news by id and return true if deleted
    public boolean deleteNews(Long id) {
        int count = newsDao.deleteNewsById(id);
        if (count == 0) throw new IllegalArgumentException(
                "These news cannot be found"
        );
        return true;
    }

    // Delete news by entity
    public boolean deleteNews(News news) {
        int count = newsDao.deleteNews(news);
        if (count == 0) throw new IllegalArgumentException(
                "These news cannot be found"
        );
        return true;
    }

    // Delete news by  url
    public boolean deleteNews(String url) {
        int count = newsDao.deleteNewsByUrl(url);
        if (count == 0) throw new IllegalArgumentException(
                "These news cannot be found"
        );
        return true;
    }
}
