package team3;

import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.*;

public class BookAdmin implements BookAdminIF {
	
	private BookAdminGuiHandler gui;
	private Connection con;
	private int lendLimit;
	private int deadline;
	
	public BookAdmin(BookAdminGuiHandler gui, Connection con) throws Exception {
		this.gui = gui;
		this.con = con;
		lendLimit = 5;
		deadline = 14;
	}
	public int getLendLimit() {
		return lendLimit;
	}

	public void setLendLimit(int lendLimit) {
		this.lendLimit = lendLimit;
	}
	
	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}


	@Override
	public void login() throws Exception{
	
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
	    		gui.setLoginMsg("�α��� ������ �Էµ��� �ʾҽ��ϴ�. �Է��� �α��� ������ Ȯ�����ּ���.");
	    	}else {
	    		gui.setLoginMsg("�α��� ������ ��ġ���� �ʽ��ϴ�.");   		
	    	}
	    }
	    rs.close();
	    ps.close();
	}

	//ȸ������ �޼���
	public void register() throws Exception{
	    String sql = "select * from admin where admin_id = ?";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ps.setString(1, gui.getAdminId());
	    ResultSet rs = ps.executeQuery();
	    if(rs.next()) {
	    	if(gui.getAdminId().equals("") || gui.getAdminPw().equals("") || gui.getAdminKey().equals("")) {
	    		gui.setAdminAddMsg("�Է� ������ ��� �Էµ��� �ʾҽ��ϴ�. �Է� ������ Ȯ�����ּ���.");	    			    		
	    	}else {
	    		if(gui.getAdminKey().equals("1234")) {
	    			gui.setAdminAddMsg("�ߺ��� ID�Դϴ�.");	    			    			    		
	    		}else {
	    			gui.setAdminAddMsg("������ Ű�� �߸� �Է��Ͽ����ϴ�. ������ Ű�� Ȯ�����ּ���.");	    			    			    			    			    		
	    		}	    		
	    	}
	    }else {
	    	if(gui.getAdminId().equals("") || gui.getAdminPw().equals("") || gui.getAdminKey().equals("")) {
	    		gui.setAdminAddMsg("�Է� ������ ��� �Էµ��� �ʾҽ��ϴ�. �Է� ������ Ȯ�����ּ���.");	    			    		
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
	    			gui.setAdminAddMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");	    			    			    			    			    		
	    		}else {
	    			gui.setAdminAddMsg("������ Ű�� �߸� �Է��Ͽ����ϴ�. ������ Ű�� Ȯ�����ּ���.");	    			    			    			    			    		    			    		
	    		}    			    		
	    	}
	    }
	    rs.close();
	    ps.close();
	}
	
	
	
	
	@Override
	public String userList() throws Exception {
	      
		String sql = "select person_id, person_name, addr, tel, birth from person order by person_id asc";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    String str = "ȸ����ȣ   �̸�\t      �ּ�        ����ó\t         �������\n";
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

	
	
	// ȸ�� ���1
	@Override
	public void userAdd() throws Exception {
		if (gui.getUserAddName().equals("") || gui.getUserAddTel().equals("") || gui.getUserAddAddr().equals("")
				|| gui.getUserAddBirth().equals("")) {
			gui.setUserAddMsg("�ʼ� �Է� ������ Ȯ�����ּ���.");

		} else {
			String sql = "select * from person where tel = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, gui.getUserAddTel());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				gui.setUserAddMsg("�̹� ��ϵ� ������Դϴ�.");
			}else {
				sql = "insert into person (person_name,tel,addr,birth) values(?,?,?,?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, gui.getUserAddName());
				ps.setString(2, gui.getUserAddTel());
				ps.setString(3, gui.getUserAddAddr());
				ps.setString(4, gui.getUserAddBirth());
				ps.executeUpdate();
				
				gui.setUserAddMsg(gui.getUserAddName() + "���� ����� �Ϸ�Ǿ����ϴ�.");
				gui.setUserAddName("");
				gui.setUserAddTel("");
				gui.setUserAddAddr("");
				gui.setUserAddBirth("");
				
				gui.setUserList(userList());
			}
			rs.close();
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
				gui.setUserUpdateMsg("ȸ�� ��ȣ�� �Էµ��� �ʾҽ��ϴ�.");
			} else {
				gui.setUserUpdateMsg("��ϵ��� ���� ȸ����ȣ�Դϴ�. ȸ����ȣ�� Ȯ���� �ּ���.");
			}
		}
		rs.close();
		ps.close();

	}

	// ȸ�� ���� ���� �޼���
	@Override
	public void userUpdate() throws Exception {
		if (gui.getUserUpdateName().equals("") || gui.getUserUpdateAddr().equals("")
				|| gui.getUserUpdateTel().equals("") || gui.getUserUpdateBirth().equals("")) {
			gui.setUserUpdateMsg("�Էµ��� ���� ������ �ֽ��ϴ�. �Է� ������ Ȯ�����ּ���.");
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
				gui.setUserUpdateMsg(gui.getUserUpdateName() + " ���� ������ ����Ǿ����ϴ�.");
				;
				gui.setUserUpdateId("");
				gui.setUserUpdateName("");
				gui.setUserUpdateAddr("");
				gui.setUserUpdateTel("");
				gui.setUserUpdateBirth("");
			} else {
				gui.setUserUpdateMsg("ȸ�� ���� ���濡 �����Ͽ����ϴ�.");
			}
			gui.setUserUpdateList(userList());
			ps.close();
		}
	}

		
	//å ���� ��Ȳ �޼���
	@Override
	public String bookList() throws Exception {
		
		String sql = "select * from book order by book_id asc";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		String str = "å��ȣ\t"
					+String.format("%-"+(45-("������".length()*2))+"s", "������") + "\t\t"
					+ String.format("%-"+(20-("���ǻ�".length()*2))+"s", "���ǻ�") + "\t"
					+ "����\n";
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
	
	//å ��� �޼���
	@Override
	public void bookAdd() throws Exception{
	      
	    Map<String, String> genreId = new HashMap<String, String>();
	    genreId.put("ö��",	 "philosophy_sq.NEXTVAL");
	    genreId.put("����",	 "literature_sq.NEXTVAL");
	    genreId.put("����",	 "science_sq.NEXTVAL");
	    
	    if(gui.getBookAddTitle().equals("") || gui.getBookAddGenre().equals("") || gui.getBookAddAuthor().equals("") || gui.getBookAddPublisher().equals("")) {
	    	gui.setBookAddMsg("�Էµ��� ���� ������ �ֽ��ϴ�. �Է� ������ Ȯ�����ּ���.");
	    }
	    else {
		    String sql = "insert into book (book_id, book_name, author, publisher) "
		     	+ "values (" + genreId.get(gui.getBookAddGenre()) + " ,?,?,?)";
		    PreparedStatement ps = con.prepareStatement(sql);
		      
		    ps.setString(1, gui.getBookAddTitle());
		    ps.setString(2, gui.getBookAddAuthor());
		    ps.setString(3, gui.getBookAddPublisher());
		    ps.executeUpdate();
		    gui.setBookAddMsg(gui.getBookAddTitle() + " å�� �ű� ����� �Ϸ�Ǿ����ϴ�.");
		    gui.setBookAddTitle("");
		    gui.setBookAddGenre("");
		    gui.setBookAddAuthor("");
		    gui.setBookAddPublisher("");
		    gui.setBookAddList(bookList());
		    ps.close();
	    }
	}
	
	//å ���� �޼���
	@Override
	public void bookDelete() throws Exception{
		
		if(gui.getBookDeleteId().equals("")) {
			gui.setBookDeleteMsg("å ��ȣ�� �Էµ��� �ʾҽ��ϴ�.");			
		}
		else {
			String sql = "delete from book where book_id=? ";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, gui.getBookDeleteId());
			int count = ps.executeUpdate();
			if(count == 0) {
				gui.setBookDeleteMsg("�ش� å�� ��ϵǾ����� �ʽ��ϴ�.");
			}else {
					gui.setBookDeleteMsg(gui.getBookDeleteId() + " �� å�� ������ ���� �Ǿ����ϴ�.");
					gui.setBookDeleteId("");
			}
			gui.setBookDeleteList(bookList());
			ps.close();
		}
	}
	
	//å �뿩 ��Ȳ ����Ʈ �޼���
	@Override
	public String bookLendList() throws Exception{
		
		String sql = "select records_id,book_id,person_id,to_char(TRUNC(event_time),'YYYY-MM-DD') as event_time from records order by records_id asc";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		String str = "å ��ȣ\t\t�����ID\t\t�뿩��\n";
		while(rs.next()) {
			str += rs.getInt("book_id")+"\t\t"+rs.getInt("person_id")+"\t\t"+rs.getString("event_time")+"\n";
		}
		rs.close();
		ps.close();
		
		return str;
	}
	
	//å �뿩 �޼���
	@Override
	public void bookLend() throws Exception{
		String person_id = gui.getBookLendPid();
		String book_id = gui.getBookLendBid();
		int count = 0;
		
		PreparedStatement ps = con.prepareStatement("SELECT person.person_id, \r\n"
				+ "    CASE \r\n"
				+ "        WHEN NVL(count, 0) >= 5 THEN 1\r\n"
				+ "        ELSE 0\r\n"
				+ "    END AS exceed_limit\r\n"
				+ "FROM person, (SELECT person_id, COUNT(person_id) AS count\r\n"
				+ "                FROM records\r\n"
				+ "                GROUP BY person_id) lent_per_person\r\n"
				+ "WHERE person.person_id = lent_per_person.person_id(+)\r\n"
				+ "AND person.person_id = ?");
		ps.setString(1, person_id);
		ResultSet searchPerson = ps.executeQuery();
		
		ps = con.prepareStatement("SELECT book_id,  \r\n"
				+ "    CASE \r\n"
				+ "        WHEN book_id IN (SELECT book_id FROM records) THEN 1\r\n"
				+ "        ELSE 0\r\n"
				+ "    END AS is_lent\r\n"
				+ "FROM book\r\n"
				+ "WHERE book_id = ?");
		ps.setString(1, book_id);
		ResultSet searchBook = ps.executeQuery();
		
		if(gui.getBookLendBid().equals("") || gui.getBookLendPid().equals("")) {
			gui.setBookLendMsg("�Էµ��� ���� ������ �ֽ��ϴ�. �Է� ������ Ȯ�����ּ���.");
		}else {
			if (!searchPerson.next()) {
				gui.setBookLendMsg(person_id+"�� ����ڴ� ��ϵ��� ���� ������Դϴ�.");
			}
			else if (!searchBook.next()) {
				gui.setBookLendMsg(book_id+"�� å�� ��ϵ��� �ʾҽ��ϴ�. å ��ȣ�� Ȯ�����ּ���.");
			}
			else {
				if (searchPerson.getInt("exceed_limit") == 1) {
					gui.setBookLendMsg(person_id+"�� ����ڴ��� �̹� " + lendLimit +"���� å�� �����̽��ϴ�. �ݳ� �� �̿����ּ���.");
				}
				else if (searchBook.getInt("is_lent") == 1) {
					gui.setBookLendMsg(book_id+"�� å�� �뿩 �� �����Դϴ�.");
				}
				else {
					ps = con.prepareStatement("insert into records (book_id,person_id) values (?,?)");
					ps.setString(1, book_id);
					ps.setString(2, person_id);
					ps.executeUpdate();
					gui.setBookLendMsg(person_id+"�� ����ڴ���"+book_id+"�� å �뿩�� �Ϸ�Ǿ����ϴ�.");
					gui.setBookLendPid("");
					gui.setBookLendBid("");
				}
			}
		}
		gui.setBookLendList(bookLendList());
		searchBook.close();
		searchPerson.close();
		ps.close();
	}
	

	//å �ݳ� �޼���
	@Override
	public void bookReturn() throws Exception{
		String book_id = gui.getBookLendBid();
		
		PreparedStatement ps = con.prepareStatement("SELECT records.person_id, book.book_id,\r\n"
				+ "    CASE \r\n"
				+ "        WHEN records.book_id IS NULL THEN 0\r\n"
				+ "        ELSE 1\r\n"
				+ "    END AS is_lent\r\n"
				+ "FROM records, book\r\n"
				+ "WHERE book.book_id = records.book_id(+)\r\n"
				+ "AND book.book_id = ?");
		ps.setString(1, book_id);
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			gui.setBookLendMsg(book_id+"�� å�� ��ϵ��� �ʾҽ��ϴ�. å ��ȣ�� Ȯ�����ּ���.");
		}
		else {
			if (rs.getInt("is_lent") == 0) {
				gui.setBookLendMsg(book_id + "�� å�� �뿩���� �ʾҽ��ϴ�. å ��ȣ�� Ȯ�����ּ���.");
			}
			else {
				ps = con.prepareStatement("delete from records where book_id=?");
				ps.setString(1, book_id);
				ps.executeUpdate();
				gui.setBookLendMsg(rs.getInt("person_id") + "�� ����ڷκ��� "+ book_id +"�� å�� �ݳ��Ǿ����ϴ�.");
				gui.setBookLendPid("");
				gui.setBookLendBid("");
			}
		}
		rs.close();
		ps.close();
		gui.setBookLendList(bookLendList());
	}
	
	
	//�˻��ϱ� �޼���
	@Override
	public void searchInfo() throws Exception{
		
		String key = gui.getSearchInfoKey();
		
		if (gui.getBookSearchState()) {
			PreparedStatement ps = con.prepareStatement("SELECT book_id, book_name, author, publisher, genre, "
					+ "    CASE "
					+ "        WHEN book_id IN (SELECT book_id FROM records) THEN '�뿩�Ұ�' "
					+ "        ELSE '�뿩����' "
					+ "    END AS status "
					+ "FROM book, genres "
					+ "WHERE book_id BETWEEN min AND max "
					+ "    AND book_name LIKE ? "
					+ "ORDER BY book_id ASC");
			ps.setString(1, "%" + key + "%");
			ResultSet rs = ps.executeQuery();
			
			gui.setSearchResult("");
			
			gui.appendSearchResult("å��ȣ\t������\t\t\t\t\t����\t\t\t���ǻ�\t\t�帣\t�뿩����\n");
			
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
			
			gui.appendSearchResult("�����ID\t �̸�\t\t����ó\t\t   �ּ�\t\t    �������\t�뿩���ɵ���(�ִ� 5��)\n");
			
			while (rs.next()) {
				String row = rs.getInt("person_id") + "\t " + String.format("%-"+(23-(rs.getString("person_name").length()*2))+"s", rs.getString("person_name"))
						+ rs.getString("tel") + "\t   " + String.format("%-"+(25-(rs.getString("addr").length()*2))+"s", rs.getString("addr"))
						+ rs.getString("birth") + "\t" + rs.getInt("lend_limit") + "�� ("+(5-rs.getInt("lend_limit"))+"�� �뿩��)\n";
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
				+ "and r.person_id=p.person_id\r\n" + "and event_time+ " + deadline + " < systimestamp "
				+ "ORDER BY records_id ASC";// �뿩���� 2�� ���� ȸ�� Ȯ��
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		String blank=" ";
		int a=35;
		int b=34;

		while (rs.next()) {
			
			a-=2 * (String.valueOf(rs.getInt("records_id")).length());
			b-=3*(rs.getString("person_name").length());
			gui.addDelayInfo(rs.getInt("records_id")+blank.repeat(a+4)+rs.getString("person_name")+blank.repeat(b+3)+rs.getString("event_time")+blank.repeat(22)+rs.getString("book_name"));
			a=35;
			b=34;
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