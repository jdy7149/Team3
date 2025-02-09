package team3;

public interface BookAdminGuiHandler {
	// 회원 등록
	String getUserAddName();
	String getUserAddTel();
	String getUserAddAddr();
	String getUserAddBirth();
	void setUserAddMsg(String message);
	void setUserList(String userList);
	
	// 책 등록
	String getBookAddGenre();
	String getBookAddtitle();
	String getBookAddAuthor();
	String getBookAddPublisher();
	void setBookAddMsg(String message);
	void setBookAddList(String bookList);
	
	// 책 삭제 
	String getBookDeleteId();
	void setBookDeleteMsg(String message);
	void setBookDeleteList(String bookList);
	
	// 책 대여 
	String getBookLendPid();
	String getBookLendBid();
	void setBookLendMsg(String message);
	void setBookLendList(String bookList);
	
	// 검색하기
	String getSearchInfoKey();
	boolean getBookSearchState();
	boolean getPersonSearchState();
	void setSearchResult(String result);
	void appendSearchResult(String result);
	
	// 연체 정보
	void addDelayInfo(String delayInfo);
	
	// 탑 텐
	void addTopTenRank(String rank);
	
	// DB 연동 기능 탑재
	void setFeatures(BookAdminIF features);
	
}
