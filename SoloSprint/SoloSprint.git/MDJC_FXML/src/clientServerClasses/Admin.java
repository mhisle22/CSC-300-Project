package clientServerClasses;
import java.io.Serializable;

public class Admin extends User implements Serializable
{

	public Admin(String username, String password, boolean isAdmin)
	{
		super(username, password, isAdmin);
	}
	
	public Admin()
	{
		super();
	}
	
}
