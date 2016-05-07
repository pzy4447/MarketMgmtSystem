package entity;

public class User {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private int id;// 每次从数据库中查询后填充
	private String name;
	private String password;
	private UserType userType;

	public User(String name, String password, UserType userType) {
		this.name = name;
		this.password = password;
		this.userType = userType;
	}

	public User(Integer id, String name, String password, UserType userType) {
		this(name, password, userType);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ ", userType=" + userType + "]";
	}

}
