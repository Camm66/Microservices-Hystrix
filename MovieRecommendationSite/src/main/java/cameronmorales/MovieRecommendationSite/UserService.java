package cameronmorales.MovieRecommendationSite;

public class UserService implements IUserService {
	private int userAge;

	public UserService(){}

	public int getAge() throws Exception {
		return this.userAge;
	}
}
