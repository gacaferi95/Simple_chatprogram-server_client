import java.awt.EventQueue;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class login {

	private JFrame frame;
	private JTextField username;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Login");
		frame.setBounds(100, 100, 474, 336);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(103, 113, 73, 16);
		frame.getContentPane().add(lblUsername);
		
		username = new JTextField();
		username.setBounds(188, 110, 139, 22);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Password:");
		lblNewLabel.setBounds(103, 161, 73, 16);
		frame.getContentPane().add(lblNewLabel);
		
		password = new JTextField();
		password.setBounds(188, 158, 139, 22);
		frame.getContentPane().add(password);
		password.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection connn=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root", "");
						Statement stmt=connn.createStatement();
						
						String query="Select * from tblogin where username='"+username.getText()+"' and password='"+password.getText()+"'";
						ResultSet rs=stmt.executeQuery(query);
						
						if(rs.next())
						{
							SimpleChatClient obj_klient=new SimpleChatClient(username.getText());
							obj_klient.go();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Can't login, type right information.");
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Check server!");
					}
				}	
		});
		btnLogin.setBounds(163, 224, 97, 25);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblLoginPage = new JLabel("Login Page");
		lblLoginPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginPage.setFont(new Font("Times New Roman", Font.PLAIN, 28));
		lblLoginPage.setBounds(22, 13, 408, 68);
		frame.getContentPane().add(lblLoginPage);
	}
}
