package tim31.pswisa.dto;

import tim31.pswisa.model.CheckUpType;

public class CheckUpTypeDTO {

	private Long id;
	private String name;
	private double typePrice;

	public CheckUpTypeDTO(CheckUpType c) {
		this(c.getId(), c.getName(), c.getTypePrice());
	}

	public CheckUpTypeDTO(Long id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.typePrice = price;
	}

	public CheckUpTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTypePrice() {
		return typePrice;
	}

	public void setTypePrice(double typePrice) {
		this.typePrice = typePrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}