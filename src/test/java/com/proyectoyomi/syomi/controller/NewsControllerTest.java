package com.proyectoyomi.syomi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoyomi.syomi.configuration.JwtRequestFilter;
import com.proyectoyomi.syomi.entity.News;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void setUp() {
        news = new News(
                1L,
                "Headline 1",
                new Date(),
                "Url 1",
                false
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
}
