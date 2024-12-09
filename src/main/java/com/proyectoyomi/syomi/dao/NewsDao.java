package com.proyectoyomi.syomi.dao;

import com.proyectoyomi.syomi.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NewsDao extends JpaRepository<News, Long> {

    News findByUrl(String url);
    List<News> findByReviewed(boolean reviewed);
    long deleteById(long id);
    long deleteByUrl(String url);
}
