package gui_FXML.Test;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

	@RunWith(JUnitPlatform.class)

	@SelectClasses(
	{
	   AddCategoryTest.class,
	   AddStatementTest.class,
	   AddUserTest.class,
	   CategoryTreeTest.class,
	   NonAdminCategoryTreeTest.class,
	   nonAdminStatementTreeTest.class,
	   PlanInfoNonAdminTest.class,
	   PlanInfoExitTest.class,
	   PlanInfoTest.class,
	   PlanSelectionTest.class,
	   RetrievePlanTest.class,
	   ServerLoginCancelTest.class,
	   ServerLoginTest.class,
	   StatementInfoTest.class,
	   UserLoginCancelTest.class,
	   UserLoginTest.class
	})
	
	public class TestSuite {
}
