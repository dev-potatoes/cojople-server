package io.mojolll.project.excelfilereader;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ExcelConnection {
    private Connection getConnection() {
        Map<String, Object> datasource = YmlReader.datasourceReader("application.yml");
        String dbUrl = (String) datasource.get("url");
        String id = (String) datasource.get("username");
        String password = (String) datasource.get("password");
        String driverClassName = (String) datasource.get("driver-class-name");
        Connection conn = null;

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e){
            e.getMessage();
            System.out.println("연결되지 않았습니다.");
        }
        try {
            conn = DriverManager.getConnection(dbUrl,id,password);
            System.out.println("연결에 성공했습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

     public void excute(List<Map<Object, Object>> list) throws IOException {
        Connection  conn  = null;
        PreparedStatement pstmt = null;
        String query = "INSERT INTO university (type,university_id,name,branch,domain) values (?, ?, ?, ?, ?)";

        System.out.println("총 라인 수 : "+list.size());

        try {
            conn = getConnection();	//데이터베이스 연결
            pstmt = conn.prepareStatement(query);	//쿼리 설정

            for(int i=1; i<list.size(); i++) {      //매개변수로 받아온 ArrayList 의 길이만큼 반복한다.

                //읽어온 각 셀들이 자신이 생성해준 table 제약조건과 일치하지 않을 경우 SqlException이 발생한다.
                //그러한 조건이 발생하면 continue 를 해주는 부분을 추가해주면 된다.
                if(list.get(i).isEmpty()) continue;	//행에 값이 없을 경우에 그 행을 제외하고 진행
                //앞의 쿼리에서 물음표에 들어갈 항목들을 순서대로 기입
                pstmt.setString(1, (String)list.get(i).get("type"));
                pstmt.setInt(2, Integer.parseInt((String) list.get(i).get("university_id")));
                pstmt.setString(3, (String)list.get(i).get("name"));
                pstmt.setString(4, (String)list.get(i).get("branch"));
                pstmt.setString(5, (String)list.get(i).get("domain"));

                //update query 실행
                pstmt.executeUpdate();
            }

            System.out.println("insert를 완료했습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace();}
            if(conn  != null) try { conn.close();  } catch (SQLException e) { e.printStackTrace();}
        } // DB 연결에 사용한 객체와 Query수행을 위해 사용한 객체를 닫는다.
    }
}
