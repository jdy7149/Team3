package team3;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.util.*;
import java.util.Date;
import java.awt.List;

public class BookAdminGui extends Frame implements ActionListener, BookAdminGuiHandler {
	
	//공통
	Menu menu;
	MenuBar menubar;
	MenuItem user_add, user_update, book_add, book_delete, book_lend, search_info, delay_info, book_topten, logout;
	Panel p_main;
	Dialog info;
	Button bt_check;
	
	//로그인 화면
	Label lb_login_title, lb_login_id, lb_login_pw, lb_login_msg;
	TextField tf_login_id, tf_login_pw;
	Button bt_login, bt_login_add;

	//회원가입 화면
	Label lb_admin_add_title, lb_admin_add_id, lb_admin_add_pw, lb_admin_add_key, lb_admin_add_msg;
	TextField tf_admin_add_id, tf_admin_add_pw, tf_admin_add_key;
	Button bt_admin_add, bt_admin_add_home;
	
	//초기 화면
	Label lb_main_msg1, lb_main_msg2;
	
	//사용자 등록
	Label lb_user_add_title, lb_user_add_name, lb_user_add_birth, lb_user_add_addr, lb_user_add_tel, lb_user_add_msg;
	TextField tf_user_add_name, tf_user_add_addr, tf_user_add_tel, tf_user_add_birth;
	TextArea ta_user_list;
	Button bt_user_add;
	Panel p_north, p_south;
	
	
	//사용자 정보 수정
	Label lb_user_update_title, lb_user_update_id, lb_user_update_name, lb_user_update_addr, lb_user_update_tel, lb_user_update_birth, lb_user_update_msg;
	TextField tf_user_update_id, tf_user_update_name, tf_user_update_addr, tf_user_update_tel, tf_user_update_birth;
	TextArea ta_user_update_list;
	Button bt_user_update_uinfo, bt_user_update;
	
	//책 정보 등록
	Label lb_book_add_title, lb_book_add_bname, lb_book_add_genre, lb_book_add_author, lb_book_add_publisher, lb_book_add_msg;
	TextField tf_book_add_bname, tf_book_add_genre, tf_book_add_author, tf_book_add_publisher;
	TextArea ta_book_add_list;
	Button bt_book_add;
	
	//책 정보 삭제
	Label lb_book_delete_title, lb_book_delete_bid, lb_book_delete_msg;
	TextField tf_book_delete_bid;
	TextArea ta_book_delete_list;
	Button bt_book_delete;
	
	//책 대여 반납
	Label lb_book_lend_title, lb_book_lend_pid, lb_book_lend_bid, lb_book_lend_msg;
	TextField tf_book_lend_pid, tf_book_lend_bid;
	TextArea ta_book_lend_list;
	Button bt_book_lend, bt_book_return;
	
	//검색하기
	Label lb_search_info_title;
	CheckboxGroup cg_options;
	Checkbox cb_book, cb_person;
	Button bt_search_info;
	TextField tf_search_info_key;
	TextArea ta_search_info_show_result;

	// 연체정보보기
	Label lb_delay_info_title, lb_delay_info_bname, lb_delay_info_msg,lb_time,day_delay;
	Panel p_main_c_n,p_main_c,p_main_s,p_main_s_one;
	Button bt_delay_info_reload;
	List l_delay,l_book_id,l_person_name,l_book_name,l_event_time,l_time_cal;

	// 인기순위
	Label lb_rank_info_title;
	Panel p_main_rank, p_main_rank_center;
	
	// DB 연동 기능 인터페이스
	private BookAdminIF features;
	
	// DB 연동 기능을 탑재하지 않았을 때 메시지
	private String featuresNotFound;
	
	// 로그인 상태
	private boolean loginStatus;
	
