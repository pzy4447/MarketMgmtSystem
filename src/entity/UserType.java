package entity;

public enum UserType {
	ALL, SALESMAN, MANAGER;

	public static void main(String args[]) {
		UserType tp = UserType.valueOf("MANAGER");
		System.out.printf("%d%n", tp.ordinal());
	}

	public static UserType valueOf(int i) {
		return UserType.values()[i];
	}
}
