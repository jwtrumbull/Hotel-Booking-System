package hotel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
/**
 * allows customers to add their rating
 * @author anhthy, chad, jordan
 *
 */
public class RatingPanel extends JPanel {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/hbs";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	/*
	 * creates panel to search hotels by ratings
	 */
	public RatingPanel() throws SQLException, ClassNotFoundException {

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL,USER,PASS);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel titleHeader = new JLabel("          Hotel Ratings          ");
		titleHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		this.add(titleHeader);
		JTextArea result = new JTextArea("Hotel Ratings");

		// displays hotels by rating and hotel both ascending order
		String str = "";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"select hotelName, avg(stars) as starRate from Rating group by hotelName ORDER by starRate ASC, hotelName ASC");
			while (rs.next()) {
				str = str + ("Hotel Name =" + rs.getString("hotelName") + "\nRating=" + rs.getInt("starRate")
						+ "\n ------------ \n");
				result.setText(str);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// creates button bar to sort hotels by different categories
		JPanel sortOptions = new JPanel();

		JLabel sortLabel = new JLabel("Sort by: ");
		String[] sorts = { "Rating: 1 - 5", "Rating: 5 - 1", "Hotel Name", "Number Of Reviews" };
		JComboBox sOption = new JComboBox(sorts);

		JButton sortEnter = new JButton("Sort");
		sortEnter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = "";
				// sorts by rating 1-5
				if (sOption.getSelectedIndex() == 0) {
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotelName, avg(stars) as starRate from Rating group by hotelName ORDER by starRate ASC, hotelName ASC");
						while (rs.next()) {
							str = str + ("Hotel Name =" + rs.getString("hotelName") + "\nRating="
									+ rs.getInt("starRate") + "\n ------------ \n");
							result.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// sorts by rating 5-1
				else if (sOption.getSelectedIndex() == 1) {
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotelName, avg(stars) as starRate from Rating group by hotelName ORDER by starRate DESC, hotelName ASC");
						while (rs.next()) {
							str = str + ("Hotel Name =" + rs.getString("hotelName") + "\nRating="
									+ rs.getInt("starRate") + "\n ------------ \n");
							result.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// sorts by hotel name
				else if (sOption.getSelectedIndex() == 2) {
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotelName, avg(stars) as starRate from Rating group by hotelName ORDER by hotelName ASC");
						while (rs.next()) {
							str = str + ("Hotel Name =" + rs.getString("hotelName") + "\nRating="
									+ rs.getInt("starRate") + "\n ------------ \n");
							result.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// sort by number of reviews
				else if (sOption.getSelectedIndex() == 3) {
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(
								"select hotelName, count(cid) as cIDCount from Rating group by hotelName ORDER by hotelName ASC");
						while (rs.next()) {
							str = str + ("Hotel Name =" + rs.getString("hotelName") + "\nReviews="
									+ rs.getInt("cIDCount") + "\n ------------ \n");
							result.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		});

		sortOptions.add(sortLabel);
		sortOptions.add(sOption);
		sortOptions.add(sortEnter);

		this.add(sortOptions);

		this.add(result);
	}
}
