

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClient extends JFrame{
	JFrame dritarja;
	JTextArea jtaincoming;
	JTextField jtfoutgoing;
	DataInputStream reader;
	DataOutputStream writer;
	Socket soketi;
	
	//perdoruesi
	String perdoruesi="";
	
	public SimpleChatClient(String _user)
	{
		perdoruesi=_user;
	}
	
    public void go()
	{
    	dritarja=new JFrame("Chat Client");
		dritarja.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel paneli1=new JPanel();
		paneli1.setLayout(new FlowLayout());
		
		jtaincoming =new JTextArea(15,50);
		jtaincoming.setLineWrap(true);
		jtaincoming.setWrapStyleWord(true);
		jtaincoming.setEditable(false);
		
		JScrollPane scroll=new JScrollPane(jtaincoming);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jtfoutgoing =new JTextField(20);
		jtfoutgoing.addActionListener(new SendMesage());
		
		JButton btnsend=new JButton("Send");
		btnsend.addActionListener(new SendMesage());
		
		paneli1.add(scroll);
		paneli1.add(jtfoutgoing);
		paneli1.add(btnsend);
		
		
		setUpNetworking();
		Thread readerThread=new Thread(new IncomingReader());
		readerThread.start();
		
		dritarja.add(BorderLayout.CENTER,paneli1);
		dritarja.setSize(600,330);
		dritarja.setVisible(true);
	}
	
    private void setUpNetworking()
    {
    	try {
    		soketi=new Socket("localhost",9900);
    		reader=new DataInputStream(soketi.getInputStream());
    		writer=new DataOutputStream(soketi.getOutputStream());
    		System.out.println("networking established");
    	}
    	catch(Exception ex)
    	{
    		jtaincoming.append("Can't connect to server!\n");
    	}
    }
    
	class SendMesage implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			try {
				writer.writeUTF(perdoruesi+": "+jtfoutgoing.getText());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			jtfoutgoing.setText("");
			jtfoutgoing.requestFocus();
	
		}
		
	}
	
	public class IncomingReader implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message="";
			
			try
			{
				while((message=reader.readUTF()) != null)
				{
					System.out.println("read"+ message);
					jtaincoming.append(message +"\n");
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}

}
