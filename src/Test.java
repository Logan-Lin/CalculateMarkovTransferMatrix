import sqlConnect.DBHelper;
import transferMatrix.CalculateTransferMatrix;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        CalculateTransferMatrix testSample = new CalculateTransferMatrix();
//        testSample.formCommunities();
//        testSample.writeCommunitiesIntoSql();
//        testSample.close();
//        validateCommunities(1);
        testSample.readCommunitiesFromSQL();
        testSample.calculateTransferPossibility(1);
    }

    public static void validateCommunities(int communityNumber) throws Exception {
        // TODO: remember to close the dbHelper.
        DBHelper dbHelper = new DBHelper();
        List<Integer> userList = new ArrayList<>();

        ResultSet resultSet = dbHelper.getResult(
                "SELECT UserId FROM NextPre.Communities " +
                        "WHERE CommunityNumber = " + communityNumber + ";");
        while (resultSet.next()) {
            userList.add(resultSet.getInt(1));
        }
        resultSet.close();
        for (int user1 : userList) {
            for (int user2 : userList) {
                if (user1 == user2) continue;
                int userId1 = user1 < user2 ? user1 : user2;
                int userId2 = user1 < user2 ? user2 : user1;
                resultSet = dbHelper.getResult("SELECT Similar FROM " +
                        "NextPre.OPTICS_Similar WHERE user_id1 = '" + userId1 +
                        "'AND user_id2 = '" + userId2 + "';");
                if (resultSet.next()) {
                    System.out.println(userId1 + "\t" + userId2 + "\t" + resultSet.getDouble(1));
                }
            }
        }
        resultSet.close();
        dbHelper.close();
    }
}
