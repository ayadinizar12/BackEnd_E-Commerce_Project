package com.sid.demo.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CartDTO {

	private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private List<CartltemDTO> cartltems;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<CartltemDTO> getCartltems() {
		return cartltems;
	}
	public void setCartltems(List<CartltemDTO> cartltems) {
		this.cartltems = cartltems;
	}
    
}
