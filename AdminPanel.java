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
 * Administrator Panel is accessed by adminstrator with password: password 
 * allows employee access to view hotel status (occupants, customer database, etc.,)
 * this panel is the access point to get to admin frame 
 * @author anhth
 *
 */
public class AdminPanel extends JPanel {
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
	 * creates panel for administrator 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public AdminPanel() throws ClassNotFoundException, SQLException {
		

		// STEP 1: Register JDBC driver (automatically done since JDBC 4.0)
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 2: Open a connection
		conn = DriverManager.getConnection("jdbc:mysql://localhost/hbs?user=root&password=root");

		//gets admin info (allows access) 
		JLabel textRequired = new JLabel("Password Required");
		JTextArea passwordEntry = new JTextArea();
		passwordEntry.setColumns(10);
		JButton enterAdmin = new JButton("Enter");
		enterAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (passwordEntry.getText().equals("password")) {
					try {
						AdminFrame af = new AdminFrame();
						passwordEntry.setText("");
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					passwordEntry.setText("Incorrect password");
				}

			}

		});

		this.add(textRequired);
		this.add(passwordEntry);
		this.add(enterAdmin);

	}

}
