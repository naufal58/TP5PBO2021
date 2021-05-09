/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Fauzan
 */
public class dbConnection {
    public static Connection con;
    public static Statement stm;
    
    public void connect(){//untuk membuka koneksi ke database
        try {
            String url ="jdbc:mysql://localhost/db_gamepbo";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            System.out.println("koneksi berhasil;");
        } catch (Exception e) {
            System.err.println("koneksi gagal" +e.getMessage());
        }
    }
    
    public DefaultTableModel readTable(){
        
        DefaultTableModel dataTabel = null;
        try{
            Object[] column = {"No", "Username", "Score"};
            connect();
            dataTabel = new DefaultTableModel(null, column){
                Class[] types = {String.class, String.class, Number.class};
                @Override
                public Class getColumnClass(int columnIndex){
                    return this.types[columnIndex];
                }
//                @Override
//                public Class getColumnClass(int c){
//                    return getValueAt(2,c).getClass();
//                }
            };
            
            String sql = "Select * from highscore order by score desc";
            ResultSet res = stm.executeQuery(sql);
            
            int no = 1;
            while(res.next()){
                Object[] hasil = new Object[3];
                hasil[0] = no;
                hasil[1] = res.getString("Username");
                hasil[2] = res.getString("Score");
                no++;
                dataTabel.addRow(hasil);
            }
        }catch(Exception e){
            System.err.println("Read gagal " +e.getMessage());
        }
        
        return dataTabel;
    }
    
    public void uploadScore(String username, int score){
        connect();
        String sql = "INSERT INTO highscore VALUES(NULL, ?, ?)";
        PreparedStatement pstmt;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, String.valueOf(score));
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Upload gagal " +e.getMessage());
        }
    }
}
