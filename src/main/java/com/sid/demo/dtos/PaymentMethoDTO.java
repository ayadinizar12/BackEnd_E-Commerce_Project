package com.sid.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentMethoDTO {

	private String type;
    private CardDTO card;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CardDTO getCard() {
		return card;
	}
	public void setCard(CardDTO card) {
		this.card = card;
	}
    
    
}
