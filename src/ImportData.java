import java.io.*;
import java.sql.*;

public class ImportData {

//	创建MachineLearning表格，把初始GPS点集导入到数据库中

    private Connection c;

    public ImportData() {

        DatabaseConnection d = new DatabaseConnection();

        this.c = d.getConnection();

        Statement s = null;
        try {
            s = c.createStatement();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        String sql = "CREATE TABLE GPS (num INT  NOT NULL PRIMARY KEY, Id VARCHAR(255), Latitude DOUBLE,Longitude DOUBLE,Date DATE,Time TIME, index Date (DATE, TIME), index Id (Id));";

        try {
            s.executeUpdate(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }


        PreparedStatement pstmt = null;
        try {
            pstmt = c.prepareStatement("INSERT INTO GPS VALUES(?,?,?,?,?,?)");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        File files[] = new File("/Users/loganlin/Downloads/Data/").listFiles();

        int i = 0;
        int num = 1;

        for (File file : files) {
            if (i == 0) {
                i = 1;
                continue;
            }
            File f[] = file.listFiles();
            System.out.println(file);

//            if (file.getName().equals(".DS_Store"))
//                continue;
            for (File ff :
                    f) {
//				System.out.println(ff);
                if (!ff.getName().equals("Trajectory"))
                    continue;

                File fff[] = ff.listFiles();


                for (File ffff : fff) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(ffff));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String temp = null;

                    for (i = 0; i < 7; i++) {
                        try {
                            temp = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    while (temp != null) {
                        int n[] = new int[6];
                        int count = 0;
                        for (int i1 = 0; i1 < temp.length(); i1++) {
                            if (temp.charAt(i1) == ',') {
                                n[count] = i1;
                                count++;
                            }
                        }
                        String str[] = new String[7];
                        str[0] = temp.substring(0, n[0]);
                        str[1] = temp.substring(n[0] + 1, n[1]);
                        str[2] = temp.substring(n[1] + 1, n[2]);
                        str[3] = temp.substring(n[2] + 1, n[3]);
                        str[4] = temp.substring(n[3] + 1, n[4]);
                        str[5] = temp.substring(n[4] + 1, n[5]);
                        str[6] = temp.substring(n[5] + 1);

                        try {
                            temp = br.readLine();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }

                        double num1;
                        double num2;
                        Date num6 = null;
                        Time num7 = null;

                        num1 = Double.valueOf(str[0]);
                        num2 = Double.valueOf(str[1]);
                        num6 = Date.valueOf(str[5]);
                        num7 = Time.valueOf(str[6]);

                        String id = file.getName();

                        try {
                            pstmt.setInt(1, num);
                        } catch (SQLException e3) {
                            e3.printStackTrace();
                        }
                        try {
                            pstmt.setString(2, id);
                        } catch (SQLException e2) {
                            e2.printStackTrace();
                        }
                        try {
                            pstmt.setDouble(3, num1);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            pstmt.setDouble(4, num2);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            pstmt.setDate(5, num6);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            pstmt.setTime(6, num7);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            pstmt.addBatch();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                        num++;
                    }
                    if (br != null)
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
            try {
                pstmt.executeBatch();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }


        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        d.closeConnection();

        if (s != null)
            try {
                s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
