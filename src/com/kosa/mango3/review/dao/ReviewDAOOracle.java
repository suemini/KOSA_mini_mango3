package com.kosa.mango3.review.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kosa.mango3.customer.dto.CustomerDTO;
import com.kosa.mango3.db.Oracle;
import com.kosa.mango3.exception.FindException;
import com.kosa.mango3.review.dto.ReviewDTO;
import com.kosa.mango3.store.dto.StoreDTO;

public class ReviewDAOOracle implements ReviewDAO {
//	private List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();	

	Oracle oc = new Oracle();
	Connection conn = null;
	
	@Override
	public List<ReviewDTO> selectByStoreNo(long storeId) throws FindException {
		List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();
		conn = oc.DBConnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectSQL = "SELECT rownum, grade, rw_content, login_id, regdate" 
				+ " FROM review"
				+ " WHERE store_id =?"
				+ " ORDER BY regdate DESC";
		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setLong(1, storeId);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				Integer grade = rs.getInt("grade");
				String comment = rs.getString("rw_content");
				String loginId = rs.getString("login_id");
				Date regdate = rs.getDate("regdate");		
				
				CustomerDTO customerDTO = CustomerDTO.builder().loginId(loginId).build();			
				ReviewDTO rwDTO = ReviewDTO.builder()
									.grade(grade)
									.comment(comment)
									.regdate(regdate)
									.customerDTO(customerDTO)
									.build();					
			 
				reviewList.add(rwDTO);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			} 
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return reviewList;
	}
		
	@Override
	public List<ReviewDTO> selectByGrade(long storeId, int grade) throws FindException {
		List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();
		
		conn = oc.DBConnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectSQL = "SELECT rownum, grade, rw_content, login_id, regdate" 
				+ " FROM review"
				+ " WHERE store_id =? and grade=?"
				+ " ORDER BY regdate DESC";
		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setLong(1, storeId);
			pstmt.setInt(2, grade);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				Integer grades = rs.getInt("grade");
				String comment = rs.getString("rw_content");
				String loginId = rs.getString("login_id");
				Date regdate = rs.getDate("regdate");		
				
				CustomerDTO customerDTO = CustomerDTO.builder().loginId(loginId).build();			
				ReviewDTO rwDTO = ReviewDTO.builder()
									.grade(grades)
									.comment(comment)
									.regdate(regdate)
									.customerDTO(customerDTO)
									.build();			
			 
				reviewList.add(rwDTO);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			} 
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return reviewList;
	}

	public void create(ReviewDTO reviewDTO, String loginId) {
		conn = oc.DBConnect();
		
//		CustomerDAO cd = new CustomerDAO();
//		if(cd.login() == -1) {
//			ReviewService.reviewMenu();
//			return;
//		}
		
		PreparedStatement pstmt = null;
		String insertSQL = "INSERT INTO \"MANGO3\".\"REVIEW\" (REVIEW_ID, GRADE, RW_CONTENT, STORE_ID, LOGIN_ID) "
							+ "VALUES (REVIEW_SEQ.NEXTVAL, ?, ?, ?, ?)";
			
			try {
				 	pstmt = conn.prepareStatement(insertSQL);
					pstmt.setInt(1, reviewDTO.getGrade());
					pstmt.setString(2, reviewDTO.getComment());
					pstmt.setLong(3, reviewDTO.getStoreDTO().getStoreId());
					pstmt.setString(4, reviewDTO.getCustomerDTO().getLoginId());

					int rowcnt = pstmt.executeUpdate();	// 반환값이 int 타입,
					System.out.println(rowcnt + "건 추가 성공");
					//conn.commit(); auto commit 끄는법
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
					}
				}			
				if(conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		
	@Override
	public void delete(long reviewId) {
		conn = oc.DBConnect();
		
		/*
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/

		PreparedStatement pstmt = null;		
		String deleteMyReview = "DELETE review WHERE review_id = ?";
		
		try {
			pstmt = conn.prepareStatement(deleteMyReview);
			pstmt.setLong(1, reviewId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	@Override
	public List<ReviewDTO> selectByCustomer(String loginId, int page) {
		
		List<ReviewDTO> reviewList = new ArrayList<>();
		int pageSize = 5;
		
		conn = oc.DBConnect();

		/*
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPwd());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectMyReview = "SELECT r.review_id, s.store_name, r.grade, r.rw_content, TO_CHAR(r.regdate) regdate"
							  + "FROM review r JOIN store s ON r.store_id = s.store_id"
							  + "WHERE login_id = ?";
		
		String mySqlTest = "SELECT r.review_id, s.store_name, r.grade, r.rw_content, created_at\r\n"
						 + "FROM review r JOIN store s ON r.store_id = s.store_id\r\n"
						 + "WHERE r.login_id = ?";
		
		String oraclePaging = "SELECT rn, review_id, store_name, grade, rw_content, regdate\r\n"
					  		+ "FROM (SELECT ROWNUM rn, a.*\r\n"
					  		+ "      FROM (SELECT r.review_id, s.store_name, r.grade, r.rw_content, TO_CHAR(r.regdate) regdate\r\n"
					  		+ "            FROM review r JOIN store s ON r.store_id = s.store_id\r\n"
					  		+ "            WHERE login_id = ?) a\r\n"
					  		+ "     )\r\n"
					  		+ "WHERE rn BETWEEN ? AND ?";

		String mySqlPaging = "SELECT rn, review_id, store_name, grade, rw_content, regdate\r\n"
				  		   + "FROM (SELECT @ROWNUM := @ROWNUM + 1 AS rn, a.*\r\n"
				  		   + "      FROM (SELECT r.review_id, s.store_name, r.grade, r.rw_content, created_at AS regdate\r\n"
				  		   + "            FROM review r JOIN store s ON r.store_id = s.store_id\r\n"
				  		   + "            WHERE r.login_id = ?) a\r\n"
				  		   + "     )\r\n"
				  		   + "WHERE rn BETWEEN ? AND ?";
		
		try {
			pstmt = conn.prepareStatement(mySqlPaging);
			pstmt.setString(1, loginId);
			pstmt.setInt(2, pageSize*(page-1)+1);
			pstmt.setInt(3, pageSize*page);
			rs = pstmt.executeQuery();

			while (rs.next()) {
	
				StoreDTO s = StoreDTO.builder()			
							.storeName(rs.getString("store_name"))
							.build();
				
				ReviewDTO r = ReviewDTO.builder()
						.reviewId(rs.getLong("review_id"))
						.grade(rs.getInt("grade"))
						.comment(rs.getString("rw_content"))
						.regdate(rs.getDate("created_at"))
//						.regdate(rs.getString("created_at"))
//						.regdate(rs.getString("regdate"))
						.storeDTO(s)
						.build();

				reviewList.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		return reviewList;
	}


		

 	
}
