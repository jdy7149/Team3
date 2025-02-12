package team3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BookAdminMain {

	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "scott";
			String pwd = "1234";
			Connection con = DriverManager.getConnection(url,user,pwd);
			
			// GUI 생성
			BookAdminGui gui = new BookAdminGui();
			
			// DB 연동기능 생성
			BookAdmin features = new BookAdmin(gui, con);
			
			// DB 여농기능 탑재
			gui.setFeatures(features);
			
			gui.setSize(600,400);
			gui.setLocation(730,250);
			gui.setVisible(true);
			gui.setResizable(false);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
	}

}
