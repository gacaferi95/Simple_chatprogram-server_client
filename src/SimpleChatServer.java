

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class SimpleChatServer {
	
	//gui compunents
	
	JTextArea jtatekstServeri;
	ArrayList<Socket> userat= new ArrayList();
	boolean serverStateON=false;
	
	Socket clientSoket;
	ServerSocket serverSock;
	
	//porit
	
	int port=9900;
	
	public static void main(String [] args)
	{
		SimpleChatServer chatServer=new SimpleChatServer();
		chatServer.go();
	}
	
	
	class klientHandler implements Runnable
	{
		
		Socket soketi;
		DataInputStream reader;
		DataOutputStream writer;
		
		public klientHandler(Socket clientsoketi)
		{
			try {
				soketi=clientsoketi;
				reader=new DataInputStream(soketi.getInputStream());
				writer=new DataOutputStream(soketi.getOutputStream());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message;
			try
			{
				while((message=reader.readUTF()) != null)
				{
					System.out.println("read" + message);
					messageEveryone(message);
				}
			}	
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		
		
		public void messageEveryone(String message)
		{
			DataOutputStream output;
			Socket _soketi;
			int i=0;
			while(i < userat.size())
			{
				
				try {
					_soketi=userat.get(i);
					output=new DataOutputStream(_soketi.getOutputStream());
					output.writeUTF(message);
					
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				i++;
			
			}
			
		}
	}
		
		public void go()
		{
			
			JFrame dritarja=new JFrame("Chat Server");
			dritarja.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JPanel paneli=new JPanel();
			paneli.setLayout(new FlowLayout());
			
			JPanel panelicenter=new JPanel();
			panelicenter.setLayout(new FlowLayout());
			jtatekstServeri= new JTextArea(12,40);
			jtatekstServeri.setLineWrap(true);
			jtatekstServeri.setWrapStyleWord(true);
			jtatekstServeri.setEditable(false);
			JScrollPane scroll=new JScrollPane(jtatekstServeri);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			panelicenter.add(scroll);
			
			
			JButton btnconnect=new JButton("Start Server");
			btnconnect.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent arg0) {	
							
							new Thread()
							{
								public void run()
								{
									if(serverStateON==false)
									{
										try
										{
											serverSock= new ServerSocket(port);
											btnconnect.setText("Stop Server");
											jtatekstServeri.append("Server has started at port: ="+port+" \n");
											serverStateON=true;
											while(true)
											{
												clientSoket=serverSock.accept();
												userat.add(clientSoket);
												jtatekstServeri.append(clientSoket.toString()+"is connected. \n");
												Thread t=new Thread(new klientHandler(clientSoket));
												t.start();
											}
										}
										catch(Exception ex)
										{
											jtatekstServeri.append("Can't start the server, restart program server.\n");
										}
									}
									else
									{
										clientSoket=null;
										serverSock=null;
										jtatekstServeri.append("Server has Stopped. \n");
										btnconnect.setText("Start Server");
										serverStateON=false;
									}
								}
								
							}.start();
						}
				
					});
			paneli.add(btnconnect);
						
			dritarja.add(BorderLayout.NORTH,paneli);
			dritarja.add(BorderLayout.CENTER, panelicenter);
			
			
			dritarja.pack();
			dritarja.setVisible(true);
				
		}
				
}
		
