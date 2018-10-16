package cameronmorales.MovieRecommendationSite;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import cameronmorales.MovieRecommendationSite.MovieRecommendationService;
import cameronmorales.MovieRecommendationSite.UserService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for MovieRecommendationService.
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieRecommendationServiceTest {
	private UserService userService;
	private MovieRecommendationService movieService;

	@Before
	public void setup(){
		//Create MovieRecommendationService
		movieService = new MovieRecommendationService();

		//Create mock UserService
		userService = mock(UserService.class);

		//Inject mock services into MovieRecommendationSite
		movieService.setUserService(userService);
	}

	//Test for hard-coded age being returned from userService
    @Test
    public void getAgeTest() throws Exception {
       	when(userService.getAge()).thenReturn(12);
        assertEquals(userService.getAge(), 12);
        verify(userService).getAge();
    }

    //Test for correct movies returned from MovieRecommendationService
	@Test
	public void ratedGmoviesTest() throws Exception {
		/*
		 * when:
		 * 		UserService would provide behavior for <12 year old users
		 * assertEqauls:
		 * 		Films returned are for the correct age group
		 * verify:
		 * 		Verify call to UserService was made
		 * */
	    List<String> movies = Arrays.asList("Shrek", "Coco", "The Incredibles");
	    when(userService.getAge()).thenReturn(12);
	    assertEquals(movieService.getRecommendedMovies(), movies);
	    verify(userService).getAge();
	}

	@Test
	public void ratedPG13moviesTest() throws Exception {
		/*
		 * when:
		 * 		UserService would provide behavior for 14 year old users
		 * assertEqauls:
		 * 		Films returned are for the correct age group
		 * verify:
		 * 		Verify call to UserService was made
		 * */
	    List<String> movies = Arrays.asList("The Avengers", "The Dark Knight", "Inception");
	    when(userService.getAge()).thenReturn(14);
	    assertEquals(movieService.getRecommendedMovies(), movies);
	    verify(userService).getAge();
	}

	@Test
	public void ratedRmoviesTest() throws Exception {
		/*
		 * when:
		 * 		UserService would provide behavior for 21 year old users
		 * assertEqauls:
		 * 		Films returned are for the correct age group
		 * verify:
		 * 		Verify call to UserService was made
		 * */
	    List<String> movies = Arrays.asList("The Godfather", "Deadpool", "Saving Private Ryan");
	    when(userService.getAge()).thenReturn(21);
	    assertEquals(movieService.getRecommendedMovies(), movies);
	    verify(userService).getAge();
	}

	@Test
	public void movieServiceFallbackTest() throws Exception {
		/*
		 * when:
		 * 		UserService call from MovieRecommendationService responds with an Exception
		 * assertEqauls:
		 * 		Films returned are for the default age group <13
		 * verify:
		 * 		Verify call to UserService was made
		 * */
		List<String> movies = Arrays.asList("Shrek", "Coco", "The Incredibles");
		when(userService.getAge()).thenThrow(Exception.class);
		assertEquals(movieService.execute(), movies);
		verify(userService).getAge();
	}

	@Test
	public void timeOutFallbackTest() throws Exception {
		/*
		 * when:
		 * 		UserService has a timeout of > 100 ms
		 * assertEqauls:
		 * 		Films returned are for the default age group <13
		 * verify:
		 * 		Verify call to UserService was made
		 * */
		List<String> movies = Arrays.asList("Shrek", "Coco", "The Incredibles");
		when(userService.getAge()).thenAnswer(new Answer<Integer>() {
			   public Integer answer(InvocationOnMock invocation) throws InterruptedException{
			     Thread.sleep(5000);
			     return 21; 
			   }
			});
		assertEquals(movieService.execute(), movies);
		verify(userService).getAge();
	}
}
