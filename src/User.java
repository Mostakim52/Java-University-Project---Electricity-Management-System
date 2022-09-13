
public class User {
	private String username;
	private String password;
	private String email;
	User(){}
	User(User u1){
		this.username = u1.getUsername();
		this.password = u1.getPassword();
		this.email = u1.getEmail();
	}
	User(String username, String password,String email){
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
