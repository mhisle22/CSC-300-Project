package gui_FXML.OldTests;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import clientServerClasses.Admin;
import clientServerClasses.Department;
import clientServerClasses.User;

class AdminTest
{

	User user;
	Department dept;
	
	@BeforeEach
	void setUp()
	{
		dept = new Department("testDepartment");
		user = new Admin("testAdmin","password",true);
		user.setDepartment(dept);
	}
	
	@Test
	void testAddUser()
	{
		User newUser1 = user.addUser("newUser", "password1", false);
		User newUser2 = user.addUser("newAdmin", "password2", true);
		
		assertEquals("newUser", newUser1.getUsername());
		assertEquals("password1", newUser1.getPassword());
		assertEquals("clientServerClasses.User", newUser1.getClass().getName());
		
		assertEquals("newAdmin", newUser2.getUsername());
		assertEquals("password2", newUser2.getPassword());
		assertEquals("clientServerClasses.Admin", newUser2.getClass().getName());
	}
	
}