	public BookAdminGui() throws Exception{
		featuresNotFound = "DB연동 기능 사용불가";
		loginStatus = false;
		loginView();
		menubar = new MenuBar();
		this.setMenuBar(menubar);
		menu = new Menu("메뉴");
		menubar.add(menu);
		this.add(p_main);
		user_add = new MenuItem("회원정보 등록");
		user_update = new MenuItem("회원정보 수정");
		book_add = new MenuItem("책 정보 등록");
		book_delete = new MenuItem("책 정보 삭제");
		book_lend = new MenuItem("책 대여 및 반납");
		search_info = new MenuItem("검색하기");
		delay_info = new MenuItem("연체 정보 보기");
		book_topten = new MenuItem("인기도서 Top10");
		logout = new MenuItem("로그아웃");
		info = new Dialog(this, "메세지", true);
		info.setSize(450, 110);
		info.setLocation(800, 400);
		p_north = new Panel(new FlowLayout());
		p_south = new Panel(new FlowLayout());
		info.setLayout(new BorderLayout());
		info.add(p_north, "North");
		info.add(p_south, "Center");
		bt_check = new Button("확인");
		
		
		setMenubar(loginStatus);
		
		menu.add(user_add);
		menu.add(user_update);
		menu.add(book_add);
		menu.add(book_delete);
		menu.add(book_lend);
		menu.add(search_info);
		menu.add(delay_info);
		menu.add(book_topten);
		menu.addSeparator();
		menu.add(logout);
		
		user_add.addActionListener(this);
		user_update.addActionListener(this);
		book_add.addActionListener(this);
		book_delete.addActionListener(this);
		book_lend.addActionListener(this);
		search_info.addActionListener(this);
		delay_info.addActionListener(this);
		book_topten.addActionListener(this);
		logout.addActionListener(this);
		bt_check.addActionListener(this);

		this.addWindowListener(
				new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				}
		);
		info.addWindowListener(
				new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						info.dispose();
					}
				}
		);

	}
	
	
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
	@Override
	public Insets insets() {
		Insets i = new Insets(60, 30, 25, 30);
		return i;
	}
	
	@Override
	public void setFeatures(BookAdminIF features) {
		// DB 연동 기능 탑재
		this.features =features;
	}
	@Override
	public String getloginId() {
		return tf_login_id.getText();
	}
	@Override
	public String getloginPw() {
		return tf_login_pw.getText();
	}
	@Override
	public void setAdminAddMsg(String Message) {
		lb_admin_add_msg.setText(Message);
		p_south.add(bt_check);
		p_north.add(lb_admin_add_msg);
		info.setVisible(true);
	}
	@Override
	public void setLoginMsg(String Message) {
		lb_login_msg.setText(Message);
		p_south.add(bt_check);
		p_north.add(lb_login_msg);
		info.setVisible(true);
	}
	
	@Override
	public String getAdminId() {
		return tf_admin_add_id.getText();
	}
	@Override
	public String getAdminPw() {
		return tf_admin_add_pw.getText();
	}
	@Override
	public String getAdminKey() {
		return tf_admin_add_key.getText();
	}
	@Override
	public void setAdminId(String id) {
		tf_admin_add_id.setText(id);
		
	}
	@Override
	public void setAdminPw(String pw) {
		tf_admin_add_pw.setText(pw);
		
	}
	@Override
	public void setAdminKey(String key) {
		tf_admin_add_key.setText(key);
		
	}
	@Override
	public String getUserAddName() {
		return tf_user_add_name.getText();
	}

	@Override
	public String getUserAddTel() {
		return tf_user_add_tel.getText();
	}

	@Override
	public String getUserAddAddr() {
		return tf_user_add_addr.getText();
	}

	@Override
	public String getUserAddBirth() {
		return tf_user_add_birth.getText();
	}

	@Override
	public void setUserAddMsg(String message) {
		lb_user_add_msg.setText(message);
		p_south.add(bt_check);
		p_north.add(lb_user_add_msg);
		info.setVisible(true);
	}
	@Override
	public void setUserAddName(String name) {
		tf_user_add_name.setText(name);
		
	}
	@Override
	public void setUserAddTel(String tel) {
		tf_user_add_tel.setText(tel);
		
	}
	@Override
	public void setUserAddAddr(String addr) {
		tf_user_add_addr.setText(addr);
		
	}
	@Override
	public void setUserAddBirth(String birth) {
		tf_user_add_birth.setText(birth);
		
	}

	@Override
	public void setUserList(String userList) {
		ta_user_list.setText(userList);
	}

	@Override
	public String getUserUpdateId() {
		// TODO Auto-generated method stub
		return tf_user_update_id.getText();
	}

	@Override
	public String getUserUpdateName() {
		// TODO Auto-generated method stub
		return tf_user_update_name.getText();
	}

	@Override
	public String getUserUpdateTel() {
		// TODO Auto-generated method stub
		return tf_user_update_tel.getText();
	}

	@Override
	public String getUserUpdateAddr() {
		// TODO Auto-generated method stub
		return tf_user_update_addr.getText();
	}

	@Override
	public String getUserUpdateBirth() {
		// TODO Auto-generated method stub
		return tf_user_update_birth.getText();
	}

	@Override
	public void setUserUpdateId(String id) {
		// TODO Auto-generated method stub
		tf_user_update_id.setText(id);
	}

	@Override
	public void setUserUpdateName(String name) {
		// TODO Auto-generated method stub
		tf_user_update_name.setText(name);
	}

	@Override
	public void setUserUpdateAddr(String addr) {
		// TODO Auto-generated method stub
		tf_user_update_addr.setText(addr);
	}

	@Override
	public void setUserUpdateTel(String tel) {
		// TODO Auto-generated method stub
		tf_user_update_tel.setText(tel);
	}

	@Override
	public void setUserUpdateBirth(String birth) {
		// TODO Auto-generated method stub
		tf_user_update_birth.setText(birth);
	}

	@Override
	public void setUserUpdateMsg(String message) {
		// TODO Auto-generated method stub
		lb_user_update_msg.setText(message);
		p_south.add(bt_check);
		p_north.add(lb_user_update_msg);
		info.setVisible(true);
	}

	@Override
	public void setUserUpdateList(String userList) {
		// TODO Auto-generated method stub
		ta_user_update_list.setText(userList);
	}
	
	@Override
	public String getBookAddGenre() {
		return tf_book_add_genre.getText();
	}

	@Override
	public String getBookAddTitle() {
		return tf_book_add_bname.getText();
	}

	@Override
	public String getBookAddAuthor() {
		return tf_book_add_author.getText();
	}

	@Override
	public String getBookAddPublisher() {
		return tf_book_add_publisher.getText();
	}
	
	@Override
	public void setBookAddTitle(String title) {
		tf_book_add_bname.setText(title);
	}
	
	@Override
	public void setBookAddGenre(String genre) {
		tf_book_add_genre.setText(genre);
	}
	
	@Override
	public void setBookAddAuthor(String author) {
		tf_book_add_author.setText(author);
	}
	
	@Override
	public void setBookAddPublisher(String publisher) {
		tf_book_add_publisher.setText(publisher);	
	}

	@Override
	public void setBookAddMsg(String message) {
		lb_book_add_msg.setText(message);
		p_south.add(bt_check);
		p_north.add(lb_book_add_msg);
		info.setVisible(true);
	}

	@Override
	public void setBookAddList(String bookList) {
		ta_book_add_list.setText(bookList);
	}
	
	@Override
	public void setBookDeleteId(String id) {
		tf_book_delete_bid.setText(id);	
	}
	
	@Override
	public String getBookDeleteId() {
		return tf_book_delete_bid.getText();
	}

	@Override
	public void setBookDeleteMsg(String message) {
		lb_book_delete_msg.setText(message);
		p_south.add(bt_check);
		p_north.add(lb_book_delete_msg);
		info.setVisible(true);
	}

	@Override
	public void setBookDeleteList(String bookList) {
		ta_book_delete_list.setText(bookList);
	}

	@Override
	public String getBookLendPid() {
		return tf_book_lend_pid.getText();
	}

	@Override
	public String getBookLendBid() {
		return tf_book_lend_bid.getText();
	}

	@Override
	public void setBookLendPid(String pid) {
		tf_book_lend_pid.setText(pid);
	}
	
	@Override
	public void setBookLendBid(String bid) {
		tf_book_lend_bid.setText(bid);
	}
	
	@Override
	public void setBookLendMsg(String message) {
		lb_book_lend_msg.setText(message);
		p_south.add(bt_check);
		p_north.add(lb_book_lend_msg);
		info.setVisible(true);
	}

	@Override
	public void setBookLendList(String bookList) {
		ta_book_lend_list.setText(bookList);
	}

	@Override
	public String getSearchInfoKey() {
		return tf_search_info_key.getText();
	}

	@Override
	public boolean getBookSearchState() {
		return cb_book.getState();
	}

	@Override
	public boolean getPersonSearchState() {
		return cb_person.getState();
	}

	@Override
	public void setSearchResult(String result) {
		ta_search_info_show_result.setText(result);
	}

	@Override
	public void appendSearchResult(String result) {
		ta_search_info_show_result.append(result);
	}

	@Override
	public void addDelayInfo(String delayInfo) {
		l_delay.add(delayInfo);
	}

	@Override
	public void addTopTenRank(String rankInfo) {
		p_main_rank_center.add(new Label(rankInfo, Label.CENTER));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == user_add) {
			this.remove(p_main);
			try {
				String content = features != null ?
						features.userList() : featuresNotFound;
				userAddView(content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();
		} else if (obj == user_update) {
			this.remove(p_main);
			try {
				String content = features != null ? features.userList() : featuresNotFound;
				userUpdateView(content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();

		}else if(obj == book_add) {
			this.remove(p_main);
			try {
				String content = features != null ?
						features.bookList() : featuresNotFound;
				bookAddView(content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();
		}else if(obj == book_delete) {
			this.remove(p_main);
			try {
				String content = features != null ?
						features.bookList() : featuresNotFound;
				bookDeleteView(content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();
		}else if(obj == book_lend) {
			this.remove(p_main);
			try {
				String content = features != null ?
						features.bookLendList() : featuresNotFound;
				bookLendView(content);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();
		}else if(obj == search_info) {
			this.remove(p_main);
			searchInfoView();
			this.add(p_main);
			this.validate();
		}else if(obj == delay_info) {
			this.remove(p_main);
			try {
				delayInfoView();
				if (features != null) {
					features.delayInfo();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.add(p_main);
			this.validate();	
		}else if(obj == book_topten) {
			this.remove(p_main);
			topTenView();
			this.add(p_main);
			this.validate();
			if (features != null) {
				try {
					features.topTen();
					p_main_rank.revalidate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else if(obj == logout) {
			this.remove(p_main);
			logout();
			this.add(p_main);
			this.validate();
		} else if (features != null) {
		
			if(obj == bt_user_add) {
				p_north.removeAll();
				try {
					features.userAdd();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (obj == bt_user_update) {
				p_north.removeAll();
				try {
					features.userUpdate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (obj == bt_user_update_uinfo) {
				p_north.removeAll();
				try {
					features.userInfo();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(obj == bt_book_lend) {
				p_north.removeAll();
				try {
					features.bookLend();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(obj == bt_book_add) {
				p_north.removeAll();
				try {
					features.bookAdd();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			}else if(obj == bt_book_return) {
				p_north.removeAll();
				try {
					features.bookReturn();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(obj == bt_book_delete) {
				p_north.removeAll();
				try {
					features.bookDelete();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if (obj == bt_search_info) {
				try {
					features.searchInfo();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if (obj == bt_delay_info_reload) {
				this.remove(p_main);
				try {
					delayInfoView();
					features.delayInfo();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.add(p_main);
				this.validate();
			}else if(obj == bt_login) {
				p_north.removeAll();
				try {
					features.login();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(loginStatus == true) {
					this.remove(p_main);
					topTenView();
					this.add(p_main);
					this.validate();
					if (features != null) {
						try {
							features.topTen();
							p_main_rank.revalidate();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}else if(obj == bt_login_add) {
				p_north.removeAll();
				this.remove(p_main);
				adminAddView();
				this.add(p_main);
				this.validate();
			}else if(obj == bt_admin_add) {
				p_north.removeAll();
				try {
					features.register();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(obj == bt_admin_add_home) {
				p_north.removeAll();
				this.remove(p_main);
				loginView();
				this.add(p_main);
				this.validate();
			}else if(obj == bt_check) {
				info.dispose();
			}
		}
	}
	
	//메인 화면 메서드
	   public void mainScreen() {
		    p_main = new Panel(new BorderLayout());
			lb_main_msg1 = new Label("도서관리 프로그램 v3.0", Label.CENTER);
			lb_main_msg2 = new Label("상단 메뉴에서 사용할 기능을 선택해주세요.", Label.CENTER);
			p_main.add(lb_main_msg1);
			p_main.add(lb_main_msg2);

			this.add(p_main);
	   }
		//메인 화면 메서드
		public void loginView() {
			p_main = new Panel(new BorderLayout());
			Panel p_main_north = new Panel(new GridLayout(4, 1, 10, 10));
			Panel p_main_east = new Panel(new GridLayout(1, 1, 10, 10));
			Panel p_main_west = new Panel(new GridLayout(1, 1, 10, 10));
			Panel p_main_south = new Panel(new GridLayout(5, 1, 7, 7));
			Panel p_main_center = new Panel(new GridLayout(2, 3, 10, 10));
			
			lb_login_title = new Label("도서관리 프로그램 v3.0", Label.CENTER);
			lb_login_msg = new Label("", Label.CENTER);
			lb_login_id = new Label("          ID   : ");
			lb_login_pw = new Label("          PW : ");
			tf_login_id = new TextField("");
			tf_login_pw = new TextField("");
			tf_login_pw.setEchoChar('*');
			bt_login = new Button("로그인");
			bt_login_add = new Button("회원가입");	
			
			p_main_north.add(new Label());
			p_main_north.add(new Label());
			p_main_north.add(lb_login_title);
			p_main_north.add(new Label());		
			p_main_east.add(new Label("                                            "));
			p_main_west.add(new Label("                                            "));
			p_main_south.add(new Label());
			p_main_south.add(lb_login_msg);
			p_main_south.add(new Label());
			p_main_south.add(new Label());								
			
			p_main_center.add(lb_login_id);
			p_main_center.add(tf_login_id);
			p_main_center.add(bt_login);
			p_main_center.add(lb_login_pw);
			p_main_center.add(tf_login_pw);
			p_main_center.add(bt_login_add);
				
			p_main.add(p_main_center, "Center");
			p_main.add(p_main_north, "North");
			p_main.add(p_main_east, "East");
			p_main.add(p_main_west, "West");
			p_main.add(p_main_south, "South");
			
			this.add(p_main);
			
			bt_login.addActionListener(this);
			bt_login_add.addActionListener(this);		
		}	   

		//로그아웃 메서드
		public void logout() {
			loginStatus = false;
			setMenubar(loginStatus);
			loginView();
		}

		public void setMenubar(boolean login_info) {
			user_add.setEnabled(login_info);
			user_update.setEnabled(login_info);
			book_add.setEnabled(login_info);
			book_delete.setEnabled(login_info);
			book_lend.setEnabled(login_info);
			search_info.setEnabled(login_info);
			delay_info.setEnabled(login_info);
			book_topten.setEnabled(login_info);
			logout.setEnabled(login_info);
		}		
		
		//관리자 회원가입 화면 메서드
		public void adminAddView() {
			p_main = new Panel(new BorderLayout());
			Panel p_main_north = new Panel(new GridLayout(3, 1, 10, 10));
			Panel p_main_east = new Panel(new GridLayout(1, 1, 10, 10));
			Panel p_main_west = new Panel(new GridLayout(1, 1, 10, 10));
			Panel p_main_south = new Panel(new BorderLayout(10, 10));
			Panel p_main_south_center = new Panel(new GridLayout(3, 5, 5, 5));
			Panel p_main_center = new Panel(new GridLayout(4, 2, 10, 10));
			
			lb_admin_add_title = new Label("회원가입", Label.CENTER);
			lb_admin_add_id = new Label("ID   : ");
			lb_admin_add_pw = new Label("PW : ");
			lb_admin_add_key = new Label("Admin key : ");
			lb_admin_add_msg = new Label("", Label.CENTER);
			tf_admin_add_id = new TextField("");
			tf_admin_add_pw = new TextField("");
			tf_admin_add_key = new TextField("");
			tf_admin_add_pw.setEchoChar('*');
			tf_admin_add_key.setEchoChar('*');
			bt_admin_add = new Button("회원가입");
			bt_admin_add_home = new Button("처음으로");
			
			p_main_north.add(new Label());
			p_main_north.add(lb_admin_add_title);
			p_main_east.add(new Label("                                                           "));
			p_main_west.add(new Label("                                                           "));
			p_main_south_center.add(new Label());		
			p_main_south_center.add(new Label());
			p_main_south_center.add(bt_admin_add);
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(bt_admin_add_home);
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			p_main_south_center.add(new Label());
			
			p_main_south.add(lb_admin_add_msg, "North");
			p_main_south.add(p_main_south_center);
			
			p_main_center.add(lb_admin_add_id);
			p_main_center.add(tf_admin_add_id);
			p_main_center.add(lb_admin_add_pw);
			p_main_center.add(tf_admin_add_pw);
			p_main_center.add(lb_admin_add_key);
			p_main_center.add(tf_admin_add_key);
			
			p_main.add(p_main_center, "Center");
			p_main.add(p_main_north, "North");
			p_main.add(p_main_east, "East");
			p_main.add(p_main_west, "West");
			p_main.add(p_main_south, "South");
			
			this.add(p_main);
			
			bt_admin_add.addActionListener(this);
			bt_admin_add_home.addActionListener(this);
		}
		
		
	   //사용자 등록 화면 메서드
	   public void userAddView(String str) {
	       p_main = new Panel(new BorderLayout(10,10));
	       lb_user_add_title = new Label("회원 정보 추가",Label.CENTER);
	       p_main.add(lb_user_add_title, "North");
	         
	       Panel p_center_temp = new Panel(new BorderLayout(20,20));
	       Panel p_center_temp_west = new Panel(new GridLayout(6,2,5,16));
	       Panel p_center_temp_center = new Panel(new BorderLayout(5,5));
	         
	       lb_user_add_name = new Label("회원 이름 : ");
	       tf_user_add_name = new TextField();
	       lb_user_add_tel = new Label("전화 번호 : ");
	       tf_user_add_tel = new TextField();
	       lb_user_add_addr = new Label("주소 : ");
	       tf_user_add_addr = new TextField();
	       lb_user_add_birth = new Label("생년월일 : ");
	       tf_user_add_birth = new TextField();
	       ta_user_list = new TextArea(str,0,0,ta_book_lend_list.SCROLLBARS_VERTICAL_ONLY);
	       ta_user_list.setEditable(false);
	         
	       p_center_temp_west.add(new Label());
	       p_center_temp_west.add(new Label());
	       p_center_temp_west.add(lb_user_add_name);
	       p_center_temp_west.add(tf_user_add_name);
	       p_center_temp_west.add(lb_user_add_tel);
	       p_center_temp_west.add(tf_user_add_tel);
	       p_center_temp_west.add(lb_user_add_addr);
	       p_center_temp_west.add(tf_user_add_addr);
	       p_center_temp_west.add(lb_user_add_birth);
	       p_center_temp_west.add(tf_user_add_birth);
	         
	       p_center_temp_center.add(new Label("회원 정보 현황",Label.CENTER), "North");
	       p_center_temp_center.add(ta_user_list, "Center");
	       p_center_temp.add(p_center_temp_west,"West");
	       p_center_temp.add(p_center_temp_center,"Center");
	       p_main.add(p_center_temp,"Center");
	         
	       Panel p_south_temp = new Panel(new BorderLayout(5,5));
	       Panel p_south_south = new Panel();
	       lb_user_add_msg = new Label("");
	       bt_user_add = new Button("등록하기");
	       p_south_temp.add(lb_user_add_msg);
	       p_south_south.add(bt_user_add,"South");
	       p_south_temp.add(p_south_south,"South");
	       p_main.add(p_south_temp, "South");
	         
	       bt_user_add.addActionListener(this);
	   }
	   //회원 정보 수정 화면 메서드 1
	   public void userUpdateView(String str) {

	      p_main = new Panel(new BorderLayout(10, 10));
	      lb_user_update_title = new Label("회원 정보 수정", Label.CENTER);
	      p_main.add(lb_user_update_title, "North");

	      Panel pCenter = new Panel(new BorderLayout(10, 10));
	      Panel pCenterWest = new Panel(new GridLayout(6, 2, 5, 15));
	      Panel pCenterCenter = new Panel(new BorderLayout(5, 5));
	      Panel p_user_info = new Panel(new GridLayout(1, 2, 5, 5));

	      
	      lb_user_update_id = new Label("회원 번호 : ");
	      tf_user_update_id = new TextField("");
	      bt_user_update_uinfo = new Button("검색");
	      lb_user_update_name = new Label("회원 이름 : ");
	      tf_user_update_name = new TextField("");
	      lb_user_update_addr = new Label("주소 : ");
	      tf_user_update_addr = new TextField("");
	      lb_user_update_tel = new Label("연락처 : ");
	      tf_user_update_tel = new TextField("");
	      lb_user_update_birth = new Label("생년월일 : ");
	      tf_user_update_birth = new TextField("");
	      bt_user_update = new Button("수정하기");
	      ta_user_update_list = new TextArea(str, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
	      ta_user_update_list.setEditable(false);

	      p_user_info.add(tf_user_update_id);
	      p_user_info.add(bt_user_update_uinfo);
	      
	      pCenterWest.add(new Label());
	      pCenterWest.add(new Label());
	      pCenterWest.add(lb_user_update_id);
	      pCenterWest.add(p_user_info);
	      pCenterWest.add(lb_user_update_name);
	      pCenterWest.add(tf_user_update_name);
	      pCenterWest.add(lb_user_update_addr);
	      pCenterWest.add(tf_user_update_addr);
	      pCenterWest.add(lb_user_update_tel);
	      pCenterWest.add(tf_user_update_tel);
	      pCenterWest.add(lb_user_update_birth);
	      pCenterWest.add(tf_user_update_birth);

	      pCenterCenter.add(new Label("회원 정보 현황", Label.CENTER), "North");
	      pCenterCenter.add(ta_user_update_list, "Center");


	      pCenter.add(pCenterWest, "West");
	      pCenter.add(pCenterCenter, "Center");
	      p_main.add(pCenter, "Center");

	      Panel pSouth = new Panel(new BorderLayout(5, 5));
	      Panel pSouthSouth = new Panel();
	      lb_user_update_msg = new Label("");
	      bt_user_update = new Button("수정하기");
	      pSouth.add(lb_user_update_msg);
	      pSouthSouth.add(bt_user_update, "South");
	      pSouth.add(pSouthSouth, "South");
	      p_main.add(pSouth, "South");

	      bt_user_update_uinfo.addActionListener(this);
	      bt_user_update.addActionListener(this);
	   }

	   
	
	//책 정보 등록 화면 메서드
	public void bookAddView(String str) {
		p_main = new Panel(new BorderLayout(10, 10));
		lb_book_add_title = new Label("책 정보 등록", Label.CENTER);
		p_main.add(lb_book_add_title, "North");

		Panel p_center_temp = new Panel(new BorderLayout(20, 20));
		Panel p_center_temp_west = new Panel(new GridLayout(8, 1, 5, 5));
		Panel p_center_temp_center = new Panel(new BorderLayout(5, 5));

		lb_book_add_bname = new Label("도서명 :                        ");
		lb_book_add_genre = new Label("장르 :                        ");
		lb_book_add_author = new Label("저자 :                        ");
		lb_book_add_publisher = new Label("출판사 :                        ");
		tf_book_add_bname = new TextField("");
		tf_book_add_genre = new TextField("");
		tf_book_add_author = new TextField("");
		tf_book_add_publisher = new TextField("");
		ta_book_add_list = new TextArea(str);
		ta_book_add_list.setEditable(false);
		
		p_center_temp_west.add(lb_book_add_bname);
		p_center_temp_west.add(tf_book_add_bname);
		p_center_temp_west.add(lb_book_add_genre);
		p_center_temp_west.add(tf_book_add_genre);
		p_center_temp_west.add(lb_book_add_author);
		p_center_temp_west.add(tf_book_add_author);
		p_center_temp_west.add(lb_book_add_publisher);
		p_center_temp_west.add(tf_book_add_publisher);

		p_center_temp_center.add(new Label("책 정보 현황", Label.CENTER), "North");
		p_center_temp_center.add(ta_book_add_list, "Center");
		p_center_temp.add(p_center_temp_west, "West");
		p_center_temp.add(p_center_temp_center, "Center");
		p_main.add(p_center_temp, "Center");

		Panel p_south_temp = new Panel(new BorderLayout(5, 5));
		Panel p_south_south = new Panel();
		lb_book_add_msg = new Label("");
		bt_book_add = new Button("등록하기");
		p_south_temp.add(lb_book_add_msg);
		p_south_south.add(bt_book_add, "South");
		p_south_temp.add(p_south_south, "South");
		p_main.add(p_south_temp, "South");

		bt_book_add.addActionListener(this);
	}
	
	
	public void bookDeleteView(String str) {
		p_main = new Panel(new BorderLayout(10,10));
		lb_book_delete_title = new Label("책 정보 삭제", Label.CENTER);
		p_main.add(lb_book_delete_title, "North");
		
		Panel p_center_temp = new Panel(new BorderLayout(20,20));
		Panel p_center_temp_west = new Panel(new GridLayout(6,2,5,13));
		Panel p_center_temp_center = new Panel(new BorderLayout(5,5));
		
		lb_book_delete_bid = new Label("책 번호 : ");
		tf_book_delete_bid = new TextField("");
		ta_book_delete_list = new TextArea(str);
		ta_book_delete_list.setEditable(false);		
		
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(lb_book_delete_bid);
		p_center_temp_west.add(tf_book_delete_bid);
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(new Label());
		
		p_center_temp_center.add(new Label("책 정보 현황",Label.CENTER), "North");
		p_center_temp_center.add(ta_book_delete_list, "Center");
		p_center_temp.add(p_center_temp_west, "West");
		p_center_temp.add(p_center_temp_center, "Center");
		p_main.add(p_center_temp, "Center");
		
		Panel p_south_temp = new Panel(new BorderLayout(5,5));
		Panel p_south_south = new Panel();
		lb_book_delete_msg = new Label("");
		bt_book_delete = new Button("삭제하기");
		p_south_temp.add(lb_book_delete_msg);
		p_south_south.add(bt_book_delete, "South");
		p_south_temp.add(p_south_south, "South");
		p_main.add(p_south_temp, "South");
		
		bt_book_delete.addActionListener(this);
	}
	
	
	//책 대여 및 반납 화면 메서드
	public void bookLendView(String str) {	
		p_main = new Panel(new BorderLayout(10,10));
		lb_book_lend_title = new Label("책 대여 및 반납", Label.CENTER);
		p_main.add(lb_book_lend_title, "North");
		
		Panel p_center_temp = new Panel(new BorderLayout(20,20));
		Panel p_center_temp_west = new Panel(new GridLayout(6,2,5,16));
		Panel p_center_temp_center = new Panel(new BorderLayout(5,5));
		
		lb_book_lend_pid = new Label("회원 번호 : ");
		tf_book_lend_pid = new TextField("");
		lb_book_lend_bid = new Label("책 번호     : ");
		tf_book_lend_bid = new TextField("");
		ta_book_lend_list = new TextArea(str,0,0,ta_book_lend_list.SCROLLBARS_VERTICAL_ONLY);
		ta_book_lend_list.setEditable(false);		
		
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(new Label());
		p_center_temp_west.add(lb_book_lend_pid);
		p_center_temp_west.add(tf_book_lend_pid);
		p_center_temp_west.add(lb_book_lend_bid);
		p_center_temp_west.add(tf_book_lend_bid);
		p_center_temp_west.add(new Panel());
		p_center_temp_center.add(new Label("책 대여 현황",Label.CENTER), "North");
		p_center_temp_center.add(ta_book_lend_list, "Center");
		p_center_temp.add(p_center_temp_west, "West");
		p_center_temp.add(p_center_temp_center, "Center");
		p_main.add(p_center_temp, "Center");
		
		Panel p_south_temp = new Panel(new BorderLayout(5,5));
		Panel p_south_south = new Panel();
		lb_book_lend_msg = new Label("");
		bt_book_lend = new Button("대여하기");
		bt_book_return = new Button("반납하기");
		p_south_temp.add(lb_book_lend_msg);
		p_south_south.add(bt_book_lend);
		p_south_south.add(bt_book_return);
		p_south_temp.add(p_south_south, "South");
		p_main.add(p_south_temp, "South");
		
		bt_book_lend.addActionListener(this);
		bt_book_return.addActionListener(this);
	}
	

	//검색하기 화면 메서드
	public void searchInfoView() {
		p_main = new Panel(new BorderLayout(10,10));
		lb_search_info_title = new Label("검색하기", Label.CENTER);
		p_main.add(lb_search_info_title, "North");
		
		Panel p_center_temp = new Panel(new BorderLayout(5, 5));
		Panel p_top = new Panel(new FlowLayout(FlowLayout.CENTER));
		
		Panel p_tf_and_bt = new Panel(new GridLayout(1, 4, 5, 5));
		cg_options = new CheckboxGroup();
		cb_book = new Checkbox("도서명으로 검색", cg_options, true);
		cb_person = new Checkbox("사람 이름으로 검색", cg_options, false);
		tf_search_info_key = new TextField();
		bt_search_info = new Button("검색");
		p_tf_and_bt.add(cb_book);
		p_tf_and_bt.add(cb_person);
		p_tf_and_bt.add(tf_search_info_key);
		p_tf_and_bt.add(bt_search_info);
		p_top.add(p_tf_and_bt);
		p_center_temp.add(p_top, "North");
		
		ta_search_info_show_result = new TextArea("");
		ta_search_info_show_result.setEditable(false);
		
		p_center_temp.add(ta_search_info_show_result);
		
		p_main.add(p_center_temp, "Center");
		
		bt_search_info.addActionListener(this);
	}
	

	// 연체정보 화면 메서드
	public void delayInfoView() throws Exception {
		
		p_main = new Panel(new BorderLayout(10, 10));
		lb_delay_info_title = new Label("연체 회원 정보", Label.CENTER);; 
		
		
		p_main_c = new Panel(new BorderLayout(5, 5));
		p_main_s = new Panel(new GridLayout(2,1));
		Panel p_main_n=new Panel(new FlowLayout());
		
		p_main.add(p_main_n,"North"); 
		p_main.add(p_main_c, "Center");
		p_main.add(p_main_s, "South");
		
		p_main_n.add(lb_delay_info_title);
		
		Panel p_main_c_n=new Panel(new GridLayout(1,4,10,10));
		p_main_c.add(p_main_c_n,"North");
		
		Label l_book_id_sub=new Label("대여번호");
		Label l_person_name_sub=new Label("회원이름");
		Label l_book_name_sub=new Label("대여날짜");
		Label l_event_time_sub=new Label("책제목");
		p_main_c_n.add(l_book_id_sub);
		p_main_c_n.add(l_person_name_sub);
		p_main_c_n.add(l_book_name_sub);
		p_main_c_n.add(l_event_time_sub);
		
		l_delay=new List(0,false);
		l_time_cal=new List(0,false);
		p_main_c.add(l_delay,"Center");
		
		p_main_s_one=new Panel(new GridLayout(1,3));
		Panel p_main_s_two=new Panel(new FlowLayout());
		
		p_main_s.add(p_main_s_one);
		p_main_s.add(p_main_s_two);
		
		lb_time = new Label(" ");
		
		startTimer();
		
		p_main_s_one.add(lb_time);
		p_main_s_one.add(new Label(" "));
		
		
		bt_delay_info_reload = new Button("새로고침");
		bt_delay_info_reload.setSize(100, 100);
		p_main_s_two.add(bt_delay_info_reload, "South");
		
		day_delay=new Label(" "); 
		l_delay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					p_main_s_one.remove(day_delay);
					String[] selected=l_delay.getSelectedItem().split(" ");
					
					try {
						SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd");
						Date selectedDate = time_format.parse(selected[selected.length-23]);
						Date now = new Date();

						long delay_time = (now.getTime() - selectedDate.getTime()) / (24 * 60 * 60 * 1000);

						day_delay = new Label("연체일 : " + (delay_time - features.getDeadline()));
						p_main_s_one.add(day_delay);

						p_main_c.revalidate();
						p_main_c.repaint();
					} catch (Exception ex) {
						ex.printStackTrace();
						p_main_c.revalidate();
						p_main_c.repaint();
					}
				}
				
			}
			
		});
		

		
		bt_delay_info_reload.addActionListener(this);
		p_main.revalidate();
		p_main.repaint();
	}
	
	public void startTimer() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						updateTimeLabel();
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					return;
				}
			}
		}).start();
	}

	public void updateTimeLabel() {
		Date now = new Date();
		SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedNow = time_format.format(now);
		EventQueue.invokeLater(() -> lb_time.setText("현재시간 : " + formattedNow));
	}
	
	
	public void topTenView() {
		p_main = new Panel(new BorderLayout(20, 20));
		lb_rank_info_title = new Label("인기도서 Top10", Label.CENTER);
		p_main_rank = new Panel(new BorderLayout());
		p_main_rank_center = new Panel(new GridLayout(11,1,2,2));
		p_main_rank_center.add(lb_rank_info_title);
	
		p_main_rank.add(p_main_rank_center, "Center");
		p_main.add(p_main_rank, "Center");
	}



}