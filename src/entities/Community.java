package entities;

import sqlConnect.DBHelper;
import transferMatrix.CalculateTransferMatrix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Community {
    private int communityNumber;
    private List<Integer> userGroup;

    Community(int communityNumber) {
        this.communityNumber = communityNumber;
        userGroup = new ArrayList<>();
    }

    public Community(int communityNumber, List<Integer> userGroup) {
        this.communityNumber = communityNumber;
        this.userGroup = userGroup;
    }

    public void addUser(int newUserId) throws SQLException {
        DBHelper dbHelper = new DBHelper();
        boolean canAdd = true;
        for (int userId : userGroup) {
            int userId1 = userId < newUserId ? userId : newUserId;
            int userId2 = userId < newUserId ? newUserId : userId;
            ResultSet resultSet = dbHelper.getResult("SELECT Similar FROM " +
                    "NextPre.OPTICS_Similar WHERE user_id1 = '" + userId1 +
                    "'AND user_id2 = '" + userId2 + "';");
            if (resultSet.next()) {
                double similar = resultSet.getDouble(1);
                if (similar < CalculateTransferMatrix.minSimilarity) {
                    canAdd = false;
                    break;
                }
            } else {
                canAdd = false;
            }
        }
        if (canAdd) {
            userGroup.add(newUserId);
        }
        dbHelper.close();
    }

    boolean isInCommunity(int userId) {
        return userGroup.contains(userId);
    }

    public void setCommunityNumber(int communityNumber) {
        this.communityNumber = communityNumber;
    }

    public int getCommunityNumber() {
        return communityNumber;
    }

    public List<Integer> getUserGroup() {
        return userGroup;
    }
}
