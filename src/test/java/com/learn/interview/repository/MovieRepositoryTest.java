package com.learn.interview.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.learn.interview.entity.Movie;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Optional
//Use this only if you want to use your real DB (optional).
public class MovieRepositoryTest {

	@Autowired
	private MovieRepository movieRepository;
	
	@Test
	void testSaveAndFindById() {
		//Arrange
		Movie m = new Movie();
		m.setTitle("wow");
		m.setReleaseYear("2020");
		
		//Act
		Movie saveMovie = movieRepository.save(m);
		Optional<Movie> foundMovie = movieRepository.findById(saveMovie.getId());
		
		assertTrue(foundMovie.isPresent());
		assertEquals("wow", foundMovie.get().getTitle());
	}
	
	
	@Test
	void testFindMovieByTitle() {
		// Arrange
		Movie m1 = new Movie();
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setTitle("wow");
		m2.setReleaseYear("2021");
		
		//Act
		movieRepository.saveAll(List.of(m1,m2));
		List<Movie> list = movieRepository.findMovieByTitle("wow");
		
		assertEquals(2, list.size());
		assertTrue(list.stream().allMatch(m -> m.getTitle().equals("wow")));
		
	}
	
	@Test
	void deleteById() {

		// Arrange
		Movie m1 = new Movie();
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie saveMovie = movieRepository.save(m1);
		movieRepository.deleteById(saveMovie.getId());
		
		Optional<Movie> findMovie = movieRepository.findById(saveMovie.getId());
		assertFalse(findMovie.isPresent());
		

	}
	
	
}
