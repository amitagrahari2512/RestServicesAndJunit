package com.learn.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.learn.interview.entity.Movie;
import com.learn.interview.exception.NotFoundException;
import com.learn.interview.exception.ValueNotPresentException;
import com.learn.interview.repository.MovieRepository;
import com.learn.interview.service.MovieService;

public class MovieServiceTest {

	@InjectMocks
	private MovieService movieService;
	
	@Mock
	private MovieRepository movieRepository;
	
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	public void createMovie() {
		//Arrange
		Movie movieActual = new Movie();
		movieActual.setTitle("wow");
		movieActual.setReleaseYear("2019");
		when(movieRepository.save(movieActual)).thenReturn(movieActual);
		
		//Act
		Movie movieExpected = movieService.createMovie(movieActual);
		
		//Assert
		assertNotNull(movieExpected);
		assertEquals(movieExpected.getReleaseYear(), movieActual.getReleaseYear());
		verify(movieRepository,times(1)).save(movieActual);
	}
	
	@Test
	public void createMovieValueNotPresentException() {
		//Act and Assert
		ValueNotPresentException ex = assertThrows(ValueNotPresentException.class, () -> {
			movieService.createMovie(null);
		});
		
		
		//Assert
		assertEquals(ex.getMessage(), "Null Value");
		verify(movieRepository,never()).save(any());
	}
	
	@Test
	public void updateMovie() {
		
		//Arrange
		int id = 1;
		Movie movieUpdate = new Movie();
		movieUpdate.setTitle("wow");
		movieUpdate.setReleaseYear("2020");
		
		Movie movieDb = new Movie();
		movieDb.setId(1);
		movieDb.setTitle("wow");
		movieDb.setReleaseYear("2019");
		
		when(movieRepository.findById(id)).thenReturn(Optional.of(movieDb));
		when(movieRepository.save(movieDb)).thenReturn(movieDb);
		
		//Act
		
		Movie updatedMovie = movieService.updateMovie(id, movieUpdate);
		
		//Assert
		assertNotNull(updatedMovie);
		assertEquals("2020", updatedMovie.getReleaseYear());
		verify(movieRepository,times(1)).findById(id);
		verify(movieRepository,times(1)).save(movieDb);
	
	}
	
	@Test
	public void updateMovieValueNotPresentException() {
		//Act and Assert
		ValueNotPresentException ex = assertThrows(ValueNotPresentException.class, () -> {
			movieService.updateMovie(1, null);
		});
		
		assertEquals("Null Value", ex.getMessage());
		verify(movieRepository,never()).findById(1);
	}
	
	@Test
	public void updateMovieNotFoundException() {
		//Act
		int id = 1;
		Movie movieUpdate = new Movie();
		movieUpdate.setTitle("wow");
		movieUpdate.setReleaseYear("2020");
		
		when(movieRepository.findById(id)).thenReturn(Optional.empty());
		
		//Assert
		NotFoundException ex = assertThrows(NotFoundException.class, () -> {
			movieService.updateMovie(id, movieUpdate);
		});
		
		assertEquals("No movie present for ID 1", ex.getMessage());
		verify(movieRepository,times(1)).findById(1);
	}
	
	@Test
	public void deleteMovie() {
		int id = 1;
		
		//Act
		movieRepository.deleteById(id);
		
		verify(movieRepository,times(1)).deleteById(id);
		
	}
	
	@Test
	public void getMovieById() {
		// Arrange
		int id = 1;
		Movie movieExpected = new Movie();
		movieExpected.setTitle("wow");
		movieExpected.setReleaseYear("2019");
		when(movieRepository.findById(id)).thenReturn(Optional.of(movieExpected));
		
		//Act
		Movie movieActual = movieService.getMovie(id);
		
		
		//Assert
		assertNotNull(movieExpected);
		assertEquals(movieExpected.getTitle(), movieActual.getTitle());
		verify(movieRepository,times(1)).findById(id);
	}
	
	@Test
	public void getMovieByIdNotFoundException() {
		// Arrange
		int id = 1;
		when(movieRepository.findById(1)).thenReturn(Optional.empty());
		
		//Act
		NotFoundException ex = assertThrows(NotFoundException.class, () -> {
			movieService.getMovie(id);
		});
		
		
		//Assert
		assertEquals(ex.getMessage(), "No movie present for ID 1");
		verify(movieRepository,times(1)).findById(id);
	}
	
	@Test
	public void getMovieByTitle() {
		//Arrange
		String title = "wow";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2019");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("wow");
		m2.setReleaseYear("2020");
		
		List<Movie> listOfMovies = List.of(m1,m2);
		
		when(movieRepository.findMovieByTitle(title)).thenReturn(listOfMovies);
		
		//Act
		List<Movie> list = movieService.getMovieByTitle(title);
		
		//Assert
		
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	@Test
	public void getMovieByYear() {
		//Arrange
		String year = "2020";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow1");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("wow2");
		m2.setReleaseYear("2020");
		
		List<Movie> listOfMovies = List.of(m1,m2);
		
		when(movieRepository.findMovieByReleaseYear(year)).thenReturn(listOfMovies);
		
		//Act
		List<Movie> list = movieService.getMovieByYear(year);
		
		//Assert
		
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	@Test
	public void getMovieByTitleAndYear() {
		// Arrange
		String year = "2020";
		String title = "wow";

		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2019");

		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("wow");
		m2.setReleaseYear("2020");

		List<Movie> listOfMovies = List.of(m1);

		when(movieRepository.findMovieByTitleAndReleaseYear(title, year)).thenReturn(listOfMovies);
		
		//Act
		List<Movie> list = movieService.getMovieByTitleAndReleaseYear(title, year);
		
		assertNotNull(list);
		assertEquals(1, list.size());
	}
}
