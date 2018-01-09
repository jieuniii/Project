package DB_Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql_con {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");		
		String query = "select * from NNP limit 3";
		
		try (
				Connection conn = DriverManager.getConnection("jdbc:mysql://1.234.25.136:3306/mecab","hduser","tkdydwk00");
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query);
				
			){
			
			while (rs.next())			
			{
				String word = rs.getString("term");
				String pos = rs.getString("pos");
				System.out.format("%s %s\n", word, pos);
			}
			
			
		} catch(SQLException e){
			
			System.err.println("연결 실패하였습니다.");
			
		}
		
		
	}
}
