package com.proyectoyomi.syomi.services;

import com.proyectoyomi.syomi.dao.NewsDao;
import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.service.NewsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsServiceTest {

    @Mock
    private NewsDao newsDao;

    @InjectMocks
    private NewsService newsService;

    private News completeNews;

    @BeforeEach
    public void setUp() {
        Date date = new Date();
        String headline = "Headline";
        String url = "url";
        Boolean reviewed = true;
        completeNews = new News(headline, date, url, reviewed);
    }

    @Test
    @Order(1)
    public void findByUrlTest() {
        // precondition
        when(newsDao.findByUrl(completeNews.getUrl())).thenReturn(completeNews);

        // action
        News news = newsService.findByUrl(completeNews.getUrl());

        // verification
        Assertions.assertNotNull(news);
        Assertions.assertEquals(completeNews.getHeadline(), news.getHeadline());
    }

    @Test
    @Order(2)
    public void findByUrl_ThrowIllegalArgumentExceptionTest() {
        // precondition
        String exceptionDescription = "url cannot be null or empty";
        completeNews.setUrl(null);

        // action
        IllegalArgumentException myException =
                Assertions.assertThrows(IllegalArgumentException.class,
                        () -> newsService.findByUrl(completeNews.getUrl()));

        // verification
        Assertions.assertEquals(exceptionDescription, myException.getMessage());
    }

    @Test
    @Order(3)
    public void addNewsTest() {
        // precondition
        when(newsDao.save(completeNews)).thenReturn(completeNews);

        // action
        News news = newsService.addNews(completeNews);

        // verification
        Assertions.assertNotNull(news);
        Assertions.assertEquals(completeNews.getHeadline(), news.getHeadline());
    }

    @Test
    @Order(4)
    public void addNews_ThrowIllegalArgumentExceptionTest() {
        // precondition
        String exceptionDescription = "This url already exists";
        when(newsDao.findByUrl(completeNews.getUrl())).thenReturn(completeNews);

        // action
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> newsService.addNews(completeNews)
        );

        // verification
        Assertions.assertEquals(exceptionDescription, exception.getMessage());
    }

    @Test
    @Order(5)
    public void findByReviewedTest() {
        // precondition
        News news2 = new News("headline2", null, "url2", false);
        when(newsDao.findByReviewed(true)).thenReturn(List.of(completeNews));
        when(newsDao.findByReviewed(false)).thenReturn(List.of(news2));

        // action
        List<News> newsListTrue = newsService.findByReviewed(true);
        List<News> newsListFalse = newsService.findByReviewed(false);

        // verification
        Assertions.assertNotNull(newsListTrue);
        Assertions.assertEquals(1, newsListTrue.size());
        Assertions.assertEquals(completeNews.getHeadline(), newsListTrue.getFirst().getHeadline());

        Assertions.assertNotNull(newsListFalse);
        Assertions.assertEquals(1, newsListFalse.size());
        Assertions.assertEquals(news2.getHeadline(), newsListFalse.getFirst().getHeadline());
    }

}