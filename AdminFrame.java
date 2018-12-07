package hotel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
/*
 * AdminFrame is where administrator access is allowed
 * Employee can access using the panel with password: password
 * Options allow employee to look at hotel info (occupants, returning customers, etc.,)
 */
public class AdminFrame extends JFrame{
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
		 * creates admin frame 
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public AdminFrame() throws ClassNotFoundException, SQLException {
			
			this.setSize(1000, 1000);
			this.setLocation(800, 0);
			this.setTitle("Front Desk: Administration");
			
			// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 2: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
			
			//panel checks in and checks out customers 
			JPanel checkingRes = new JPanel(); 
			checkingRes.add(new JLabel("Check In and Check Out")); 
			checkingRes.setLayout(new BoxLayout(checkingRes, BoxLayout.Y_AXIS));
			
			JTextField customerEnter = new JTextField("");
			JPanel customerName = addOptions("cID", customerEnter);
			JTextField hotelEnter = new JTextField("");
			JPanel hotelName = addOptions("Hotel Name", hotelEnter);
			JTextField roomEnter = new JTextField("");
			JPanel roomName = addOptions("Room Number", roomEnter);
			
			//checks user in with cID, hotelName, roomNumber
			JButton checkIn = new JButton("Check In");
			checkIn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try {
						stmt = conn.createStatement();
						stmt.executeUpdate("update customer set hotelName ='" + hotelEnter.getText() + "', roomNumber=" + roomEnter.getText() + " where cID=" + customerEnter.getText());
				
						stmt = conn.createStatement();
						stmt.executeUpdate("update room set reserved = 1 where roomNumber=" + roomEnter.getText() );
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
			
			//checks user out - puts hotel and room to NULL 
			JButton checkOut = new JButton("Check Out");
			checkOut.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					try {
						stmt = conn.createStatement();
						stmt.executeUpdate("update customer set hotelName = NULL, roomNumber=0 where cID =" + customerEnter.getText() );
						
						stmt = conn.createStatement();
						stmt.executeUpdate("update room set reserved = 0 where roomNumber=" + roomEnter.getText() );
						
						stmt = conn.createStatement();
						stmt.executeUpdate("delete from reservation where hotelName = '" + hotelEnter.getText() + "' AND cID=" + customerEnter.getText());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
			});
			
			//panel for buttons
			JPanel buttonP = new JPanel();
			buttonP.add(checkIn);
			buttonP.add(checkOut);
			
			//panel adds customer fields and button panel
			checkingRes.add(customerName);
			checkingRes.add(hotelName);
			checkingRes.add(roomName);
			checkingRes.add(buttonP);
			
			//panel to look at stats of hotel 
			JPanel hotelCustomers = new JPanel(); 
			hotelCustomers.setLayout(new BoxLayout(hotelCustomers, BoxLayout.Y_AXIS));
			
			//creates dropdown for hotel options
			JLabel h = new JLabel("Hotel");
			String[] hotels = { "Aria", "Four Seasons", "Orleans", "Bellagio", "Mandalay", "Caesars", "Motel 6", "Venetian", "Palazzo" };
			JComboBox hotelOption = new JComboBox(hotels);
			h.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
			JPanel hotelB = new JPanel(); 
			hotelB.add(h);
			hotelB.add(hotelOption);
			
			JPanel hotelButtons = new JPanel(); 
			
			JTextArea rslt = new JTextArea("Results"); 
		
			//shows number of occupants at hotel
			JButton enterO = new JButton("Occupants");
			enterO.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str = "";
					try {
						stmt=conn.createStatement();
						rs = stmt.executeQuery("Select COUNT(cID) as countCus, hotelName from Customer Group by hotelName");
						
						while (rs.next()) {
							str = str + "Hotel Name: " + rs.getString("hotelName") 
							+ "\nCurrent Occupants: " + rs.getInt("countCus") + "\n-------------\n";
							rslt.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// TODO Auto-generated method stub
					
				}
				
			});
			
			//shows current customers 
			JButton enterCC = new JButton("Current Customers");
			enterCC.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					rslt.setText("Results");
					String str = ""; 
					try {
						stmt=conn.createStatement();
						rs = stmt.executeQuery("Select cID, hotelName FROM Reservation WHERE Reservation.checkIn>CURDATE() AND EXISTS (Select * FROM Customer WHERE Customer.hotelName='" +
						hotelOption.getSelectedItem() + "' AND Customer.cID=Reservation.cID)");
						while (rs.next()) {
							str = str + "Customer Name: " + rs.getString("Customer.name") 
							+ "\nHotel Name: " + rs.getString("Reservation.hotelName") + "\n-------------\n";
							rslt.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (rslt.getText().equals("Results")) {
						rslt.setText("There are no current customers at " + hotelOption.getSelectedItem());
					}
					
				}
				
			});
			
			//shows users with reservations
			JButton enterR = new JButton("Reservations");
			enterR.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String str = ""; 
					try {
						stmt=conn.createStatement();
						rs = stmt.executeQuery("Select Customer.name, Customer.cID, Reservation.hotelName, Reservation.checkin From Customer LEFT JOIN Reservation ON Customer.cID = Reservation.cID");
						while (rs.next()) {
							str = str + "Customer Name: " + rs.getString("Customer.name") 
							+ "\nHotel Name: " + rs.getString("Reservation.hotelName") + "\n-------------\n";
							rslt.setText(str);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
				}
				
			});
			hotelButtons.add(enterO);
			hotelButtons.add(enterCC);
			hotelButtons.add(enterR);
			
			
			rslt.setRows(20);
			
			hotelCustomers.add(hotelB);
			hotelCustomers.add(hotelButtons);
			hotelCustomers.add(rslt);
			
			
			this.add(checkingRes);
			this.add(hotelCustomers);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
