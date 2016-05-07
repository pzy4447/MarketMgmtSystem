package entity;

public class GoodsOrder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	int id;
	float price;
	String dateString;
	int saleManId;

	public GoodsOrder(float price, int saleManId, String dateString) {
		this.price = price;
		this.dateString = dateString;
		this.saleManId = saleManId;
	}

	public GoodsOrder(int id, float price, int saleManId, String dateString) {
		this(price, saleManId, dateString);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public int getSaleManId() {
		return saleManId;
	}

	public void setSaleManId(int saleManId) {
		this.saleManId = saleManId;
	}

	@Override
	public String toString() {
		return "GoodsOrder [id=" + id + ", price=" + price + ", dateString="
				+ dateString + ", saleManId=" + saleManId + "]";
	}

}
