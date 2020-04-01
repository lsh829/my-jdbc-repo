package db.employee;

public class GroupDTO {
	private String year;
	private String month;
	private int sum_pay;
	private int avg_pay;
	private int max_pay;
	
	
	public int getMax_pay() {
		return max_pay;
	}
	public void setMax_pay(int max_pay) {
		this.max_pay = max_pay;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getSum_pay() {
		return sum_pay;
	}
	public void setSum_pay(int sum_pay) {
		this.sum_pay = sum_pay;
	}
	public int getAvg_pay() {
		return avg_pay;
	}
	public void setAvg_pay(int avg_pay) {
		this.avg_pay = avg_pay;
	}
	
	
}
