package gui_FXML.OldTests;

import static org.junit.jupiter.api.Assertions.*;
import clientServerClasses.*;
import mdjcBusinessPlanClasses.*;

import java.rmi.RemoteException;
import java.rmi.registry.*;

import org.junit.jupiter.api.Test;

import clientServerClasses.Admin;
import clientServerClasses.Client;
import clientServerClasses.Department;
import clientServerClasses.Server;

class FunctionalTest
{

	
	@Test
	void test() throws RemoteException, InterruptedException
	{

		//Create some necessary initial variables.
		Server server = new Server();
		ServerStartup startup = new ServerStartup("TestServer.xml");
		startup.setSaveThread(server, startup.getFileName());
		startup.startRegistry(server);
		Admin admin = new Admin("Matt", "Good Password", true);
		admin.department = server.addDepartment("Computer Science");
		BusinessPlan planOne = new BusinessPlan("Plan1", "Name1", "Year1", false);
		Client client = new Client(null);
		
		//Add The initial admin to the server.
		server.getUsers().add(admin);
		
		try 
		{
			//Test the registry stuff
			Registry registry = LocateRegistry.getRegistry(1099);
	
			//Get server from the registry and give it to client.
			ServerInterface stub = (ServerInterface) registry.lookup("Server");
			client = new Client(stub);
		}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		
		client.login("Matt", "Good Password");
		
		//Add a user and ensure that it worked.
		client.addUser("James", "Bad Password", "Computer Science", false);
		//there are 5 already there when this was made. 
		assertEquals(2, server.getUsers().size());
		
		//Make sure that two the userTokens are unique.
		String mattToken = server.getUsers().get(0).getUserToken();
		String jamesToken = server.getUsers().get(1).getUserToken();
		assertNotEquals(mattToken, jamesToken);
		
		//Test adding a new BusinessPlan.
		client.setLocalCopy(planOne);
		client.save();
		
		//Test to make sure that the BusinessPlan was saved and retrieved properly.
		assertEquals(planOne.getId(), server.retrieve(client.getUserToken(),"Plan1").getId());
		
		
		//Test saving a new version of the BusinessPlan as both a User and an Admin.
		BusinessPlan planOneCopy = new BusinessPlan("Plan1", "DifferentName1", "DifferentYear1", true);
		client.setLocalCopy(planOneCopy);
			
		//Make sure regular users cannot save a non-editable BusinessPlan.
		client.login("James", "Bad Password");
		client.save();
		assertEquals(planOne.getId(), server.retrieve(client.getUserToken(),"Plan1").getId());
		
		//Make sure admins can save non-editable BusinessPlans.
		client.login("Matt", "Good Password");
		client.save();
		assertEquals(planOneCopy.getId(), server.retrieve(client.getUserToken(),"Plan1").getId());
			
		//Make sure Users and Admins can save new plans
		BusinessPlan planTwo = new BusinessPlan("Plan2", "Name2", "Year2", true);
		BusinessPlan planThree = new BusinessPlan("Plan3", "Name3", "Year3", false);
			
		client.setLocalCopy(planTwo);
		client.save();
		assertEquals(planTwo.getId(), server.retrieve(client.getUserToken(), "Plan2").getId());
			
		client.login("James", "Bad Password");
		client.setLocalCopy(planThree);
		client.save();
		assertEquals(planThree.getId(), server.retrieve(client.getUserToken(), "Plan3").getId());
			
			
		//Test to ensure that view() returns the appropriate information.
		String[][] data = client.view();
		
		assertEquals(data[0][0], "Plan1");
		assertEquals(data[0][1], "DifferentName1");
		assertEquals(data[0][2], "DifferentYear1");
		
		assertEquals(data[1][0], "Plan2");
		assertEquals(data[1][1], "Name2");
		assertEquals(data[1][2], "Year2");

		assertEquals(data[2][0], "Plan3");
		assertEquals(data[2][1], "Name3");
		assertEquals(data[2][2], "Year3");
		
		ServerStartup.unbindServer();
		
	}

}
