package prowiki;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class ConnectDatabase {

	String drivename = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/github-wikipedia";
	String user = "root";
	String password = "";
	String insql;
	String upsql;
	String delsql;
	String sql;
	
	Connection conn;
	ResultSet rs = null;

	public Connection ConnectMysql() {
		try {
			Class.forName(drivename);
			conn = (Connection) DriverManager.getConnection(url, user, password);
			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			} else {
				System.out.println("Falled connecting to the Database!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	public void CutConnection(Connection conn) throws SQLException {
		try {
			if (rs != null)
				;
			if (conn != null)
				;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			conn.close();
		}
	}

	class exactpage {// �ڲ��࣬���ֶζ�Ӧ������š���ȡ���ݿ��е�����
		int id;
		String title = "";
		JSONObject titlewiki = JSONObject.fromObject(new Object());
		String message = "";
		List<JSONObject> messagewiki = new ArrayList<JSONObject>();

		// ͨ��set�����������ʵ�����š�����
		// ͨ��get�����������ʵ�����á����ݣ�Ȼ����ͨ���������ݿ�
		public void setId(int id) {
			this.id = id;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setTitleWiki(JSONObject titlewiki) {
			this.titlewiki = titlewiki;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		public void setMessageWiki(List<JSONObject> messagewiki) {
			this.messagewiki = messagewiki;
		}
		public int getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public JSONObject getTitleWiki() {
			
			return titlewiki;
		}

		public String getMessage() {
			return message;
		}
		public List<JSONObject> getMessageWiki() {
			return messagewiki;
		}
	}

	// ���롢ɾ�������µķ�����һ���ģ���һ���������ݿ����
	public boolean InsertSql(exactpage exactpage) {
		JSONObject titlewiki = JSONObject.fromObject(new Object());
		String message = "";
		List<JSONObject> messagewiki = new ArrayList<JSONObject>();
		
		try {
			insql = "insert into exactpage(id,title,titlewiki,message,messagewiki) values(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(insql);
			ps.setInt(1, exactpage.getId());
			ps.setString(2, exactpage.getTitle());
			String restitle = "";
			if(exactpage.getTitleWiki().size() != 0) restitle = exactpage.getTitleWiki().toString();
			ps.setObject(3, restitle);
			ps.setString(4, exactpage.getMessage());
			String resmess = "";
			if(exactpage.getMessageWiki().size() != 0) resmess = exactpage.getMessageWiki().toString();
			ps.setObject(5, resmess);
//			System.out.println(exactpage.getId());
//			System.out.println(exactpage.getTitle());
//			System.out.println(exactpage.getTitleWiki());
//			System.out.println(exactpage.getMessage());
//			System.out.println(exactpage.getMessageWiki());
			int result = ps.executeUpdate();
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// ������������Ƚϣ���ѯ����ڲ�ѯ����Ҫһ����ѯ�������ResultSet���������ѯ���
	public void SelectSql(String sql) {
		try {
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("id") + rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean UpdateSql(String upsql) {
		try {
			PreparedStatement ps = conn.prepareStatement(upsql);
			int result = ps.executeUpdate();// ������������0
			if (result > 0)
				return true;
		} catch (SQLException ex) {
			Logger.getLogger(ConnectDatabase.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public boolean DeletSql(String delsql) {

		try {
			PreparedStatement ps = conn.prepareStatement(upsql);
			int result = ps.executeUpdate(delsql);
			if (result > 0)
				return true;
		} catch (SQLException ex) {
			Logger.getLogger(ConnectDatabase.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

}