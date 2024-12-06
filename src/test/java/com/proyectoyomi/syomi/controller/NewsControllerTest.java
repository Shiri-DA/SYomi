package com.proyectoyomi.syomi.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoyomi.syomi.configuration.JwtRequestFilter;
import com.proyectoyomi.syomi.entity.News;
import com.proyectoyomi.syomi.service.NewsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NewsController.class)
@AutoConfigureMockMvc
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
    @WithMockUser(value = "admin")
    public void createNewsTest() throws Exception {
        // precondition
        given(newsService.addNews(any(News.class))).willReturn(news);

        // action
        ResultActions response = mockMvc.perform(post("/news")
                        .with(csrf())
                        .with(user("ADMIN").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(news))
        );

        // verify
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.headline").value(news.getHeadline()))
                .andExpect(jsonPath("$.url").value(news.getUrl()));
    }
}
