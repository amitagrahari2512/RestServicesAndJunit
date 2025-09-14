package com.learn.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.interview.entity.Movie;
import com.learn.interview.service.MovieService;

@RestController
@RequestMapping("/movie/")
public class MovieController {
	//Create, read, update, and delete an individual movie.
	//Search for movies by their title and release year.
	
	@Autowired
	public MovieService movieService;
	
	@PostMapping
	public ResponseEntity<Object> createMovie(@RequestBody Movie movie) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateMovie(@PathVariable("id") int id, @RequestBody Movie movie) {
			
		return ResponseEntity.status(HttpStatus.OK).body((movieService.updateMovie(id, movie)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteMovie(@PathVariable("id") int id) {
		movieService.deleteMovie(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping("id/{id}")
	public ResponseEntity<Object> getMovie(@PathVariable("id") int id) {
		return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovie(id));
	}
	
	@GetMapping("search/title/{title}")
	public ResponseEntity<Object> getMovieByTitle(@PathVariable("title") String title) {
		return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieByTitle(title));
	}
	
	@GetMapping("search/year/{year}")
	public ResponseEntity<Object> getMovieByYear(@PathVariable("year") String year) {
		
		return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieByYear(year));
	}
	
	@GetMapping("search")
	public ResponseEntity<Object> getMovieByTitleAndYear(@RequestParam(required = true) String title, @RequestParam(required = true) String year) {
		
		return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieByTitleAndReleaseYear(title, year));
	}
}
