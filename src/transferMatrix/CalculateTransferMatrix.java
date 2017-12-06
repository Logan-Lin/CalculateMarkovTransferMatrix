package transferMatrix;

import entities.*;
import sqlConnect.*;

import java.sql.*;
import java.util.*;

public class CalculateTransferMatrix {
    // TODO: remember to close the dbHelper.
    private DBHelper dbHelper;
    private CommunitySet communities;
    public static double minSimilarity = 1;
    public static int markovSeqLength = 3;
    public static int clusterLevel = 3;

    private SequenceCount sequenceCount;

    public CalculateTransferMatrix() {
        dbHelper = new DBHelper();
        communities = new CommunitySet();
        sequenceCount = new SequenceCount();
    }

    public void formCommunities() throws SQLException{
        ResultSet similarityResultSet = dbHelper.getResult(
                "SELECT user_id1, user_id2, Similar " +
                "FROM NextPre.OPTICS_Similar WHERE Similar >= " +
                minSimilarity + " ORDER BY Similar DESC;");
        while(similarityResultSet.next()) {
            int user_id1 = similarityResultSet.getInt(1);
            int user_id2 = similarityResultSet.getInt(2);
            int index1 = communities.getCommunityIndex(user_id1);
            int index2 = communities.getCommunityIndex(user_id2);
            if (index1 == -1 && index2 == -1) {
                // If both users are not in any community
                communities.newCommunity(user_id1, user_id2);
            } else {
                if (index1 != -1) {
                    // If user1 is already in a community
                    Community community = communities.getCommunity(index1);
                    community.addUser(user_id2);
                } else {
                    // If user2 is already in a community
                    Community community = communities.getCommunity(index2);
                    community.addUser(user_id1);
                }
            }
        }
        similarityResultSet.close();
    }

    public void calculateTransferPossibility(int communityIndex) throws Exception {
        if (communityIndex < 0 ||
                communityIndex > communities.getCommunities().size()) {
            System.out.println("Community Number not correct!");
            return;
        }
        for (int userId : communities.getCommunity(communityIndex).getUserGroup()) {
            ResultSet locationSequenceResultSet = dbHelper.getResult(
                    "SELECT location_id FROM NextPre.OPTICS_Seq " +
                            "WHERE user_id = " + userId + " AND level = " + clusterLevel + ";");
            List<Integer> singleUserSequence = new ArrayList<>();
            while (locationSequenceResultSet.next()) {
                singleUserSequence.add(locationSequenceResultSet.getInt(1));
            }
            for (int i = 0; i <= singleUserSequence.size() - markovSeqLength; i++) {
                List<Integer> sequenceList = new ArrayList<>();
                for (int j = i; j < i + markovSeqLength; j++) {
                    sequenceList.add(singleUserSequence.get(j));
                }
                sequenceCount.addSequence(sequenceList);
            }
            locationSequenceResultSet.close();
        }
    }

    public void writeCommunitiesIntoSql() throws Exception {
        CommunityTableAccess access = new CommunityTableAccess();
        access.clearCommunity();
        access.prepareCommunityStatement();
        for (Community community : communities.getCommunities()) {
            for (int i = 0; i < community.getUserGroup().size(); i++) {
                access.insertCommunity(community.getCommunityNumber(), i + 1,
                        community.getUserGroup().get(i));
            }
        }
        access.close();
    }

    public void writeSequencePossibilityIntoSQL(int communityNumber) throws Exception {
        CommunityTableAccess access = new CommunityTableAccess();
        access.clearSequence(communityNumber);
        access.prepareSequenceStatement();
        for (Map.Entry<List<Integer>, Double> entry :
                sequenceCount.getSequenceProportion().entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer sequenceNumber : entry.getKey())
                stringBuilder.append(sequenceNumber).append(" ");
            String sequenceString = stringBuilder.toString().trim();
            access.insertSequence(communityNumber, sequenceString, entry.getValue());
        }
        access.close();
    }

    public void readCommunitiesFromSQL() throws Exception {
        ResultSet communityResultSet = dbHelper.getResult(
                "SElECT CommunityNumber FROM NextPre.Communities " +
                        "GROUP BY CommunityNumber ORDER BY CommunityNumber DESC;");
        communityResultSet.next();
        int communitiesCount = communityResultSet.getInt(1);
        communityResultSet.close();

        for (int communityNo = 1; communityNo <= communitiesCount; communityNo++) {
            ResultSet userIdResultSet = dbHelper.getResult(
                    "SElECT UserId FROM NextPre.Communities " +
                            "WHERE CommunityNumber = "+ communityNo + ";");
            List<Integer> userIdList = new ArrayList<>();
            while (userIdResultSet.next()) {
                userIdList.add(userIdResultSet.getInt(1));
            }
            communities.addCommunity(new Community(communityNo, userIdList));
        }
    }

    public void close() {
        dbHelper.close();
    }
}
