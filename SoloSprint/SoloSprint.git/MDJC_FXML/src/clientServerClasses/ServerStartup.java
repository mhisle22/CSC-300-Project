package clientServerClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

//This class is just meant to be used to start the server whenever it has been saved to a file.
public class ServerStartup
{
	private final String SERIALIZED_FILE_NAME;
	private static Server server;
	private static saveThread saver;
	private static boolean unbind;

	public ServerStartup()
	{
		SERIALIZED_FILE_NAME = "Server.xml";
	}

	public ServerStartup(String fileName)
	{
		SERIALIZED_FILE_NAME = fileName;
	}

	//Deserialize Server.xml, which holds data on the Server and return false
	//If Server.xml is not found, then a new Server is made and return true.
	public boolean readXMLServer()
	{
	
		//Attempt to find XML file and create object from it
		Server readServer = null;
		try 
		{
		  	JAXBContext jaxbc = JAXBContext.newInstance(Server.class);
		    Unmarshaller marshaller = jaxbc.createUnmarshaller();
	      	InputStream file = null;
		    try 
			{
		      	file = new FileInputStream(SERIALIZED_FILE_NAME);
	      		readServer =(Server) marshaller.unmarshal(file);
	     	} 
		   	catch (FileNotFoundException e) 
		    {
		      	//Warns user file was not found for one reason or another
		      	System.out.println("A file does not exist under either this xml name. Creating new instance of ConcreteServer");
		      	readServer = new Server("admin", "CSC300");
		        server = readServer;
		        return true;

		   	} 
			finally 
		   	{
		   		try 
		      	{
		      		if(file != null)
		      		{
	      				file.close();
	    			}
		  		} 	
		      	catch (IOException e) 
		      	{
	      			
	      		}
		  	}
			
		} 
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
		readServer.renewStatementRefs();
		server = readServer;
		return false;
	}
	
	//Start the registry, adding a given server
	public void startRegistry(Server server)
	{
		try
		{
			Registry registry = LocateRegistry.createRegistry(1099);
			UnicastRemoteObject.exportObject(server, 1099);
			registry.rebind("Server", server);
			unbind = false;
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void startLocalServer()
	{
		//Start up the server.
		ServerStartup serverStarter = new ServerStartup();
		serverStarter.readXMLServer();
		saver = new saveThread(server, serverStarter.SERIALIZED_FILE_NAME);
		saver.start();
		serverStarter.startRegistry(server);
	}
	
	//Unbinds the server from RMI and ends the saveThread for the server safely
	public static void unbindServer()
	{
		Registry registry;
		try 
		{
			registry = LocateRegistry.getRegistry();
			registry.unbind("Server");
			UnicastRemoteObject.unexportObject(server, true);
			saver.endThread();
			saver.interrupt();
			saver.join();
			unbind = true;
		} 
		catch (RemoteException | NotBoundException | InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void refreshSaver()
	{
		saver.setServer(server);
	}
	
	public static boolean getUnbound()
	{
		return unbind;
	}
	
	public static Server getServer()
	{
		return server;
	}
	
	public void setSaveThread(Server server, String filename)
	{
		ServerStartup.server = server;
		saver = new saveThread(server, filename);
		saver.start();
	}
	
	public String getFileName()
	{
		return SERIALIZED_FILE_NAME;
	}

}
