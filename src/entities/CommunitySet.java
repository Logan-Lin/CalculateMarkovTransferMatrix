package entities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommunitySet {
    private List<Community> communities;

    public CommunitySet() {
        communities = new ArrayList<>();
    }

    public void addCommunity(Community community) {
        communities.add(community);
    }

    public void newCommunity(int userId1, int userId2) throws SQLException {
        Community newCommunity = new Community(communities.size() + 1);
        newCommunity.addUser(userId1);
        newCommunity.addUser(userId2);
        communities.add(newCommunity);
    }

    public int getCommunityIndex(int userId) {
        for (int i = 0; i < communities.size(); i++) {
            if (communities.get(i).isInCommunity(userId)) {
                return i;
            }
        }
        return -1;
    }

    public Community getCommunity(int index) {
        return communities.get(index);
    }

    public List<Community> getCommunities() {
        return communities;
    }
}
