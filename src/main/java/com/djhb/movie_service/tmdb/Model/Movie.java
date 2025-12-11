package com.djhb.movie_service.tmdb.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String director;

    @ElementCollection
    private List<String> actors = new ArrayList<>();
}
