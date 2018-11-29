package com.purvik.jhipdemo.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SubjectSearchDTO implements Serializable {

    @NotNull
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SubjectSearchDTO{" +
            "title='" + title + '\'' +
            '}';
    }
}
