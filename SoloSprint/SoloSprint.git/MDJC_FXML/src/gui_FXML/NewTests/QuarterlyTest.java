package gui_FXML.NewTests;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import clientServerClasses.ServerStartup;
import static org.assertj.core.api.Assertions.assertThat;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.Quarterly.QuarterlyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;
import mdjcBusinessPlanClasses.Statement;

@ExtendWith(ApplicationExtension.class)
public class QuarterlyTest
{
	private QuarterlyController cont;
	private FXMLLoader loader;
	private Scene scene;
	private static TransitionModel transitionModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws IOException 
	{		
		loader = new FXMLLoader();
		loader.setLocation(QuarterlyController.class.getResource("Quarterly.fxml"));
		
		scene = new Scene(loader.load());
		cont = loader.getController();
		
		transitionModel = new TransitionModel();
		model = new ClientModel(transitionModel);
		model.getNewClient("127.0.0.1");
		model.getClient().login("admin", "CSC300");
		model.getClient().setLocalCopy(new BusinessPlan());
		model.getBusinessPlan().setName("Test Name");
		model.getBusinessPlan().setYear("Test Year");
		model.getBusinessPlan().setId("Test ID");
		model.getBusinessPlan().setQuarter("Yearly");
		
		//Makes needed categories
		model.getBusinessPlan().addCategory("Test", 1, 1, 2);
		model.getBusinessPlan().addCategory("Limited", 2, 1, 1);
		model.getBusinessPlan().setCategoryList();
		
		//Creates pre-made statements that will be needed for specific tests
		model.getBusinessPlan().addStatement("This is a test statement", "Test", 
				model.getBusinessPlan().getTree().getRoot(), "Test Statement 1");
		model.getBusinessPlan().addStatement("This is a test statement", "Limited", 
				model.getBusinessPlan().getTree().getRoot(), "Test Statement 2");
		model.getBusinessPlan().addStatement("This statement should keep this full", "Limited", 
				model.getBusinessPlan().getTree().findStatement(
						model.getBusinessPlan().getTree().getRoot().getChildren(), "Test Statement 1"), "Filling Statement");
		Statement filler = new Statement();
		filler.setName("test");
		model.setCurrStatement(filler);
		
		transitionModel.setStage(stage);
		cont.setTransitionModel(transitionModel);
		cont.setClientModel(model);
		
		stage.setScene(scene);
		stage.show();
	}

	@BeforeAll
	public static void setUpServer()
	{
		ServerStartup.startLocalServer();
	}
	
	//test if selecting an option changes state
	@Test
	void testSelection(FxRobot robot)
	{
		robot.clickOn("#selectionBox");
		robot.clickOn("Yearly");
		robot.clickOn("#advanceButton");
		assertThat(model.getCurrentState().getName().equals("Yearly"));
	}
	
	//test the same thing, different state
	@Test
	void allSelection(FxRobot robot)
	{
		robot.clickOn("#selectionBox");
		robot.clickOn("All");
		robot.clickOn("#advanceButton");
		assertThat(model.getCurrentState().getName().equals("All"));
	}
	
	//test the same thing, different state
	@Test
	void twoSelection(FxRobot robot)
	{
		robot.clickOn("#selectionBox");
		robot.clickOn("Second");
		robot.clickOn("#advanceButton");
		assertThat(model.getCurrentState().getName().equals("Second"));
		robot.clickOn("12e: Second Example, 2017, Second");
	}
	
	//test the same thing, different state
		@Test
		void threeSelection(FxRobot robot)
		{
			robot.clickOn("#selectionBox");
			robot.clickOn("Third");
			robot.clickOn("#advanceButton");
			assertThat(model.getCurrentState().getName().equals("Third"));
		}
	
	//test the same thing, different state
	@Test
	void Selection(FxRobot robot)
	{
		robot.clickOn("#selectionBox");
		robot.clickOn("Fourth");
		robot.clickOn("#advanceButton");
		assertThat(model.getCurrentState().getName().equals("Fourth"));
	}
	
	//test if one can select a state and keep going, assuming that correct plans were pulled
	@Test
	void advanceSelection(FxRobot robot)
	{
		robot.clickOn("#selectionBox");
		robot.clickOn("First");
		robot.clickOn("#advanceButton");
		assertThat(model.getCurrentState().getName().equals("First"));
		robot.clickOn("1er: First Plan Example, 2018, First");
		assertThat(model.getBusinessPlan().getQuarter().equals("First"));
	}
}
