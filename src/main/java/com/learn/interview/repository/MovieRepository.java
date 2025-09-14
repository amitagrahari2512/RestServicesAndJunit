package com.learn.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learn.interview.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{
	
	public List<Movie> findMovieByTitle(String title);
	public List<Movie> findMovieByReleaseYear(String year);
	
	@Query("select m from Movie m where m.title = :title and m.releaseYear = :releaseYear")
	public List<Movie> findMovieByTitleAndReleaseYear(@Param("title") String title, @Param("releaseYear") String releaseYear);
}
