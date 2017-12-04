# Form Markov transfer matrix model using users' similarity and cluster sequence information

Transfer matrix is a must-have in next prediction using Markov. For the simplest approach, we can use statistic method to calculate transfer matrix.

But calculate an individual transfer matrix for everyone is not very practical, considering there are many user there having very short cluster sequence. So in this project, we also utilize users' similarity information. A set of users who have a high enough similarity can be count together to form one single transfer matrix.