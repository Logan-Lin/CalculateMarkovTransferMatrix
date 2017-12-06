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
    }

    public void prepareCommunityStatement() throws Exception {
        preparedStatement = connection.prepareStatement(
                "INSERT INTO NextPre.Communities " +
                "(CommunityNumber, UserSequence, UserId) " +
                "VALUE (?, ?, ?)");
    }

    public void prepareSequenceStatement() throws Exception {
        preparedStatement = connection.prepareStatement(
                "INSERT INTO NextPre.SequenceProportion " +
                "(CommunityNo, Sequence, Proportion) VALUE (?, ?, ?)");
    }

    public void insertCommunity(int communityNumber, int userSequence, int userId)
            throws Exception {
        // Parameters start with 1
        preparedStatement.setInt(1, communityNumber);
        preparedStatement.setInt(2, userSequence);
        preparedStatement.setInt(3, userId);
        preparedStatement.executeUpdate();
    }

    public void insertSequence(int communityNo, String sequence, double proportion) throws Exception {
        preparedStatement.setInt(1, communityNo);
        preparedStatement.setString(2, sequence);
        preparedStatement.setDouble(3, proportion);
        preparedStatement.executeUpdate();
    }

    public void clearCommunity() throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM NextPre.Communities");
        preparedStatement.execute();
    }

    public void clearSequence(int communityNo) throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM NextPre.SequenceProportion WHERE CommunityNo = " + communityNo);
        preparedStatement.executeUpdate();
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
