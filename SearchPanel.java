package hotel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import javafx.scene.layout.Border;

/**
 * creates panel to search for specific pool/breakfast/hotel amenities
 * @author anhth
 *
 */
public class SearchPanel extends JPanel {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/hbs";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "password";

	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	public SearchPanel() throws ClassNotFoundException, SQLException {

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		JLabel tf = new JLabel("    Search for a Hotel     ");
		tf.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel p = new JLabel("Pool?");
		String[] pool = { "No", "Yes"};
		JComboBox poolOption = new JComboBox(pool);
		p.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel e = new JLabel("Elevators?");
		String[] el = { "No", "Yes"};
		JComboBox elOption = new JComboBox(el);
		e.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel b = new JLabel("Free Breakfast?");
		String[] bf = { "No", "Yes" };
		JComboBox bfOption = new JComboBox(bf);
		b.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JTextArea rslt = new JTextArea("This hotel matches your search: ");
		JButton enter = new JButton("Hotel Search");
		enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String str = "";
				try {
					// query to get selected hotel
					stmt = conn.createStatement();
					rs = stmt.executeQuery("select hotelName from hotel where pool=" +poolOption.getSelectedIndex() + " AND  elevators="+elOption.getSelectedIndex() +" AND freeBreakfast="+bfOption.getSelectedIndex()); 

					// get results
					while (rs.next()) {
						str = str + ("Hotel Name=" + rs.getString("hotelName")+ "\n");
						rslt.setText(str);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (str.equals("")) {
					// if no hotels match query
					rslt.setText("No hotels match your search");
				}
			}
			
		});
		enter.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		JLabel roomSearch = new JLabel("    Search for a Room     ");
		roomSearch.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel hotel = new JLabel("Select Hotel");
		String[] hotelString = {"", "Aria", "Four Seasons", "Orleans", "Bellagio", "Mandalay", "Motel 6", "Venetian", "Palazzo"};
		JComboBox hotelOption = new JComboBox(hotelString);
		hotel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel  bNumber= new JLabel("Bed Number");
		String[] bn = { "1", "2", "3", "4", "5"};
		JComboBox bnOption = new JComboBox(bn);
		bNumber.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JLabel bType = new JLabel("Bed Type");
		String[] bt = { "King", "Queen" };
		JComboBox btOption = new JComboBox(bt);
		bType.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		JButton enter2 = new JButton("Room Search");
		enter2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String str = "";
				try {
					// query to get selected hotel
					stmt = conn.createStatement();
					if (hotelOption.getSelectedItem().equals("")) {
						rs = stmt.executeQuery("select hotelName, roomNumber, price from room where bedNumber=" + bnOption.getSelectedItem()+" AND bedType='" + btOption.getSelectedItem()+ "'");
					}
					else {
						rs = stmt.executeQuery("select hotelName, roomNumber, price  from room where hotelName ='" + hotelOption.getSelectedItem() +"' AND bedNumber=" + bnOption.getSelectedItem()+" AND bedType='" + btOption.getSelectedItem()+ "'");
					}
					 

					// get results
					while (rs.next()) {
						str = str + ("Hotel Name=" + rs.getString("hotelName") + "\nRoom number=" + rs.getString("roomNumber")+ "\nPrice=" + rs.getInt("price")+ "\n------------\n");
						rslt.setText(str);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (str.equals("")) {
					// if no hotels match query
					rslt.setText("No rooms match your search");
				}
			}
			
		});
		enter2.setAlignmentX(JButton.CENTER_ALIGNMENT);

		this.add(tf);
		this.add(p);
		this.add(poolOption);
		this.add(e);
		this.add(elOption);
		this.add(b);
		this.add(bfOption);
		this.add(enter);
		
		this.add(roomSearch);
		this.add(hotel);
		this.add(hotelOption);
		this.add(bNumber);
		this.add(bnOption);
		this.add(bType);
		this.add(btOption);
		this.add(enter2); 
		
		this.add(rslt);
	}

}
