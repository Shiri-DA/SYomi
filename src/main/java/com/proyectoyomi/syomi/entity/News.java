package com.proyectoyomi.syomi.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String headline;
    private Date creationDate;
    @Column(nullable = false, unique = true)
    private String url;
    private Boolean reviewed;

    // TODO one2many relationship with media outlet
    // --- CONSTRUCTORS ---
    // All args
    public News(String headline, Date creationDate,
                String url, Boolean reviewed) {
        this.headline = headline;
        this.creationDate = creationDate;
        this.url = url;
        this.reviewed = reviewed;
    }

    //No args
    public News() {}

    // Without reviewed, it automatically puts it in No
    public News(String headline, Date creationDate,
                String url) {
        this.headline = headline;
        this.creationDate = creationDate;
        this.url = url;
        this.reviewed = false;
    }

    // --- SETTERS AND GETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }
}
