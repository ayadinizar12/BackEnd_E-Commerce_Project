package com.sid.demo.mappers;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sid.demo.dtos.CartDTO;
import com.sid.demo.dtos.CartltemDTO;
import com.sid.demo.models.Cart;
import com.sid.demo.models.Cartltem;

@Mapper
public interface CartMapper {

	 CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
	 
	 @Mapping(target = "id", source = "cart.id")
	 @Mapping(target = "cartltems", source = "cartltems")
	 CartDTO cartToCartDTO(Cart cart,BigDecimal totalPrice,List<CartltemDTO> cartltems);

	 @Mapping(target = "subTotal", expression = "java(cartltem.getSubTotal())")
     CartltemDTO cartltemToCartltemDTO(Cartltem cartltem);
	 

}

