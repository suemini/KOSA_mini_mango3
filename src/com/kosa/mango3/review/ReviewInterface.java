package com.kosa.mango3.review;

import java.util.List;

public interface ReviewInterface {

	/**
	 * 가게별 리뷰 리스트 조회
	 */
	List<ReviewDTO> selectByStore(int storeId);
//		SELECT * FROM review WHERE storeId = ?
	
	/**
	 * 등급별 리뷰 리스트 조회
	 * @param storeId
	 * @param grade
	 */
	List<ReviewDTO> selectByGrade(int storeId, int grade);
//	SELECT * FROM review WHERE storeId = ? AND grade = ?
	
	/**
	 * 리뷰 쓰기
	 */
	void create();
	
	/**
	 * 리뷰 삭제
	 */
	void delete(int reviewId);
	
	/**
	 * 내 리뷰 리스트 조회
	 */
	List<ReviewDTO> selectByCustomer(String loginId);
	
//	SELECT * FROM review WHERE loginId = ? 
}
