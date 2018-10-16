package cameronmorales.MovieRecommendationSite;

import java.util.List;

public interface IMovieRecommendationService {
	public List<String> getRecommendedMovies() throws Exception;
}
