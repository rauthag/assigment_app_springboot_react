package com.rauthagproject.assigmentapp.domain;

import jakarta.persistence.*;


@Entity
public class Assigment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String status;
    private String githubUrl;
    private String branch;
    private String codeReviewUrl;

    @ManyToOne(optional = false) //You cant have assigment without user
    private User user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCodeReviewUrl() {
        return codeReviewUrl;
    }

    public void setCodeReviewUrl(String codeReviewUrl) {
        this.codeReviewUrl = codeReviewUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
