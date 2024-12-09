package com.proyectoyomi.syomi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoyomi.syomi.configuration.JwtRequestFilter;
import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.exception.ElementDoesNotExistException;
import com.proyectoyomi.syomi.exception.ElementExistException;
import com.proyectoyomi.syomi.service.NewsService;
import com.proyectoyomi.syomi.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NewsController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Still there is not replacement for mockbean, check in future
    @SuppressWarnings("removal")
    @MockBean
    private NewsService newsService;

    // Have to mock the filter because the controller uses it and webmvctest
    // does not scan components
    @SuppressWarnings("removal")
    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    News news;
    News news2;

    @BeforeEach
    public void setUp() {
        news = new News(
                1L,
                "Headline 1",
                new Date(),
                "Url 1",
                false
        );

        news2 = new News(
                2L, "Headline 2", null,
                "Url 2", true
        );
    }

    //Post Controller
    @Test
    @Order(1)
    public void createNewsTest() throws Exception {
        // precondition
        News news2 = new News("headline2", null, "url2");
        // Activate only service when recieve a headline like news 2
        when(newsService.addNews(MockitoHamcrest
                .argThat(Matchers
                        .hasProperty(
                                "headline",
                                Matchers.is(news2.getHeadline())))))
                .thenReturn(news);

        // action
        ResultActions response = mockMvc.perform(post("/news")
              .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(news2))
        );

        // verify
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.headline").value(news.getHeadline()))
                .andExpect(jsonPath("$.url").value(news.getUrl()));
    }

    @Test
    @Order(2)
    public void createNews_UrlExistsTest() throws Exception {
        // precondition
        when(newsService.addNews(any(News.class))).thenThrow(
                new ElementExistException("This url already exists")
        );

        // action and verify
        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(news))
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("This url already exists")
            );
    }

    @Test
    @Order(3)
    public void getAllNewsTest() throws Exception {
        // precondition
        when(newsService.getAllNews()).thenReturn(List.of(news, news2));

        // action
        ResultActions response = mockMvc.perform(get("/news"));

        // verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(news.getId()))
                .andExpect(jsonPath("$[0].headline").value(news.getHeadline()))
                .andExpect(jsonPath("$[0].url").value(news.getUrl()))
                .andExpect(jsonPath("$[1].id").value(news2.getId()))
                .andExpect(jsonPath("$[1].headline").value(news2.getHeadline()))
                .andExpect(jsonPath("$[1].url").value(news2.getUrl())
        );
    }

    @Test
    @Order(4)
    public void getByReviewedTest() throws Exception {
        // precondition
        when(newsService.findByReviewed(true)).thenReturn(List.of(news2));
        when(newsService.findByReviewed(false)).thenReturn(List.of(news));

        // action
        ResultActions responseTrue = mockMvc.perform(get("/news/reviewed?reviewed=true"));
        ResultActions responseFalse = mockMvc.perform(get("/news/reviewed?reviewed=false"));

        // verify
        responseTrue.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(news2.getId()))
                .andExpect(jsonPath("$[0].headline").value(news2.getHeadline()))
                .andExpect(jsonPath("$[0].url").value(news2.getUrl()))
                .andExpect(jsonPath("$[0].reviewed").value(true));

        responseFalse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(news.getId()))
                .andExpect(jsonPath("$[0].headline").value(news.getHeadline()))
                .andExpect(jsonPath("$[0].url").value(news.getUrl()))
                .andExpect(jsonPath("$[0].reviewed").value(false));
    }

    @Test
    @Order(5)
    public void getByUrlTest() throws Exception {
        // precondition
        when(newsService.findByUrl(news.getUrl())).thenReturn(news);

        // action
        ResultActions response = mockMvc.perform(get("/news")
                .param("url", news.getUrl()));

        // verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.headline").value(news.getHeadline()))
                .andExpect(jsonPath("$.url").value(news.getUrl()))
                .andExpect(jsonPath("$.reviewed").value(news.getReviewed())
        );
    }

    @Test
    @Order(6)
    public void getByUrl_NullTest() throws Exception {
        // precondition
        when(newsService.findByUrl("null")).
        thenThrow(new IllegalArgumentException("url cannot be null or empty"));

        // action
        ResultActions responseString = mockMvc.perform(get("/news")
                .param("url", "null")
        );

        // verify
        responseString.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("url cannot be null or empty")
            );
    }

    @Test
    @Order(7)
    public void updateNewsTest() throws Exception {
        // precondition
        when(newsService.updateNews(any(News.class))).thenReturn(news);

        // action
        ResultActions response = mockMvc.perform(put("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(news))
        );

        // verify
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.headline").value(news.getHeadline()))
                .andExpect(jsonPath("$.url").value(news.getUrl()))
                .andExpect(jsonPath("$.reviewed").value(news.getReviewed())
            );
    }

    @Test
    @Order(8)
    public void updateNews_IdNotFoundTest() throws Exception {
        // precondition
        when(newsService.updateNews(any(News.class))).thenThrow(
                new ElementDoesNotExistException("News Id cannot be found")
        );

        // action
        ResultActions resultActions = mockMvc.perform(put("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(news))
        );

        // verify
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("News Id cannot be found"));
    }

    @Test
    @Order(9)
    public void deleteNewsTest() throws Exception {
        // precondition
    }
}
