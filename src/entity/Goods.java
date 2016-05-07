package entity;

/**
 * @author lenovo 商品实体类
 */
public class Goods {

	private int id;// 每次从数据库中查询后填充
	private String name;
	private float price;
	private int number;

	public Goods(String name, float price, int number) {
		this.name = name;
		this.price = price;
		this.number = number;
	}

	public Goods(Integer id, String name, float price, int number) {
		this(name, price, number);
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", price=" + price
				+ ", number=" + number + "]";
	}

}
