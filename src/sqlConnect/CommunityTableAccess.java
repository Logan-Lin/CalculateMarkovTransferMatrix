package sqlConnect;

import java.sql.*;

public class CommunityTableAccess {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public CommunityTableAccess() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/NextPre?autoReconnect=true&useSSL=false",
                "root", "094213");
        preparedStatement = connection.prepareStatement("INSERT INTO NextPre.Communities " +
                "(CommunityNumber, UserSequence, UserId) " +
                "VALUE " +
                "(?, ?, ?)");
    }

    public void insertSQL(int communityNumber, int userSequence, int userId)
            throws Exception {
        // Parameters start with 1
        preparedStatement.setInt(1, communityNumber);
        preparedStatement.setInt(2, userSequence);
        preparedStatement.setInt(3, userId);
        preparedStatement.executeUpdate();
    }

    public void clearTable() throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM NextPre.Communities");
        preparedStatement.execute();
    }

    // You need to close the resultSet
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
