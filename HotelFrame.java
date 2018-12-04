package hotel;

import java.sql.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.*;

public class HotelFrame extends JPanel {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/hbs";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "password";

	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	
	private String selectedHotel;

	private JTextArea result = new JTextArea();

	public HotelFrame() throws MalformedURLException, IOException, SQLException, ClassNotFoundException {

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// create hotel grid
		this.setName("Hotel Reservation System");
		this.setSize(1500, 1500);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		result.setSize(400, 600);

		JPanel hotelGrid = new JPanel();
		GridLayout experiment = new GridLayout(5, 2);
		hotelGrid.setLayout(experiment);

		// buttons for all the hotels - clickable

		JButton aria = this.addHotel(
				"https://66.media.tumblr.com/b2da660233bd9c60da90c969fac31721/tumblr_pidl31Rn6Y1qg9zhfo7_250.png",
				"Aria");
		JButton FS = this.addHotel(
				"https://66.media.tumblr.com/7b2fa017dc05d51d5e1364cae5c319dd/tumblr_pidl31Rn6Y1qg9zhfo1_250.png",
				"Four Seasons");
		JButton orleans = this.addHotel(
				"https://66.media.tumblr.com/e1836d14475350b03f3e23c4266369f6/tumblr_pidl31Rn6Y1qg9zhfo4_250.png",
				"Orleans");
		JButton bellagio = this.addHotel(
				"https://66.media.tumblr.com/43d2797b00bc5a7881f3b09ba5f3bee8/tumblr_pidl31Rn6Y1qg9zhfo8_250.png",
				"Bellagio");
		JButton mandalay = this.addHotel(
				"https://66.media.tumblr.com/e298f6df82e865904914755f2dfc74f3/tumblr_pidl31Rn6Y1qg9zhfo2_250.png",
				"Mandalay");
		JButton caesars = this.addHotel(
				"https://66.media.tumblr.com/3c2d184e3aa3c58f27dc0809a910cbff/tumblr_pidl31Rn6Y1qg9zhfo9_250.png",
				"Caesars");
		JButton motel6 = this.addHotel(
				"https://66.media.tumblr.com/a0c8c834e2930aeacb77f22996dec1c1/tumblr_pidl31Rn6Y1qg9zhfo3_250.png",
				"Motel 6");
		JButton venetian = this.addHotel(
				"https://66.media.tumblr.com/297820eeb80afe4d37e1e2f0405c9da7/tumblr_pidl31Rn6Y1qg9zhfo6_250.png",
				"Venetian");
		JButton palazzo = this.addHotel(
				"https://66.media.tumblr.com/9b8361614331fadcadac2e7befdc66e2/tumblr_pidl31Rn6Y1qg9zhfo5_250.png",
				"Palazzo");

		hotelGrid.add(aria);
		hotelGrid.add(FS);
		hotelGrid.add(orleans);
		hotelGrid.add(bellagio);
		hotelGrid.add(mandalay);
		hotelGrid.add(caesars);
		hotelGrid.add(motel6);
		hotelGrid.add(venetian);
		hotelGrid.add(palazzo);

		// creates panel for customer portal or customer info
		JTextField userName = new JTextField();
		JLabel getName = new JLabel("Customer ID");
		userName.setText("Customer ID");
		userName.addFocusListener((FocusListener) new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				JTextField source = (JTextField) e.getComponent();
				source.setText("");
				source.removeFocusListener(this);
			}
		});

		// result panel for button click for customer info
		result.setText("Result");
		JPanel customerName = new JPanel();
		JButton enterName = new JButton("Enter");
		enterName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String sql = "";
				String str = "";
				// QUERY for getting customer based on cID
				try {
					stmt = conn.createStatement();
					rs = stmt.executeQuery(
							"select cID, name, roomNumber, hotelName from customer where cID=" + userName.getText());

					// get results
					while (rs.next()) {
						str = str + ("Current Stay: \nCustomer ID=" + rs.getInt("cID") + "\nCustomer Name="
								+ rs.getString("name") + "\nHotel Name=" + rs.getString("hotelName") + "\nRoom Number="
								+ rs.getInt("roomNumber") + "\n--------------\n");
						result.setText(str);
					}

					stmt = conn.createStatement();
					rs = stmt.executeQuery("select * from reservation where cID =" + userName.getText());

					while (rs.next()) {
						str = str + ("Reservation: \nCustomer ID=" + rs.getInt("cID") + "\nHotel Name="
								+ rs.getString("hotelName") + "\nCheck In Date =" + rs.getInt("checkIn")
								+ "\nCheck Out Date =" + rs.getInt("checkOut") + "\n--------------\n");
						result.setText(str);
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		customerName.add(getName);
		customerName.add(userName);
		customerName.add(enterName);

		JPanel customerArea = new JPanel();

		customerArea.add(customerName);

		// book a room
		JButton book = new JButton("Book a Room");
		book.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ReservationFrame rf = new ReservationFrame();
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		// Allows user to sign up for customer profile
		JButton signUp = new JButton("Sign Up");
		signUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					SignUpFrame su = new SignUpFrame();
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		
		JPanel hotelInformation = new JPanel();
		hotelInformation.setLayout(new BoxLayout(hotelInformation, BoxLayout.Y_AXIS) );
		

		JButton checkRooms = new JButton("Check Availability");
		checkRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String str = "";
				
				if (hotelInformation.getComponentCount()>1) {
					hotelInformation.remove(1);
				}
				
				result.setText("");

				try {
					stmt = conn.createStatement();
					rs = stmt.executeQuery(
							"select roomNumber from room where hotelName='" + selectedHotel+  "' AND reserved = 0");

					// get results
					while (rs.next()) {
						str = (selectedHotel +": " + rs.getInt("roomNumber"));
						int roomOption = rs.getInt("roomNumber"); 
						JButton hotelOption = new JButton(str); 
						hotelOption.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								String str2 = "";
								try {
									stmt = conn.createStatement();
									rs = stmt.executeQuery(
											"select price from Room where hotelName = '" + selectedHotel + "' AND roomNumber =" + roomOption);
									while (rs.next()) {
										str2 = str2 + ("Room Number=" + roomOption + "\nPrice="
												+ rs.getInt("price") );
										result.setText(str2);
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
							
							
						});
						hotelInformation.add(hotelOption);
						result.setText("Results");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(result.getText().equals("")) {
					result.setText("There are no available rooms at this time");
				}

			}
		});
		
		RatingPanel rp = new RatingPanel(); 
		
		hotelInformation.add(checkRooms);

		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
		rightSide.add(customerArea);
		rightSide.add(hotelInformation);
		rightSide.add(result);
		rightSide.add(book);
		rightSide.add(signUp);

		SearchPanel sp = new SearchPanel();

		// adds elements to JFrame (class itself)
		this.add(hotelGrid);
		this.add(rp);
		this.add(sp);
		this.add(rightSide);
		

		
		
	}

	public JButton addHotel(String picURL, String name) throws MalformedURLException, IOException {
		BufferedImage hotelimg = ImageIO.read(new URL(picURL));
		JButton hotel = new JButton();
		hotel.setIcon(new ImageIcon(hotelimg));
		hotel.setText(name);
		hotel.setForeground(Color.WHITE);
		hotel.setHorizontalTextPosition(hotel.CENTER);

		hotel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String str = "";
				try {
					stmt = conn.createStatement();
					rs = stmt.executeQuery(
							"select hotelName, elevators, pool, freeBreakfast from hotel where hotelName = '" + name
									+ "'");

					// STEP 5: Process the results
					while (rs.next()) {
						str = str + ("Hotel Name=" + rs.getString("hotelName") + "\nElevators="
								+ rs.getBoolean("elevators") + "\nPool=" + rs.getBoolean("pool") + "\nFree Breakfast="
								+ rs.getBoolean("freeBreakfast"));
						result.setText(str);
						selectedHotel = name; 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// result.setText(name);
			}

		});
		return hotel;
	}

	public static void main(String[] args)
			throws MalformedURLException, IOException, SQLException, ClassNotFoundException {
		JFrame jf = new JFrame(); 
		HotelFrame p = new HotelFrame();
		jf.add(p);
		jf.setTitle("Hotel Reservation System");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

}
