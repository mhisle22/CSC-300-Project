package gui_FXML.OldTests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

import mdjcBusinessPlanClasses.BusinessPlan;
import clientServerClasses.Department;

//Tests Department. Make sure you understand how BusinessPlan and Department work before looking at these tests.
class DepartmentTest
{

	Department department;
	BusinessPlan planOne;
	BusinessPlan planTwo;
	
	@BeforeEach
	void reset()
	{
		//Reset the department to ensure that everything is in its initial state
		department = new Department("Computer Science");
		
		//Reset plans to be manipulated.
		planOne = new BusinessPlan("Plan1", "Name1", "1", false);
		planTwo = new BusinessPlan("Plan2", "Name2", "2", true);
	}
	
	@Test
	void testRetrieve()
	{
		//Ensure that whenever we try to retrieve plans that are not there, null is returned.
		//We specifically test retrieve on two plans which we will add, and two plans we will not.
		//This way, we have an initial state that we can compare against later.
		assertEquals(null, department.retrieve("Plan0"));
		assertEquals(null, department.retrieve("Plan1"));
		assertEquals(null, department.retrieve("Plan2"));
		assertEquals(null, department.retrieve("Plan3"));
		
		//Set up the new ArrayList of BusinessPlans
		ArrayList<BusinessPlan> plans = new ArrayList<BusinessPlan>();
		plans.add(planOne);
		plans.add(planTwo);
		department.setPlans(plans);
		
		//Test to ensure that everything is retrieved correctly.
		assertEquals(null, department.retrieve("Plan0"));
		assertEquals(planOne, department.retrieve("Plan1"));
		assertEquals(planTwo, department.retrieve("Plan2"));
		assertEquals(null, department.retrieve("Plan3"));
	}
	
	@Test
	void testSave()
	{	
		//Ensure that the plans which we are saving are not already there.
		assertEquals(null, department.retrieve("Plan1"));
		assertEquals(null, department.retrieve("Plan2"));
		
		//Ensure that a null BusinessPlan cannot be saved.
		department.save(null, true);
		
		//Save one BusinessPlan and ensure that it saved properly.
		department.save(planOne, true);
		assertEquals(planOne, department.retrieve("Plan1"));
		
		//Ensure no other plans were added.
		assertEquals(1, department.getPlans().size());
		
		//Ensure that saving a second BusinessPlan works.
		department.save(planTwo, true);
		assertEquals(planTwo, department.retrieve("Plan2"));
		assertEquals(2, department.getPlans().size());
		
		//Create a copy of planOne and planTwo.
		BusinessPlan planOneCopy = new BusinessPlan("Plan1", "Name1", "1", false);
		BusinessPlan planTwoCopy = new BusinessPlan("Plan2", "Name2", "2", true);
		
		//Change some BusinessPlans so that we can resave them.
		planOneCopy.setYear("3");
		planTwoCopy.setYear("4");
		
		//Ensure that non-editable BusinessPlans cannot be saved by non-admins.
		department.save(planOneCopy, false);
		assertEquals("1", department.retrieve("Plan1").getYear());
		
		//Ensure that non-editable BusinessPlans can be saved by admins.
		department.save(planOneCopy, true);
		assertEquals("3", department.retrieve("Plan1").getYear());

		//Ensure that the old BusinessPlan was changed and a new one was not added.
		assertEquals(2, department.getPlans().size());
		
		//Ensure that editable BusinessPlans can be saved by Users.
		department.save(planTwoCopy, false);
		assertEquals("4", department.retrieve("Plan2").getYear());
		
		//Ensure that editable BusinessPlans can be saved by admins.
		planTwoCopy = new BusinessPlan("Plan2", "Name2", "2", true);
		planTwo.setYear("5");
		department.save(planTwo, true);
		assertEquals("5", department.retrieve("Plan2").getYear());
	}
	
	@Test
	void testView()
	{
		//Ensure that when their is nothing to view, an empty array is returned.
		assertEquals(0, department.view().length);
		
		//Add a new BusinessPlan and make sure view returns the correct data.
		department.save(planOne, true);
		String[][] data = department.view();
		
		assertEquals("Plan1", data[0][0]);
		assertEquals("Name1", data[0][1]);
		assertEquals("1", data[0][2]);
		
		//Add another BusinessPlan and ensure that it returns the correct data.
		department.save(planTwo, true);
		data = department.view();
		
		assertEquals("Plan1", data[0][0]);
		assertEquals("Name1", data[0][1]);
		assertEquals("1", data[0][2]);
		
		assertEquals("Plan2", data[1][0]);
		assertEquals("Name2", data[1][1]);
		assertEquals("2", data[1][2]);
		
		//Ensure that whenever data changes, view() changes appropriately.
		planOne.setYear("3");
		planOne.setName("Not Plan1 (But still totally Plan1)");
		department.save(planOne, true);
		data = department.view();
		
		assertEquals("Plan1", data[0][0]);
		assertEquals("Not Plan1 (But still totally Plan1)", data[0][1]);
		assertEquals("3", data[0][2]);
		
		assertEquals("Plan2", data[1][0]);
		assertEquals("Name2", data[1][1]);
		assertEquals("2", data[1][2]);
	}

}
