package team3;

public interface BookAdminGuiHandler {
	//로그인
	String getloginId();
	String getloginPw();
	void setLoginMsg(String Message);
	void setMenubar(boolean login_info);
	void setLoginStatus(boolean st);
	//회원가입
	String getAdminId();
	String getAdminPw();
	String getAdminKey();
	void setAdminId(String id);
	void setAdminPw(String pw);
	void setAdminKey(String key);
	void setAdminAddMsg(String Message);
	
	// 회원 등록
	String getUserAddName();
	String getUserAddTel();
	String getUserAddAddr();
	String getUserAddBirth();
	void setUserAddName(String name);
	void setUserAddTel(String tel);
	void setUserAddAddr(String addr);
	void setUserAddBirth(String birth);
	void setUserAddMsg(String message);
	void setUserList(String userList);
	
	// 회원 정보 수정
	String getUserUpdateId();

	String getUserUpdateName();

	String getUserUpdateTel();

	String getUserUpdateAddr();

	String getUserUpdateBirth();

	void setUserUpdateId(String id);

	void setUserUpdateName(String name);

	void setUserUpdateAddr(String addr);

	void setUserUpdateTel(String tel);

	void setUserUpdateBirth(String birth);

	void setUserUpdateMsg(String message);

	void setUserUpdateList(String userList);
	
	// 책 등록
	String getBookAddGenre();
	String getBookAddTitle();
	String getBookAddAuthor();
	String getBookAddPublisher();
	void setBookAddTitle(String title);
	void setBookAddGenre(String genre);
	void setBookAddAuthor(String author);
	void setBookAddPublisher(String publisher);
	void setBookAddMsg(String message);
	void setBookAddList(String bookList);
	
	// 책 삭제 
	String getBookDeleteId();
	void setBookDeleteId(String id);
	void setBookDeleteMsg(String message);
	void setBookDeleteList(String bookList);
	
	// 책 대여 
	String getBookLendPid();
	String getBookLendBid();
	void setBookLendPid(String pid);
	void setBookLendBid(String bid);
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