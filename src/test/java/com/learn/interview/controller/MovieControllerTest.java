package com.learn.interview.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.learn.interview.controller.MovieController;
import com.learn.interview.entity.Movie;
import com.learn.interview.exception.NotFoundException;
import com.learn.interview.exception.ValueNotPresentException;
import com.learn.interview.service.MovieService;

@AutoConfigureMockMvc
@WebMvcTest(MovieController.class)
//@WebMvcTest - Loads only the web layer (Controller, Jackson, validation etc.)
public class MovieControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	//@MockBean -Mocks the service layer dependency
	@MockBean
	private MovieService movieService;
	
	@Test
	void createRequest() throws Exception {
		Movie movie =new Movie();
		movie.setTitle("wow");
		movie.setReleaseYear("2020");
		
		when(movieService.createMovie(any(Movie.class))).thenReturn(movie);
		
		mockMvc.perform(post("/movie/")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\": \"wow\", \"releaseYear\": \"2020\"}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("wow"));
		
		
		verify(movieService).createMovie(any(Movie.class));
	}
	
	@Test
	void createRequestValueNotPresent() throws Exception {
		
		when(movieService.createMovie(any(Movie.class))).thenThrow(new ValueNotPresentException("Null Value"));
		
		
		mockMvc.perform(post("/movie/")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\": \"\", \"releaseYear\": \"\"}"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Null Value"));
		
		verify(movieService).createMovie(any(Movie.class));
	}
	
	@Test
	void testUpdateMovie() throws Exception {
		//Arrange
		int id = 1;
		
		Movie updatedMovie =new Movie();
		updatedMovie.setId(1);
		updatedMovie.setTitle("wowUpdate");
		updatedMovie.setReleaseYear("2020");
	
		when(movieService.updateMovie(eq(id), any(Movie.class))).thenReturn(updatedMovie);
		
		//Act and Assert
		
		mockMvc.perform(put("/movie/{id}",id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\" : \"wowUpdate\" , \"releaseYear\":\"2020\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("wowUpdate"));
		
		verify(movieService).updateMovie(eq(id), any(Movie.class));
	}
	
	@Test
	void testUpdateMovieNotFoundException() throws Exception {
		// Arrange
		int id = 100;

		when(movieService.updateMovie(eq(id), any(Movie.class))).thenThrow(new NotFoundException("No movie present for ID 100"));	
		
		//Act and assert
		
		mockMvc.perform(put("/movie/{id}",id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\" : \"wowUpdate\" , \"releaseYear\":\"2020\"}"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("No movie present for ID 100"));
		
		verify(movieService).updateMovie(eq(id), any(Movie.class));
		
		
	}
	
	@Test
	void testUpdateMovieValueNotPresentException() throws Exception {
		// Arrange
		int id = 100;

		when(movieService.updateMovie(eq(id), any(Movie.class))).thenThrow(new ValueNotPresentException("Null Value"));	
		
		//Act and assert
		
		mockMvc.perform(put("/movie/{id}",id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\" : \"\" , \"releaseYear\":\"\"}"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Null Value"));
		
		verify(movieService).updateMovie(eq(id), any(Movie.class));
		
	}
	
	@Test
	void testGetMovie() throws Exception {
		//Arrange
		Movie m =new Movie();
		m.setId(1);
		m.setTitle("wow");
		m.setReleaseYear("2019");
		
		when(movieService.getMovie(1)).thenReturn(m);
		
		//Act and Assert
		mockMvc.perform(get("/movie/id/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("wow"));
		
		verify(movieService,times(1)).getMovie(1);
	}
	
	@Test
	void testGetMovieNotFoundException() throws Exception {
		//Arrange
		when(movieService.getMovie(100)).thenThrow(new NotFoundException("No movie present for ID 100"));
		
		//Act and Assert
		mockMvc.perform(get("/movie/id/100"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("No movie present for ID 100"));
		
		verify(movieService).getMovie(100);
	}
	
	
	@Test
	void testDeleteMovie() throws Exception {
		
		doNothing().when(movieService).deleteMovie(1);
		
		mockMvc.perform(delete("/movie/1"))
			.andExpect(status().isNoContent());

		verify(movieService,times(1)).deleteMovie(1);
	}
	
	@Test
	void testGetMovieByTitle() throws Exception {
		String title = "wow";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("wow");
		m2.setReleaseYear("2021");
		
		List<Movie> list =List.of(m1, m2);
		
		when(movieService.getMovieByTitle(eq(title))).thenReturn(list);
		
		mockMvc.perform(get("/movie/search/title/{title}", title))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$[0].title").value("wow"))
			.andExpect(jsonPath("$[0].releaseYear").value("2020"))
			.andExpect(jsonPath("$[1].title").value("wow"))
			.andExpect(jsonPath("$[1].releaseYear").value("2021"));
		
		verify(movieService,times(1)).getMovieByTitle(eq(title));
			
	}
	
	@Test
	void testGetMovieByYear() throws Exception {
		String year = "2020";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("Intel");
		m2.setReleaseYear("2020");
		
		List<Movie> list =List.of(m1, m2);
		
		when(movieService.getMovieByYear(eq(year))).thenReturn(list);
		
		mockMvc.perform(get("/movie/search/year/{year}", year))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$[0].title").value("wow"))
			.andExpect(jsonPath("$[0].releaseYear").value("2020"))
			.andExpect(jsonPath("$[1].title").value("Intel"))
			.andExpect(jsonPath("$[1].releaseYear").value("2020"));
		
		verify(movieService,times(1)).getMovieByYear(eq(year));
			
	}
	
	@Test
	void testGetMovieByYearCheckViaJson() throws Exception {
		String year = "2020";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("Intel");
		m2.setReleaseYear("2020");
		
		List<Movie> list =List.of(m1, m2);
		
		when(movieService.getMovieByYear(eq(year))).thenReturn(list);
		
		String expectedJson = """ 
								[
								{"id":1, "title":"wow", "releaseYear":"2020"},
				      			{"id":2, "title":"Intel", "releaseYear":"2020"}
								]
				""";
		
		mockMvc.perform(get("/movie/search/year/{year}", year))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
		
		verify(movieService,times(1)).getMovieByYear(eq(year));
			
	}
	
	@Test
	void testGetMovieByTitleAndYear() throws Exception {
		String year = "2020";
		String title = "wow";
		
		Movie m1 = new Movie();
		m1.setId(1);
		m1.setTitle("wow");
		m1.setReleaseYear("2020");
		
		Movie m2 = new Movie();
		m2.setId(2);
		m2.setTitle("wow");
		m2.setReleaseYear("2019");
		
		List<Movie> list =List.of(m1);
		
		when(movieService.getMovieByTitleAndReleaseYear(title, year)).thenReturn(list);
		
		
		String jsonExpected = """
				[
					{"id":1, "title":"wow", "releaseYear":"2020"}
				]
				""";
		
		mockMvc.perform(get("/movie/search")
				.param("title", "wow")
				.param("year", "2020"))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonExpected));
		
		verify(movieService,times(1)).getMovieByTitleAndReleaseYear(eq(title), eq(year));
	}
	
	
	
}
