package com.kosa.mango3.review;

import com.kosa.mango3.customer.CustomerDTO;
import com.kosa.mango3.store.StoreDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	private Long reviewId;
	private Integer grade;
	private String comment;
	private String regdate;
	private CustomerDTO customerDTO;
	private StoreDTO storeDTO;
}
