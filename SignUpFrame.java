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

public class SignUpFrame extends JFrame {

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
	 * creates frame for user to sign up registers customer to database
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SignUpFrame() throws ClassNotFoundException, SQLException {
		this.setName("Reserve a Room");
		this.setSize(1000, 1000);
		this.setLocation(800, 0);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// customer fields to enter (adding name and age)
		JTextField customerEnter = new JTextField("");
		JPanel customerName = addOptions("Name", customerEnter);

		JTextField ageenter = new JTextField("");
		JPanel customerAge = addOptions("Customer Age", ageenter);

		JTextArea rslt = new JTextArea(10, 10);
		rslt.setText("Now Signing Up");

		JButton submit = new JButton("Enter");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					stmt = conn.createStatement();
					rslt.setText(customerEnter.getText() + "," + ageenter.getText() + ", NULL, NULL");
					stmt.executeUpdate("INSERT INTO CUSTOMER(name,  age) VALUES('" + customerEnter.getText() + "', "
							+ ageenter.getText() + ")");

					// prints reservation information
					stmt = conn.createStatement();
					rs = stmt.executeQuery(
							"select cID, name from CUSTOMER where name='" + customerEnter.getText() + "'");
					while (rs.next()) {
						rslt.setText("Thanks for signing up, " + customerEnter.getText()
								+ "\n Your Customer ID Number is: " + rs.getInt("cID"));
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		this.add(customerName);
		this.add(customerAge);
		this.add(submit);
		this.add(rslt);

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
