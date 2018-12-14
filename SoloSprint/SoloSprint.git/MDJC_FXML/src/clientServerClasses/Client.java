package clientServerClasses;
import mdjcBusinessPlanClasses.*;
import java.rmi.RemoteException;

public class Client
{
	ServerInterface stub;
	String userToken;
	String host;
	public BusinessPlan localCopy;

	public Client(ServerInterface stub2)
	{
		this.stub = stub2;
	}
	
	public Client() { }

	//Sets the userToken to be equal to the userToken generated for whichever user has a matching username and password.
	//IMPORTANT: None of the following methods will work if userToken is not associated with a user on the Server.
	//			 Thus, this method must be called before anything can be done.
	//
	//As a side note, whenever the "current User" is referenced in comments, we mean the User with a matching userToken.
	public boolean login(String username, String password)
	{
		try
		{
			userToken = stub.authenticate(username, password);
			if(userToken != null)
			{
				return true;
			}
			else
			{
				return false;
			}
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}

	//Sets localCopy to be whatever BusinessPlan has an ID matching bpid.
	//If an external application controlling Client wants localCopy, they should call
	//getLocalCopy() after calling retrieve(), with an appropriate bpid().
	public boolean retrieve(String bpid)
	{
		try
		{
			localCopy = stub.retrieve(userToken, bpid);
			return true;
		} catch (RemoteException e)
		{
			return false;
		}
	}

	//Saves localCopy.
	//If localCopy has a unique ID, differing from every other BusinessPlan associated
	//with the current User's department, then this BusinessPlan is added to the Department's
	//list of BusinessPlans.
	//
	//IMPORTANT: If localCopy has a matching ID to any other BusinessPlan, then that other 
	//			 BusinessPlan will be overwritten. For this reason, ensure that your ID is correct.
	//
	//view() can be called to see all of the BusinessPlan IDs within a department.
	public void save()
	{
		try
		{
			stub.save(userToken, localCopy);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Adds a new user with the username, password, department, and admin privileges specified.
	//See Server's addUser() method for more information.
	public boolean addUser(String username, String password, String departmentName, boolean isAdmin)
	{
		try
		{
			 return stub.addUser(userToken, username, password, departmentName, isAdmin);
		} catch (RemoteException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	//Returns the ID, Name, and Year of each BusinessPlan associated with the current User's
	//department.
	//See Department's view() method for more information.
	public String[][] view()
	{
		try
		{
			return stub.view(userToken);
		} catch (RemoteException e)
		{

			e.printStackTrace();
		}
		
		return null;
	}

	//Basic getters and setters.
	public ServerInterface getStub()
	{
		return stub;
	}

	public void setStub(Server stub)
	{
		this.stub = stub;
	}

	public String getUserToken()
	{
		return userToken;
	}

	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	public BusinessPlan getLocalCopy()
	{
		return localCopy;
	}

	public void setLocalCopy(BusinessPlan localCopy)
	{
		this.localCopy = localCopy;
	}
	
	public boolean isAdmin()
	{
		
		try {
			return stub.getClientPrivilege(userToken);
		} catch (RemoteException e) {
			return false;
		}
	}

}
