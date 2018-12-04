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
 * 
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

	/**
	 * creates panel for searching attributes/qualities of hotels
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SearchPanel() throws ClassNotFoundException, SQLException {

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		JLabel tf = new JLabel("    Search for a Hotel     ");
		tf.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// labels for the search fields
		JLabel p = new JLabel("Pool?");
		String[] pool = { "", "False", "True" };
		JComboBox poolOption = new JComboBox(pool);
		p.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel e = new JLabel("Elevators?");
		String[] el = { "", "False", "True" };
		JComboBox elOption = new JComboBox(el);
		e.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel b = new JLabel("Free Breakfast?");
		String[] bf = { "", "False", "True" };
		JComboBox bfOption = new JComboBox(bf);
		b.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel pr = new JLabel("Price");
		String[] bpri = { "", "200", "400", "600", "800" };
		JComboBox prOption = new JComboBox(bpri);
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
					if (prOption.getSelectedIndex() == 0) {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotel.hotelName, price from hotel INNER JOIN room ON hotel.hotelName=room.hotelName where pool LIKE '%"
										+ changeToInt((String) poolOption.getSelectedItem())
										+ "%' AND  elevators LIKE '%" + changeToInt((String) elOption.getSelectedItem())
										+ "%' AND freeBreakfast LIKE '%"
										+ changeToInt((String) bfOption.getSelectedItem()) + "%'");
						while (rs.next()) {
							str = str + ("Hotel Name=" + rs.getString("hotel.hotelName") + "\nPrice="
									+ rs.getString("price") + "\n --------------\n");
							rslt.setText(str);
						}
					} else {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotel.hotelName, price from hotel INNER JOIN room ON hotel.hotelName=room.hotelName where pool LIKE '%"
										+ changeToInt((String) poolOption.getSelectedItem())
										+ "%' AND  elevators LIKE '%" + changeToInt((String) elOption.getSelectedItem())
										+ "%' AND freeBreakfast LIKE '%"
										+ changeToInt((String) bfOption.getSelectedItem()) + "%' AND price <"
										+ prOption.getSelectedItem());
						while (rs.next()) {
							str = str + ("Hotel Name=" + rs.getString("hotel.hotelName") + "\nPrice="
									+ rs.getString("price") + "\n --------------\n");
							rslt.setText(str);
						}

						// get results

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

		// searches for rooms using attributes
		JLabel roomSearch = new JLabel("    Search for a Room     ");
		roomSearch.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel hotel = new JLabel("Select Hotel");
		String[] hotelString = { "", "Aria", "Four Seasons", "Orleans", "Bellagio", "Mandalay", "Motel 6", "Venetian",
				"Palazzo" };
		JComboBox hotelOption = new JComboBox(hotelString);
		hotel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel bNumber = new JLabel("Bed Number");
		String[] bn = { "1", "2", "3", "4", "5" };
		JComboBox bnOption = new JComboBox(bn);
		bNumber.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		JLabel bType = new JLabel("Bed Type");
		String[] bt = { "", "King", "Queen" };
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

					rs = stmt.executeQuery(
							"select hotelName, roomNumber, price, bedType  from room where hotelName LIKE'%"
									+ hotelOption.getSelectedItem() + "%' AND bedNumber =" + bnOption.getSelectedItem()
									+ " AND bedType LIKE '%" + btOption.getSelectedItem() + "%'");

					// get results
					while (rs.next()) {
						str = str + ("Hotel Name=" + rs.getString("hotelName") + "\nRoom number="
								+ rs.getString("roomNumber") + "\nBed Type=" + rs.getString("bedType") + "\nPrice="
								+ rs.getInt("price") + "\n------------\n");
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
		this.add(pr);
		this.add(prOption);
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

	public String changeToInt(String item) {
		String rslt = "";
		if (item.equals("True")) {
			return "1";
		} else if (item.equals("False")) {
			return "0";
		}
		return rslt;
	}

}
