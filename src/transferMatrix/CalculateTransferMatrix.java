package transferMatrix;

import entities.Community;
import entities.CommunitySet;
import entities.SequenceCount;
import sqlConnect.CommunityTableAccess;
import sqlConnect.DBHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculateTransferMatrix {
    // TODO: remember to close the dbHelper.
    private DBHelper dbHelper;
    private CommunitySet communities;
    public static double minSimilarity = 0.5;
    private static int markovSeqLength = 3;
    private static int clusterLevel = 3;

    private SequenceCount sequenceCount;

    public CalculateTransferMatrix() {
        dbHelper = new DBHelper();
        communities = new CommunitySet();
        sequenceCount = new SequenceCount();
    }

    public void calculateTransferPossibility(int communityIndex) throws Exception {
        communityIndex--;
        if (communityIndex < 0 ||
                communityIndex > communities.getCommunities().size()) {
            System.out.println("Community Number not correct!");
            return;
        }
        for (int userId : communities.getCommunity(communityIndex).getUserGroup()) {
            ResultSet locationSequenceResultSet = dbHelper.getResult(
                    "SELECT location_id FROM NextPre.pre_seq " +
                            "WHERE user_id = " + userId + ";");
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

    public void calculateSingleUserTransferPossibility(int userId) throws Exception {
        SequenceCount count = new SequenceCount();

        ResultSet locationSequenceResultSet = dbHelper.getResult(
                "SELECT location_id FROM NextPre.user_seq " +
                        "WHERE user_id = " + userId + ";");
        List<Integer> singleUserSequence = new ArrayList<>();
        while (locationSequenceResultSet.next()) {
            singleUserSequence.add(locationSequenceResultSet.getInt(1));
        }
        for (int i = 0; i <= 0.8 * singleUserSequence.size() - markovSeqLength; i++) {
            List<Integer> sequenceList = new ArrayList<>();
            for (int j = i; j < i + markovSeqLength; j++) {
                sequenceList.add(singleUserSequence.get(j));
            }
            count.addSequence(sequenceList);
        }
        locationSequenceResultSet.close();

        CommunityTableAccess access = new CommunityTableAccess();
        access.clearUserSequence(userId);
        access.prepareUserSequenceStatement();
        for (Map.Entry<List<Integer>, Double> entry :
                count.getSequenceProportion().entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer sequenceNumber : entry.getKey())
                stringBuilder.append(sequenceNumber).append(" ");
            String sequenceString = stringBuilder.toString().trim();
            access.insertSequence(userId, sequenceString, entry.getValue());
        }
        access.close();
    }

    public void calculateUsersInCommunity(int communityIndex) throws Exception {
        communityIndex--;
        for (int userId : communities.getCommunity(communityIndex).getUserGroup()) {
            calculateSingleUserTransferPossibility(userId);
        }
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
        ResultSet userIdResultSet = dbHelper.getResult(
                "SElECT UserId FROM NextPre.Communities;");
        List<Integer> userIdList = new ArrayList<>();
        while (userIdResultSet.next()) {
            userIdList.add(userIdResultSet.getInt(1));
        }
        communities.addCommunity(new Community(1, userIdList));
    }

    public void clearCommunitiesFromSQL() throws Exception {
        CommunityTableAccess access = new CommunityTableAccess();
        access.clearCommunity();
        access.close();
    }

    public void close() {
        dbHelper.close();
    }
}
