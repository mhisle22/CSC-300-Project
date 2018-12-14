package gui_FXML.OldTests;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clientServerClasses.Department;
import clientServerClasses.User;

class UserTest
{
	
	User user;
	Department dept;
	
	@BeforeEach
	void setUp()
	{
		dept = new Department("testDepartment");
		user = new User("testAdmin","password", true);
	}
	
	@Test
	void testAddUser()
	{
		User newUser1 = user.addUser("newUser", "password1", false);
		User newUser2 = user.addUser("newAdmin", "password2", true);
		
		assertEquals(null, newUser1);
		
		assertEquals(null, newUser2);
	}

}
