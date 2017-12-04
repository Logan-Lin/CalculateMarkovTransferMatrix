package sqlConnect;

import java.sql.*;

public class DBHelper {
    private static final String url = "jdbc:mysql://localhost/NextPre?autoReconnect=true&useSSL=false";
    private static final String name = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "094213";
    private Statement statement = null;

    private Connection connection = null;

    public DBHelper() {
        try {
            Class.forName(name);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String sql){
        ResultSet result = null;
        try {
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
