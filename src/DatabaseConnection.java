import java.sql.*;

public class DatabaseConnection {
	
//	连接数据库 MachineLearning，用户名root 密码123456，重启电脑时需要重新设置密码（原因不明）

	private Connection c;
	
	public DatabaseConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e)
		{
			System.out.println("找不到驱动程序类，加载失败！");
			e.printStackTrace();
		}
		
		String url = "jdbc:mysql://localhost:3306/NextPre?characterEncoding=utf8&useSSL=true&useOldAliasMetadataBehavior=true";
		String name = "root";
		String password = "094213";
		
		try
		{
			c = DriverManager.getConnection(url, name, password);
		}catch(SQLException e)
		{
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}

	}
	
	public Connection getConnection()
	{
		return c;
	}
	
	public void closeConnection()
	{
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
