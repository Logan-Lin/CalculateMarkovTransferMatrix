# Form Markov transfer matrix model using users' similarity and cluster sequence information

> Transfer matrix is a must-have in next prediction using Markov. For the simplest approach, we can use statistic method to calculate transfer matrix.
> 
> But calculate an individual transfer matrix for everyone is not very practical, considering there are many user there having very short cluster sequence. So in this project, we also utilize users' similarity information. A set of users who have a high enough similarity can be count together to form one single transfer matrix.

## Class and concept explain

### Community

A virtual concept. Every user in a community have a high enough similarity to each other. This can guarantee that all users in this community have similar moving habit. Then we can gather all users' cluster sequence and calculate Markov transfer matrix as one.

This class's public method addUser can determine whether the user provided can be added to the community by query for SQL about the user's similarity with others.

### CommunitySet

A set of community. Public method getCommunityIndex can make it easy to find which community a user is in.

### SequenceCount

Actually a `Map<List<Integer>, Integer>` entity with some method to help count sequence occurrence time.

## How to use

Before running any method, you should create a CalculateTransferMatrix entity. After that, firstly run formCommunities to form users to communities. Then you can run writeCommunitiesIntoSql to save community information to SQL so that next you run this project, just use readCommunitiesFromSQL to restore community data instead of calculate all over again. Finally run calculateTransferPossibility to calculate a community's Markov model's transfer matrix, and save them it to SQL.

> Note that every time you write information into SQL, the program will delete old information first.