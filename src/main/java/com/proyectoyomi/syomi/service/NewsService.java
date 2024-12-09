package com.proyectoyomi.syomi.service;

import com.proyectoyomi.syomi.dao.NewsDao;
import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.exception.ElementDoesNotExistException;
import com.proyectoyomi.syomi.exception.ElementExistException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsDao newsDao;

    public NewsService(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    // Get all news
    public List<News> getAllNews() {
        return newsDao.findAll();
    }

    // Find by URL. Throws error if input null or empty
    public News findByUrl(String url) {
        if (url == null || url.equals("null") || url.isEmpty())
            throw new IllegalArgumentException(
                    "url cannot be null or empty");

        return newsDao.findByUrl(url);
    }

    // Add new news, if url exists send back error saying it exists
    public News addNews(News news) {
        // TODO Controller catch findbyurl Exception and reply to FE
        String newsUrl = news.getUrl();
        News findByUrl = newsDao.findByUrl(newsUrl);
        if (findByUrl != null) throw new ElementExistException(
                "This url already exists");
        return newsDao.save(news);
    }

    // Find list of news with reviewed conditional
    public List<News> findByReviewed(boolean reviewed) {
        return newsDao.findByReviewed(reviewed);
    }

    // Delete news by id and return true if deleted
    public boolean deleteNews(Long id) {
        long count = newsDao.deleteById(id.longValue());
        if (count == 0) throw new ElementDoesNotExistException(
                "These news cannot be found"
        );
        return true;
    }

    // Delete news by entity
    public boolean deleteNews(News news) {
        Long id = news.getId();
        return deleteNews(id);
    }

    // Delete news by  url
    public boolean deleteNews(String url) {
        long count = newsDao.deleteByUrl(url);
        if (count == 0) throw new ElementDoesNotExistException(
                "These news cannot be found"
        );
        return true;
    }

    // Update news
    public News updateNews(News news) {
        // Get the news and save it again
        Optional<News> newsOptional = newsDao.findById(news.getId());
        if (newsOptional.isEmpty()) throw new ElementDoesNotExistException(
                "News Id cannot be found"
        );
        return newsDao.save(news);
    }

    // Find by id
    public Optional<News> findById(Long id) {
        return newsDao.findById(id);
    }
}
