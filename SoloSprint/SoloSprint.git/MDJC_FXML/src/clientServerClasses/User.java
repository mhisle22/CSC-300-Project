package clientServerClasses;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import mdjcBusinessPlanClasses.BusinessPlan;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable
{
	
	@XmlElement
	public String username;
	@XmlElement
	public String password;
	@XmlElement
	public Department department;
	private transient String userToken;
	private boolean isAdmin;
	
	public User(String username, String password, boolean isAdmin)
	{
		super();
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	public User() { }

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public String getUserToken()
	{
		return userToken;
	}

	void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}
	
	boolean isAdmin()
	{
		return isAdmin;
	}

	//Saves a BusinessPlan
	//a plan only editable by Admins will not be overwritten if not an admin.
	public void save(BusinessPlan plan)
	{
		department.save(plan, isAdmin);
	}

	//If admin, has permissions to create a new User.
	public User addUser(String username, String password, boolean isAdmin)
	{
		if(this.isAdmin())
		{
			User newUser;
			//If isAdmin is true, new user will be an Admin
			if(isAdmin)
			{
				newUser = new Admin(username, password, isAdmin);
			}
			//Otherwise, new user will be a User
			else
			{
				newUser = new User(username, password, isAdmin);
			}
			return newUser;
		}
		return null;
	}

}
