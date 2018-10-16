package cameronmorales.MovieRecommendationSite;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class MovieRecommendationService extends HystrixCommand<List<String>> implements IMovieRecommendationService{
	private IUserService userService;

	private List<String> underAge13 = Arrays.asList("Shrek", "Coco", "The Incredibles");
	private List<String> betweenAge17and13 = Arrays.asList("The Avengers", "The Dark Knight", "Inception");
	private List<String> overAge17 = Arrays.asList("The Godfather", "Deadpool", "Saving Private Ryan");

	public MovieRecommendationService() {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MovieServices"))
			  .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
			  .withExecutionTimeoutInMilliseconds(100)));
	}

	public void setUserService(IUserService userService){
		this.userService = userService;
	}

	public List<String> getRecommendedMovies() throws Exception{
		/*
		 * Method:
		 * 		Finds film recommendations based on age returned from UserService.testAge();
		 * params:
		 * 		int userAge: User age
		 * return:
		 * 		List<String> underAge13 || betweenAge17and13 || overAge17 : Returns the correct recommendation
		 **/
		int userAge = userService.getAge();
		if(userAge < 13)
			return this.underAge13;
		else if(userAge < 17)
			return this.betweenAge17and13;
		else
			return this.overAge17;
	}

	@Override
	protected List<String> run() throws Exception, TimeoutException {
		/*Method:
		 * 		Invoked by controller/client calls to MovieRecommendationService.execute()
		 * Returns:
		 * 		Calls this.getRecommendedMovies()*/
		return getRecommendedMovies();
	}

	@Override
	protected List<String> getFallback() {
		/*Method:
		 * 		Called when this.run() fails,
		 * 		EX) UserService.getAge() returns an Exception
		 *Return:
		 *		List<String> underAge13 : Default age-group movie recommendations
		 **/
		return this.underAge13;
	}
}