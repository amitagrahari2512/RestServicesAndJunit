package com.learn.interview.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.interview.entity.Movie;
import com.learn.interview.exception.NotFoundException;
import com.learn.interview.exception.ValueNotPresentException;
import com.learn.interview.repository.MovieRepository;

@Service
public class MovieService {
	
	@Autowired
	public MovieRepository movieRepository;

		public Movie createMovie(Movie movie) {
			if(movie != null) {
				return movieRepository.save(movie);
			}
			throw new ValueNotPresentException("Null Value");
		}
		
		public Movie updateMovie( int id, Movie movie) {
			if(movie != null) {
				Optional<Movie> movieFromDB = movieRepository.findById(id);
				if(movieFromDB.isPresent()) {
					Movie m = movieFromDB.get();
					m.setTitle(movie.getTitle());
					m.setReleaseYear(movie.getReleaseYear());
					return movieRepository.save(m);
				}
				throw new NotFoundException("No movie present for ID " + id);
			}
			throw new ValueNotPresentException("Null Value");
		}
		
		public void deleteMovie(int id) {
			movieRepository.deleteById(id);
		}
		
		public Movie getMovie(int id) {
				Optional<Movie> movieFromDB = movieRepository.findById(id);
				if(movieFromDB.isPresent()) {
					return movieFromDB.get();
				}
				throw new NotFoundException("No movie present for ID " + id);
			}
		
		public List<Movie> getMovieByTitle(String title) {
			List<Movie> movieList = movieRepository.findMovieByTitle(title);
			return movieList;
		}
		
		public List<Movie> getMovieByYear(String year) {
			List<Movie> movieList = movieRepository.findMovieByReleaseYear(year);
			return movieList;
		}
		
		
		public List<Movie> getMovieByTitleAndReleaseYear(String title, String year) {
			List<Movie> movieList = movieRepository.findMovieByTitleAndReleaseYear(title, year);
			return movieList;
		}
		
		
		
		
}
