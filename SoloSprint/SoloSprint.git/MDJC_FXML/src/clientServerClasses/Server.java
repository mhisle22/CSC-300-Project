package clientServerClasses;
import mdjcBusinessPlanClasses.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Server implements ServerInterface, Serializable
{
	private static final long serialVersionUID = 375484437099263573L;
	
	@XmlElementWrapper(name = "userList")
	@XmlElement(name = "users")
	public ArrayList<User> users;
	@XmlElementWrapper(name = "departmentList")
	@XmlElement(name = "departments")
	public ArrayList<Department> departments;
	@XmlElement
	public Random tokenGenerator;
	
	//The default constructor, primarily used for XMLDeserialization
	public Server()
	{
		users = new ArrayList<User>();
		departments = new ArrayList<Department>();
		tokenGenerator = new Random(System.currentTimeMillis());
	}
	
	//The primary constructor for the server, which adds an initial default admin to the server.
	public Server(String adminUsername, String adminPassword)
	{
		users = new ArrayList<User>();
		//Give the Server an initial Admin which can be used to add other users.
		User admin = new Admin(adminUsername, adminPassword, true);
		admin.department = new Department("DEFAULT");
		users.add(admin);
		departments = new ArrayList<Department>();
		departments.add(admin.department);
		tokenGenerator = new Random(System.currentTimeMillis());
	}

	//This method generates a unique user token that is used to identify users, which is returned.
	public String genUserToken()
	{
		String token = "";
		for (int i = 0; i < 16; i++)
		{
			token = token + tokenGenerator.nextInt(10);
		}
		return token;
	}

	public ArrayList<User> getUsers()
	{
		return this.users;
	}

	public void setUsers(ArrayList<User> users)
	{
		this.users = users;
	}

	public Random getTokenGenerator()
	{
		return this.tokenGenerator;
	}

	public void setTokenGenerator(Random tokenGenerator)
	{
		this.tokenGenerator = tokenGenerator;
	}

	// Helper method that returns the user that matches the userToken, or
	// returns null if no such user exists.
	public User matchUser(String userToken)
	{
		Iterator<User> userIterator = this.users.iterator();
		User user;
		//For every user in users
		while (userIterator.hasNext())
		{
			user = userIterator.next();
			//Return this user if it's userToken matches the given userToken
			if (user.getUserToken() != null && user.getUserToken().equals(userToken))
			{
				return user;
			}
		}
		//Return null if no match was found
		return null;
	}

	//Checks that the user exists in the server's list of users and that it's password
	//matches the given password before generating and returning a userToken
	@Override
	public String authenticate(String username, String password) throws RemoteException
	{
		Iterator<User> userIterator = this.users.iterator();
		User user;
		//For every user in users
		while (userIterator.hasNext())
		{
			user = userIterator.next();
			//If this user's username matches the given username
			if (user.getUsername().equals(username))
			{
				//If this user's password matches the given password
				if (user.getPassword().equals(password))
				{
					//Generate a unique userToken
					String userToken = "";
					boolean duplicate = true;
					while (duplicate)
					{
						userToken = genUserToken();
						if (matchUser(userToken) == null)
						{
							duplicate = false;
						}
					//Give the userToken to the user and return it
					}
					for(int i = 0; i < users.size(); i++)
					{
						user = users.get(i);
						if(user.getUsername().equals(username))
						{
							user.setUserToken(userToken);
							ServerStartup.refreshSaver();
							return userToken;
						}
					}
					
					ServerStartup.refreshSaver();
					return userToken;
				}
			}
		}
		//If no username/password match was found, return null.
		return null;
	}

	// Returns the id, name, and year of all BusinessPlans within a
	// user's department.
	@Override
	public String[][] view(String userToken) throws RemoteException
	{
		User user = matchUser(userToken);
		if (user == null)
		{
			return null;
		}
		String dep = user.getDepartment().getDepartmentName();
		for(int i = 0; i < departments.size(); i++)
		{
			if(departments.get(i).getDepartmentName().equals(dep))
			{
				return departments.get(i).view();
			}
		}
		return null;
	}

	// Returns the BusinessPlan which matches the id given.
	@Override
	public BusinessPlan retrieve(String userToken, String bpid) throws RemoteException
	{
		User user = matchUser(userToken);
		if (user == null)
		{
			return null;
		}
		for(int i = 0; i < departments.size(); i++)
		{
			Department dep = departments.get(i);
			if(user.getDepartment().getDepartmentName().equals(dep.getDepartmentName()))
			{
				return dep.retrieve(bpid);
			}
		}
		return null;
	}

	// Saves the BusinessPlan within the department.
	@Override
	public void save(String userToken, BusinessPlan plan) throws RemoteException
	{
		User user = matchUser(userToken);
		if (user == null)
		{
			return;
		}
		for(int i = 0; i < departments.size(); i++)
		{
			Department dep = departments.get(i);
			if(user.getDepartment().getDepartmentName().equals(dep.getDepartmentName()))
			{
				dep.save(plan, user.isAdmin());
				user.setDepartment(dep);
				ServerStartup.refreshSaver();
				return;
			}
		}
	}

	// Adds a new User to the server, only will work if this method was
	// called by an Admin. 
	// IMPORTANT: If departmentName is not already registered, then a new Department will be created.
	//		   	  We leave it to the Application calling addUser to handle typos and ensure departmentName is accurate.
	@Override
	public boolean addUser(String userToken, String username, String password, String departmentName, boolean isAdmin) throws RemoteException
	{
		// Check to ensure that username isn't already taken.
		Iterator<User> userIterator = this.users.iterator();
		User currUser;
		//For each user in users
		while (userIterator.hasNext())
		{
			currUser = userIterator.next();
			//Return before adding new user if username matches a previous user's username
			if (currUser.getUsername() == username)
			{
				return false;
			}
		}
		// Get the user trying to add a new user, have them create the new User object.
		User user = matchUser(userToken);
		if (user == null) {return false;}
		User newUser = user.addUser(username, password, isAdmin);
		// Set the user's department and add the User if one was actually returned.
		if (newUser != null)
		{
			// Give the User their department, creating a new one if no match was found
			Iterator<Department> departmentIterator = this.departments.iterator();
			Department department = null;
			boolean found = false;
			
			// Loop through each known department to see if the requested department already exists
			while ((!found)&&(departmentIterator.hasNext()))
			{
				department = departmentIterator.next();
				if(department.getDepartmentName() == departmentName)
				{
					found = true;
				}
			}
			//Create a new department if the User's requested department does not exist.
			if(!found)
			{
				department = new Department(departmentName);
				departments.add(department);
			}
			newUser.setDepartment(department);
			users.add(newUser);
			ServerStartup.refreshSaver();
			return true;
		}
		return false;
	}
	
	//Called by an independent thread, which is created when the Server is constructed.
	//Writes this Server object to Server.xml.
	//Writes the server to XML whenever called
		public void writeXMLServer(String file_name)
		{
				 
				//Attempt to write server to an XML file in current working directory
				try {
				      JAXBContext jaxbc = JAXBContext.newInstance(Server.class);
				      Marshaller marshaller = jaxbc.createMarshaller();
				      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				      OutputStream out = null;
				      try 
				      {
				         out = new FileOutputStream(file_name);
				         marshaller.marshal(this, out);
				      } 
				      catch (FileNotFoundException e) 
				      {
				         e.printStackTrace();
				      } 
				      finally 
				      {
				         try 
				         {
				            out.close();
				         } 
				         catch (IOException e) 
				         {
				        	 e.printStackTrace();
				         }
				      }
				   } catch (JAXBException e) {
				      e.printStackTrace();
				   }
		}
	
	public Department addDepartment(String departmentName)
	{
		Iterator<Department> departmentIterator = this.departments.iterator();
		Department department = null;
		Department newDepartment = null;
		boolean found = false;
		
		// Loop through each known department to see if the requested department already exists
		while ((!found)&&(departmentIterator.hasNext()))
		{
			department = departmentIterator.next();
			if(department.getDepartmentName() == departmentName)
			{
				found = true;	
				newDepartment = department;
			}
		}
		//Create a new department if the User's requested department does not exist.
		if(!found)
		{
			newDepartment = new Department(departmentName);
			departments.add(newDepartment);
		}
		ServerStartup.refreshSaver();
		return newDepartment;
		
	}
	
	
	
	//Re-links the statements in business plans when instantiated by XML
		public void renewStatementRefs() 
		{
			if(!departments.isEmpty())
			{
				for(Department d : departments)
				{
					if(!d.getPlans().isEmpty())
					{
						for(BusinessPlan bp : d.getPlans())
						{
							bp.renewStatementRefs();
						}
					}
				}
			}
		}
		
		@Override
		public boolean getClientPrivilege(String userToken)
		{
			User user = matchUser(userToken);
			return user.isAdmin();
		}
}
