

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class RatingFrame extends JFrame {

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
	 * creates frame for user to rate hotels
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public RatingFrame() throws ClassNotFoundException, SQLException {
		this.setName("Rating Frame");
		this.setSize(1000, 1000);
		this.setLocation(800, 0);
		this.setTitle("Rate");

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection("jdbc:mysql://localhost/hbs?user=root&password=password");

		// customer fields to enter (adding name and age)
		JTextField customerEnter = new JTextField("");
		JPanel customerID = addOptions("ID", customerEnter);
		
		//customer name
		
		JTextField customerNameEntry = new JTextField("");
		JPanel customerName = addOptions("Your Name", customerNameEntry);

		
		
		JTextField hotelenter = new JTextField("");
		JPanel hotelName = addOptions("Hotel Name", hotelenter);
		
		//new date object
		
		JTextField starsenter = new JTextField("");
		JPanel customerStars = addOptions("Stars (0-5)", starsenter);
		
		JTextArea rslt = new JTextArea(10, 10);
		rslt.setText("Rating");

		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
					java.sql.Date sqlDate = new java.sql.Date(df.parse("12-10-2018").getTime());
					
					stmt = conn.createStatement();
					rslt.setText(customerEnter.getText() + "," + customerNameEntry.getText() + "," +hotelenter.getText()+ "," + sqlDate + "," +starsenter.getText());
					stmt.executeUpdate("INSERT INTO RATING(cID, name, hotelName, rDate, stars) VALUES(" + customerEnter.getText() + ",'" +customerNameEntry.getText()+
							"','"+ hotelenter.getText()+"', '"+sqlDate+"',"+Integer.parseInt(starsenter.getText()) + ")");

					// prints reservation information
					stmt = conn.createStatement();
					rs = stmt.executeQuery(
							"select cID, name from CUSTOMER where name='" + customerEnter.getText() + "'");
					while (rs.next()) {
						rslt.setText("Thanks for signing up, " + customerEnter.getText()
								+ "\n Your Customer ID Number is: " + rs.getInt("cID"));
					}

				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		this.add(customerID);
		this.add(customerName);
		this.add(hotelName);
		this.add(customerStars);
		this.add(submit);
		this.add(rslt);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * creates option buttons for input into the registration/reservation page
	 * 
	 * @param item
	 *            the field for addition e.g., customer name, customer age, etc.,
	 * @return jpanel of jbutton and jfield to add to the frame
	 */
	public JPanel addOptions(String item, JTextField tf) {
		JPanel customer = new JPanel();
		customer.setSize(100, 1000);
		customer.setLayout(new BorderLayout());
		JLabel nameLabel = new JLabel("    " + item + "   ");
		tf.setColumns(10);
		customer.add(nameLabel, BorderLayout.WEST);
		customer.add(tf, BorderLayout.EAST);
		return customer;
	}
}
