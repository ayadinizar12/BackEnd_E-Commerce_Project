package com.sid.demo.dtos;

import java.math.BigDecimal;

import com.stripe.param.ChargeCreateParams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentDTO {

	private String tokenId;
    private BigDecimal amount;
    private String currency;
    private Object metadata;

  
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Object getMetadata() {
		return metadata;
	}

	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public PaymentDTO(String tokenId, BigDecimal amount, String currency, Object metadata) {
		this.tokenId = tokenId;
		this.amount = amount;
		this.currency = currency;
		this.metadata = metadata;
	}

	public ChargeCreateParams toChargeParams() {
        return new ChargeCreateParams.Builder()
                .setAmount(amount.multiply(new BigDecimal("100")).longValue())
                .setCurrency(currency)
                .setDescription("Payment from Stripe Checkout")
                .setSource(tokenId)
                .build();
    }
}
