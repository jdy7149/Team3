package team3;

import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.*;

public class BookAdmin implements BookAdminIF {
	
	private BookAdminGuiHandler gui;
	private Connection con;
	private int lendLimit;
	
	public BookAdmin(BookAdminGuiHandler gui, Connection con) throws Exception {
		this.gui = gui;
		this.con = con;
		lendLimit = 5;
	}
	
	public int getLendLimit() {
		return lendLimit;
	}

	public void setLendLimit(int lendLimit) {
		this.lendLimit = lendLimit;
	}

	
	@Override

	public void login() throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
	    String url = "jdbc:oracle:thin:@localhost:1521:xe";
	    String user = "scott";
	    String pwd = "1234";
	    Connection con = DriverManager.getConnection(url, user, pwd);
	    String sql = "select * from admin where admin_id = ? and admin_pw = ?";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ps.setString(1, gui.getloginId());
	    ps.setString(2, gui.getloginPw());
	    ResultSet rs = ps.executeQuery();
	    if(rs.next()) {
	    	gui.setLoginStatus(true);
	    	gui.setMenubar(true);
	    }else {
	    	if(gui.getloginId().equals("")||gui.getloginPw().equals("")) {
	    		gui.setLoginMsg("로그인 정보가 입력되지 않았습니다. 입력한 로그인 정보를 확인해주세요.");
	    	}else {
	    		gui.setLoginMsg("로그인 정보가 일치하지 않습니다.");   		
	    	}
	    }
	    rs.close();
	    ps.close();
	    con.close();
	}

	//회원가입 메서드
	public void adminAdd() throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
	    String url = "jdbc:oracle:thin:@localhost:1521:xe";
	    String user = "scott";
	    String pwd = "1234";
	    Connection con = DriverManager.getConnection(url, user, pwd);
	    String sql = "select * from admin where admin_id = ?";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ps.setString(1, gui.getAdminId());
	    ResultSet rs = ps.executeQuery();
	    if(rs.next()) {
	    	if(gui.getAdminId().equals("") || gui.getAdminPw().equals("") || gui.getAdminKey().equals("")) {
	    		gui.setAdminAddMsg("입력 정보가 모두 입력되지 않았습니다. 입력 정보를 확인해주세요.");	    			    		
	    	}else {
	    		if(gui.getAdminKey().equals("1234")) {
	    			gui.setAdminAddMsg("중복된 ID입니다.");	    			    			    		
	    		}else {
	    			gui.setAdminAddMsg("관리자 키를 잘못 입력하였습니다. 관리자 키를 확인해주세요.");	    			    			    			    			    		
	    		}	    		
	    	}
	    }else {
	    	if(gui.getAdminId().equals("") || gui.getAdminPw().equals("") || gui.getAdminKey().equals("")) {
	    		gui.setAdminAddMsg("입력 정보가 모두 입력되지 않았습니다. 입력 정보를 확인해주세요.");	    			    		
	    	}else {
	    		if(gui.getAdminKey().equals("1234")) {
	    			sql = "insert into admin values (?, ?)";
	    			ps = con.prepareStatement(sql);
	    			ps.setString(1, gui.getAdminId());
	    			ps.setString(2, gui.getAdminPw());
	    			ps.execute();
	    			gui.setAdminId("");
	    			gui.setAdminPw("");
	    			gui.setAdminKey("");
	    			gui.setAdminAddMsg("회원가입이 완료되었습니다.");	    			    			    			    			    		
	    		}else {
	    			gui.setAdminAddMsg("관리자 키를 잘못 입력하였습니다. 관리자 키를 확인해주세요.");	    			    			    			    			    		    			    		
	    		}    			    		
	    	}
	    }
	    rs.close();
	    ps.close();
	    con.close();
	}
	
	
	
	
	@Override
	public String userList() throws Exception {
	      
		String sql = "select person_id, person_name, addr, tel, birth from person order by person_id asc";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    String str = "회원번호   이름\t      주소        연락처\t         생년월일\n";
	    while(rs.next()) {
	       str = str + rs.getInt("person_id")+"\t   "+String.format("%-"+(15-(rs.getString("person_name").length()*2))+"s", rs.getString("person_name"))
	       		+ String.format("%-"+(14-(rs.getString("addr").length()*2))+"s", rs.getString("addr"))
	       		+ String.format("%-"+(32-(rs.getString("tel").length()))+"s", rs.getString("tel"))
	       		+ rs.getString("birth") + "\n";
	    }
	    rs.close();
	    ps.close();
	    return str;
	}

	
	
	// 회원 등록1
	@Override
	public void userAdd() throws Exception {

		if (gui.getUserAddName().equals("") || gui.getUserAddTel().equals("") || gui.getUserAddAddr().equals("")
				|| gui.getUserAddBirth().equals("")) {
			gui.setUserAddMsg("필수 입력 정보를 확인해주세요.");

		} else {
			String sql = "insert into person (person_name,tel,addr,birth) values(?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, gui.getUserAddName());
			ps.setString(2, gui.getUserAddTel());
			ps.setString(3, gui.getUserAddAddr());
			ps.setString(4, gui.getUserAddBirth());
			ps.executeUpdate();

			gui.setUserAddMsg(gui.getUserAddName() + "님의 등록이 완료되었습니다.");

			gui.setUserList(userList());
			ps.close();
		}
	}
	
	@Override
	public void userInfo() throws Exception {
		String sql = "select * from person where person_id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, gui.getUserUpdateId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			gui.setUserUpdateName(rs.getString("person_name"));
			gui.setUserUpdateAddr(rs.getString("addr"));
			gui.setUserUpdateTel(rs.getString("tel"));
			gui.setUserUpdateBirth(rs.getString("birth"));
		} else {
			if (gui.getUserUpdateId().equals("")) {
				gui.setUserUpdateMsg("회원 번호가 입력되지 않았습니다.");
			} else {
				gui.setUserUpdateMsg("등록되지 않은 회원번호입니다. 회원번호를 확인해 주세요.");
			}
		}
		rs.close();
		ps.close();

	}

	// 회원 정보 수정 메서드
	@Override
	public void userUpdate() throws Exception {
		if (gui.getUserUpdateName().equals("") || gui.getUserUpdateAddr().equals("")
				|| gui.getUserUpdateTel().equals("") || gui.getUserUpdateBirth().equals("")) {
			gui.setUserUpdateMsg("입력되지 않은 정보가 있습니다. 입력 정보를 확인해주세요.");
		} else {
			String sql = "update person set person_name = ? , addr = ?, tel = ?, birth = ? where person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, gui.getUserUpdateName());
			ps.setString(2, gui.getUserUpdateAddr());
			ps.setString(3, gui.getUserUpdateTel());
			ps.setString(4, gui.getUserUpdateBirth());
			ps.setString(5, gui.getUserUpdateId());
			int count = ps.executeUpdate();
			if (count > 0) {
				gui.setUserUpdateMsg(gui.getUserUpdateName() + " 님의 정보가 변경되었습니다.");
				;
				gui.setUserUpdateId("");
				gui.setUserUpdateName("");
				gui.setUserUpdateAddr("");
				gui.setUserUpdateTel("");
				gui.setUserUpdateBirth("");
			} else {
				gui.setUserUpdateMsg("회원 정보 변경에 실패하였습니다.");
			}
			gui.setUserUpdateList(userList());
			ps.close();
		}
	}

		
	//책 정보 현황 메서드
	@Override
	public String bookList() throws Exception {
		
		String sql = "select * from book order by book_id asc";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		String str = "책번호\t"
					+String.format("%-"+(45-("도서명".length()*2))+"s", "도서명") + "\t\t"
					+ String.format("%-"+(20-("출판사".length()*2))+"s", "출판사") + "\t"
					+ "저자\n";
		while (rs.next()) {
			str += rs.getString("book_id") + "\t"
					+ String.format("%-"+(55-(rs.getString("book_name").length()*2))+"s", rs.getString("book_name"))+"\t" 
					+ String.format("%-"+(20-(rs.getString("publisher").length()*2))+"s", rs.getString("publisher"))+"\t"
					+ rs.getString("author") + "\n";
		}
		rs.close();
		ps.close();
		return str;
	}
	
	//책 등록 메서드
	@Override
	public void bookAdd() throws Exception{
	      
	    Map<String, String> genreId = new HashMap<String, String>();
	    genreId.put("철학",	 "philosophy_sq.NEXTVAL");
	    genreId.put("문학",	 "literature_sq.NEXTVAL");
	    genreId.put("과학",	 "science_sq.NEXTVAL");
	    
	    if(gui.getBookAddtitle().equals("") || gui.getBookAddGenre().equals("") || gui.getBookAddAuthor().equals("") || gui.getBookAddPublisher().equals("")) {
	    	gui.setBookAddMsg("입력되지 않은 정보가 있습니다. 입력 정보를 확인해주세요.");
	    }
	    else {
		    String sql = "insert into book (book_id, book_name, author, publisher) "
		     	+ "values (" + genreId.get(gui.getBookAddGenre()) + " ,?,?,?)";
		    PreparedStatement ps = con.prepareStatement(sql);
		      
		    ps.setString(1, gui.getBookAddtitle());
		    ps.setString(2, gui.getBookAddAuthor());
		    ps.setString(3, gui.getBookAddPublisher());
		    ps.executeUpdate();
		    gui.setBookAddMsg(gui.getBookAddtitle() + " 책의 신규 등록이 완료되었습니다.");   
		    gui.setBookAddList(bookList());
		    ps.close();
	    }
	}
	
	//책 삭제 메서드
	@Override
	public void bookDelete() throws Exception{
		
		if(gui.getBookDeleteId().equals("")) {
			gui.setBookDeleteMsg("책 번호가 입력되지 않았습니다.");			
		}
		else {
			String sql = "delete from book where book_id=? ";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, gui.getBookDeleteId());
			int count = ps.executeUpdate();
			if(count == 0) {
				gui.setBookDeleteMsg("해당 책은 등록되어있지 않습니다.");
			}else {
					gui.setBookDeleteMsg(gui.getBookDeleteId() + " 번 책의 정보가 삭제 되었습니다.");
			}
			gui.setBookDeleteList(bookList());
			ps.close();
		}
	}
	
	//책 대여 현황 리스트 메서드
	@Override
	public String bookLendList() throws Exception{
		
		String sql = "select records_id,book_id,person_id,to_char(TRUNC(event_time),'YYYY-MM-DD') as event_time from records order by records_id asc";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		String str = "책 번호\t\t사용자ID\t\t대여일\n";
		while(rs.next()) {
			str += rs.getInt("book_id")+"\t\t"+rs.getInt("person_id")+"\t\t"+rs.getString("event_time")+"\n";
		}
		rs.close();
		ps.close();
		
		return str;
	}
	
	//책 대여 메서드
	@Override
	public void bookLend() throws Exception{
		String person_id = gui.getBookLendPid();
		String book_id = gui.getBookLendBid();
		int count = 0;
		String sql = "select * from records where person_id=?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, person_id);
		ResultSet rs = ps.executeQuery();
		if(gui.getBookLendBid().equals("") || gui.getBookLendPid().equals("")) {
			gui.setBookLendMsg("입력되지 않은 정보가 있습니다. 입력 정보를 확인해주세요.");
		}else {
			if(rs.next()) {
				while(rs.next()) {
					count++;
				}
				if(count >= lendLimit) {
					gui.setBookLendMsg(person_id+"번 사용자님은 이미 " + lendLimit +"권의 책을 빌리셨습니다. 반납 후 이용해주세요.");			
				}
				else {
					sql = "select * from records where book_id=?";
					ps = con.prepareStatement(sql);
					ps.setString(1, book_id);
					rs = ps.executeQuery();
					if(rs.next()) {
						gui.setBookLendMsg(book_id+"번 책은 대여 된 상태입니다.");
					}
					else {
						sql = "select * from book where book_id=?";
						ps = con.prepareStatement(sql);
						ps.setString(1, book_id);
						rs = ps.executeQuery();
						if(rs.next()) {
							sql = "update book set lend_count=lend_count+1 where book_id=?";
							ps = con.prepareStatement(sql);
							ps.setString(1, book_id);
							ps.executeUpdate();
							sql = "insert into records (book_id,person_id) values (?,?)";
							ps = con.prepareStatement(sql);
							ps.setString(1, book_id);
							ps.setString(2, person_id);
							ps.executeUpdate();
							gui.setBookLendMsg(person_id+"번 사용자님의"+book_id+"번 책 대여가 완료되었습니다.");
						}
						else {
							gui.setBookLendMsg(book_id+"번 책은 등록되지 않았습니다. 책 번호를 확인해주세요.");				
						}
					}
				}	
			}else {
				gui.setBookLendMsg(person_id+"번 사용자는 등록되지 않은 사용자입니다.");
			}
		}
		gui.setBookLendList(bookLendList());
		rs.close();
		ps.close();
	}
	

	//책 반납 메서드
	@Override
	public void bookReturn() throws Exception{
		String person_id = gui.getBookLendPid();
		String book_id = gui.getBookLendBid();
		String sql = "select * from book where book_id=?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, book_id);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			sql = "select * from person where person_id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, person_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				if(gui.getBookLendBid().equals("") || gui.getBookLendPid().equals("")) {
					gui.setBookLendMsg("입력되지 않은 정보가 있습니다. 입력 정보를 확인해주세요.");
				}else {
					sql = "select * from records where book_id=? and person_id=?";
					ps = con.prepareStatement(sql);
					ps.setString(1, book_id);
					ps.setString(2, person_id);
					ps.executeUpdate();
					rs = ps.executeQuery();
					if(rs.next()) {
						sql = "delete from records where book_id=?";
						ps = con.prepareStatement(sql);
						ps.setString(1, book_id);
						ps.executeUpdate();
						gui.setBookLendMsg(person_id+"번 사용자로부터 "+book_id+"번 책이 반납되었습니다.");
					}else {
						gui.setBookLendMsg(person_id+"사용자님은 "+book_id+"번 책을 대여하지 않았습니다. 책 번호를 확인해주세요.");
					}						
				}
			}else {
				gui.setBookLendMsg(person_id+"번 사용자는 등록되지 않은 사용자입니다.");										
			}
		}else {
			gui.setBookLendMsg(book_id+"번 책은 등록되지 않았습니다. 책 번호를 확인해주세요.");
		}
		gui.setBookLendList(bookLendList());
		rs.close();
		ps.close();
	}
	
	
	//검색하기 메서드
	@Override
	public void searchInfo() throws Exception{
		
		String key = gui.getSearchInfoKey();
		
		if (gui.getBookSearchState()) {
			PreparedStatement ps = con.prepareStatement("SELECT book_id, book_name, author, publisher, genre, "
					+ "    CASE "
					+ "        WHEN book_id IN (SELECT book_id FROM records) THEN '대여불가' "
					+ "        ELSE '대여가능' "
					+ "    END AS status "
					+ "FROM book, genres "
					+ "WHERE book_id BETWEEN min AND max "
					+ "    AND book_name LIKE ? "
					+ "ORDER BY book_id ASC");
			ps.setString(1, "%" + key + "%");
			ResultSet rs = ps.executeQuery();
			
			gui.setSearchResult("");
			
			gui.appendSearchResult("책번호\t도서명\t\t\t\t\t저자\t\t\t출판사\t\t장르\t대여상태\n");
			
			while (rs.next()) {
				String row = rs.getInt("book_id") + "\t" + String.format("%-"+(55-(rs.getString("book_name").length()*2))+"s", rs.getString("book_name")) + "\t" 
						+ String.format("%-"+(30-(rs.getString("author").length()*2))+"s", rs.getString("author")) + "\t" + String.format("%-"+(20-(rs.getString("publisher").length()*2))+"s", rs.getString("publisher")) + "\t"
						+ rs.getString("genre") + "\t" + rs.getString("status") +"\n";
				gui.appendSearchResult(row);
			}
			
			rs.close();
			ps.close();
		}
		else if (gui.getPersonSearchState()){
			PreparedStatement ps = con.prepareStatement("SELECT person.person_id, person_name, tel, addr, birth, NVL(" + lendLimit + " - lend_per_person, 5) AS lend_limit "
					+ "FROM person, (SELECT person_id, COUNT(person_id) AS lend_per_person "
					+ "              FROM records "
					+ "              GROUP BY person_id) lend_per_person "
					+ "WHERE person.person_id = lend_per_person.person_id(+) "
					+ "AND person_name LIKE ?"
					+ "ORDER BY person_id ASC");
			ps.setString(1, "%" + key + "%");
			ResultSet rs = ps.executeQuery();
			
			gui.setSearchResult("");
			
			gui.appendSearchResult("사용자ID\t 이름\t\t연락처\t\t   주소\t\t    생년월일\t대여가능도서(최대 5권)\n");
			
			while (rs.next()) {
				String row = rs.getInt("person_id") + "\t " + String.format("%-"+(23-(rs.getString("person_name").length()*2))+"s", rs.getString("person_name"))
						+ rs.getString("tel") + "\t   " + String.format("%-"+(25-(rs.getString("addr").length()*2))+"s", rs.getString("addr"))
						+ rs.getString("birth") + "\t" + rs.getInt("lend_limit") + "권 ("+(5-rs.getInt("lend_limit"))+"권 대여중)\n";
				gui.appendSearchResult(row);
			}
			
			rs.close();
			ps.close();
		}
		
	}
	
	@Override//
	public void delayInfo() throws Exception{
		
		String sql = "select records_id,person_name,book_name,to_char(TRUNC(r.event_time),'YYYY-MM-DD')as event_time\r\n"
				+ "from records r,book b,person p\r\n" + "where r.book_id=b.book_id\r\n"
				+ "and r.person_id=p.person_id\r\n" + "and event_time+14>systimestamp";// 대여한지 2주 지난 회원 확인
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		String blank=" ";
		int a=40;
		int b=40;

		while (rs.next()) {
			
			a-=String.valueOf(rs.getInt("records_id")).length();
			b-=3*(rs.getString("person_name").length());
			gui.addDelayInfo(rs.getInt("records_id")+blank.repeat(a)+rs.getString("person_name")+blank.repeat(b)+rs.getString("event_time")+blank.repeat(22)+rs.getString("book_name"));
			a=40;
			b=40;
		}
		rs.close();
		ps.close();
	}

	@Override
	public void topTen() throws Exception { 

		String sql = "SELECT * FROM (\r\n" + "    SELECT ROW_NUMBER() OVER (ORDER BY SUM(lend_count) DESC) AS rank,\r\n"
				+ "    book_name\r\n" + "    FROM book\r\n" + "    GROUP BY book_name\r\n" + ")\r\n"
				+ "WHERE ROWNUM <= 10";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int rank = rs.getInt("rank");
			String title = rs.getString("book_name");
			gui.addTopTenRank(rank + "     " + title);
		}
		
		rs.close();
		ps.close();
	}


} 