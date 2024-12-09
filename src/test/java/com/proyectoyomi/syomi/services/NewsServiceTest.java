package com.proyectoyomi.syomi.services;

import com.proyectoyomi.syomi.dao.NewsDao;
import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.exception.ElementDoesNotExistException;
import com.proyectoyomi.syomi.exception.ElementExistException;
import com.proyectoyomi.syomi.service.NewsService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        completeNews = new News(1L, headline, date, url, reviewed);
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
        ElementExistException exception = Assertions.assertThrows(
                ElementExistException.class, () -> newsService.addNews(completeNews)
        );

        // verification
        Assertions.assertEquals(exceptionDescription, exception.getMessage());
    }

    @Test
    @Order(5)
    public void findByReviewedTest() {
        // precondition
        News news2 = new News(2L, "headline2", null, "url2", false);
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

    // Delete testing
    @Test
    @Order(6)
    public void deleteByIdTest() {
        // precondition
        when(newsDao.deleteNewsById(completeNews.getId())).thenReturn(1);

        // action
        boolean isDeleted = newsService.deleteNews(completeNews.getId());

        // verification
        Assertions.assertTrue(isDeleted);
        verify(newsDao, times(1)).deleteNewsById(anyLong());
    }

    @Test
    @Order(7)
    public void deleteById_ThrowIllegalArgumentExceptionTest() {
        // precondition
        String exceptionDescription = "These news cannot be found";
        when(newsDao.deleteNewsById(anyLong())).thenReturn(0);

        // action
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> newsService.deleteNews(completeNews.getId())
        );

        // verification
        Assertions.assertEquals(exceptionDescription, exception.getMessage());
    }

    @Test
    @Order(8)
    public void deleteByEntityTest() {
        // precondition
        when(newsDao.deleteNewsById(1L)).thenReturn(1);

        // action
        boolean isDeleted = newsService.deleteNews(completeNews);

        // verification
        Assertions.assertTrue(isDeleted);
    }

    @Test
    @Order(9)
    public void deleteByUrlTest() {
        // precondition
        when(newsDao.deleteNewsByUrl(completeNews.getUrl())).thenReturn(1);

        // action
        boolean isDeleted = newsService.deleteNews(completeNews.getUrl());

        // verification
        Assertions.assertTrue(isDeleted);
    }

    @Test
    @Order(10)
    public void getAllNewsTest() {
        // precondition
        News news2 = new News(2L, "headline2", null, "url2", false);
        when(newsDao.findAll()).thenReturn(List.of(completeNews, news2));

        // action
        List<News> newsList = newsService.getAllNews();

        // verification
        Assertions.assertNotNull(newsList);
        Assertions.assertEquals(2, newsList.size());
        Assertions.assertEquals(completeNews.getHeadline(), newsList.getFirst().getHeadline());
        Assertions.assertEquals(news2.getHeadline(), newsList.getLast().getHeadline());
    }

    @Test
    @Order(11)
    public void findByIdTest_success() {
        // precondition
        when(newsDao.findById(1L)).thenReturn(Optional.of(completeNews));

        // action
        Optional<News> news = newsService.findById(1L);

        // verification
        Assertions.assertTrue(news.isPresent());
        Assertions.assertEquals(completeNews.getHeadline(), news.get().getHeadline());
    }

    @Test
    @Order(12)
    public void findByIdTest_EmptyNews() {
        //precondition
        when(newsDao.findById(10L)).thenReturn(Optional.empty());

        // action
        Optional<News> news = newsService.findById(10L);

        // verification
        Assertions.assertFalse(news.isPresent());
    }

    @Test
    @Order(13)
    public void updateNewsTest() {
        // precondition
        when(newsDao.findById(1L)).thenReturn(Optional.of(completeNews));
        News newsUpdate = new News(1L, "headline2", null, "url2", false);
        when(newsDao.save(newsUpdate)).thenReturn(newsUpdate);

        // action
        News news = newsService.updateNews(newsUpdate);

        // verification
        Assertions.assertNotNull(news);
        Assertions.assertEquals(newsUpdate.getHeadline(), news.getHeadline());
        Assertions.assertEquals(newsUpdate.getUrl(), news.getUrl());
        Assertions.assertEquals(newsUpdate.getId(), news.getId());
    }

    @Test
    @Order(14)
    public void updateNews_ThrowIllegalArgumentExceptionTest() {
        // precondition
        String exceptionDescription = "News Id cannot be found";
        when(newsDao.findById(1L)).thenReturn(Optional.empty());

        // action
        ElementDoesNotExistException exception = Assertions.assertThrows(
                ElementDoesNotExistException.class, () -> newsService.updateNews(completeNews)
        );

        // verification
        Assertions.assertEquals(exceptionDescription, exception.getMessage());
    }

    @Test
    @Order(15)
    public void findByUrl_NullStringTest() {
        // precondition
        String exceptionDescription = "url cannot be null or empty";

        // action
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> newsService.findByUrl("null")
        );

        // verify
        Assertions.assertEquals(exceptionDescription, exception.getMessage());
    }
}
