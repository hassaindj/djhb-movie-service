package com.djhb.movie_service.tmdb.Repository;

import com.djhb.movie_service.tmdb.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
