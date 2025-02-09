package team3;

public interface BookAdminIF {
	// 회원 등록
	String userList() throws Exception;
	void userAdd() throws Exception;
	
	// 책 등록 및 삭제
	String bookList() throws Exception;
	void bookAdd() throws Exception;
	void bookDelete() throws Exception;
	
	// 책 대여 및 반납
	String bookLendList() throws Exception;
	void bookLend() throws Exception;
	void bookReturn() throws Exception;
	void searchInfo() throws Exception;
	
	// 연체 정보 보기
	void delayInfo() throws Exception;
	
	// 탑 텐
	void topTen() throws Exception;
	
}

