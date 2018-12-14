package gui_FXML.OldTests;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientServerClasses.Admin;
import clientServerClasses.Server;
import clientServerClasses.ServerStartup;
import clientServerClasses.User;

import java.util.concurrent.TimeUnit;

class ServerTest
{

	Server server;
	User user1;
	User user2;
	User admin;
	
	@BeforeEach
	void setUp()
	{
		server = new Server();
		ServerStartup startup = new ServerStartup("TestServer.xml");
		startup.setSaveThread(server, startup.getFileName());
		server = ServerStartup.getServer();
		user1 = new User("user1","password1",false);
		user2 = new User("user2","password2",false);
		admin = new Admin("admin","password", true);
		server.users.add(user1);
		server.users.add(user2);
		server.users.add(admin);
	}
	
	@BeforeAll
	static void startServer()
	{
		ServerStartup.startLocalServer();
	}

	@Test
	void testAuthenticateMatchUser()
	{	
		try
		{
			//Show that null is returned if user doesn't exist or if password
			//is incorrect.
			assertEquals(null,server.authenticate(null,null));
			assertEquals(null,server.authenticate("user1", "wrongPass"));
			
			//Show that server creates a userToken if user exists, and the calling
			//matchUser() with this token returns the user.
			assertEquals(user1,server.matchUser(server.authenticate("user1", "password1")));
			assertEquals(user2,server.matchUser(server.authenticate("user2", "password2")));
			assertEquals(admin,server.matchUser(server.authenticate("admin", "password")));
			
		}
		catch (RemoteException e)
		{
			fail("Local test should never throw RemoteException");
		}
	}
	
	@Test
	void testAddUser()
	{
		try
		{
			//Show that regular Users can not add new users.
			String user1Token = server.authenticate("user1", "password1");
			server.addUser(user1Token, "nonUser1", "nonPass1", "nonDepartment1", true);
			server.addUser(user1Token, "nonUser2", "nonPass2", "nonDepartment2", false);
			assertEquals(3,server.users.size());
			
			//Show that Admins can add new Users and Admins.
			String adminToken = server.authenticate("admin", "password");
			server.addUser(adminToken, "newUser", "newPass1", "newDepartment1", false);
			server.addUser(adminToken, "newAdmin", "newPass2", "newDepartment1", true);
			assertEquals(1,server.departments.size());
			assertEquals(5,server.users.size());
			
			//Show that adding a User with a non-unique username will fail.
			server.addUser(adminToken, "user1", "newPass3", "nonDepartment1", false);
			server.addUser(adminToken, "user2", "newPass3", "nonDepartment1", false);
			server.addUser(adminToken, "admin", "newPass5", "nonDepartment1", true);
			assertEquals(5,server.users.size());
		}
		catch (RemoteException e)
		{
			fail("Local test should never throw RemoteException");
		}
		
	}
	
	@Test
	void testSaveAll()
	{
		//Authenticate the users (creates userTokens)
		try
		{
			server.authenticate("user1", "password1");
			server.authenticate("user2", "password2");
			server.authenticate("admin", "password");
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Save the server as a file
		server.writeXMLServer("TestServer.xml");

		//Load the server from the file
		ServerStartup serverStarter = new ServerStartup("TestServer.xml");
		serverStarter.readXMLServer();
		Server loadedServer = ServerStartup.getServer();
		
		
		//Show that while users exist, their userTokens were not saved
		assertEquals(null,loadedServer.getUsers().get(0).getUserToken());
		assertEquals(null,loadedServer.getUsers().get(1).getUserToken());
		assertEquals(null,loadedServer.getUsers().get(2).getUserToken());
	}
}
