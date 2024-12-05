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
}
