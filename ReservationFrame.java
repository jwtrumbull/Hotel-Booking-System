package hotel;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.util.*;
import java.text.*;

/**
 * frame that allows users to book hotel stay
 * 
 * @author anhth
 *
 */
public class ReservationFrame extends JFrame {

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
	 * creates a frame holding the page to make reservations
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ReservationFrame() throws ClassNotFoundException, SQLException {

		this.setName("Reserve a Room");
		this.setSize(1000, 1000);
		this.setLocation(800, 0);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// fields for customer info entry
		JTextField cidenter = new JTextField(""); // cID
		JPanel customerCID = addOptions("Customer ID", cidenter);

		JTextField hotelenter = new JTextField(""); // hotel name
		JPanel hotelChoice = addOptions("Hotel Choice", hotelenter);

		JTextField roomEnter = new JTextField(""); // hotel name
		JPanel roomChoice = addOptions("Room Choice", roomEnter);

		JTextField checkenter = new JTextField(""); // check In
		JPanel checkin = addOptions("Check In Date", checkenter);

		JTextField checkoutenter = new JTextField(""); // check Out
		JPanel checkout = addOptions("Check Out Date", checkoutenter);

		JTextArea rslt = new JTextArea(30, 20);
		rslt.setText("Booking Now");

		// creates reservation
		JButton submit = new JButton("Enter");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = "";
				try {
					// query to get selected hotel

					stmt = conn.createStatement();
					stmt.executeUpdate("UPDATE Room SET reserved=true WHERE roomNumber=" + roomEnter.getText()
							+ " AND hotelName='" + hotelenter.getText() + "'");

					stmt = conn.createStatement();
					stmt.execute(
							"DROP TRIGGER IF EXISTS updateResRoom" );
				
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");
					String currentDate = (LocalDate.now().format(formatter));
					//triggers insert into reservation when room is updated (if reservation is not already existing)
					stmt = conn.createStatement();
					stmt.execute(
							"CREATE TRIGGER updateResRoom "
							+ "AFTER UPDATE on Room " 
							+ "FOR EACH ROW "
							+ "BEGIN IF " + cidenter.getText() + " not IN (select cID from Reservation) THEN "
							+ "INSERT into RESERVATION VALUES ("+ cidenter.getText() +  ",'"+ hotelenter.getText() +"', '"+checkenter.getText()+"', '"+checkoutenter.getText()+"', '"+currentDate+"'); END IF; END;");
			
					// prints reservation information
					stmt = conn.createStatement();
					rs = stmt.executeQuery("select cID, hotelName, checkIn, checkOut from RESERVATION where cID="
							+ (cidenter.getText()));
					while (rs.next()) {
						str = str + ("Customer ID=" + rs.getString("cID") + "\nHotel Name=" + rs.getString("hotelName")
								+ "\nCheck In Date=" + rs.getDate("checkIn") + "\nCheck Out Date="
								+ rs.getDate("checkOut") + "\n------------\n");
						rslt.setText(str + "Reservation Complete");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		this.add(customerCID);
		this.add(hotelChoice);
		this.add(roomChoice);
		this.add(checkin);
		this.add(checkout);
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
		tf.setText("");
		tf.setColumns(10);
		customer.add(nameLabel, BorderLayout.WEST);
		customer.add(tf, BorderLayout.EAST);
		return customer;
	}

}