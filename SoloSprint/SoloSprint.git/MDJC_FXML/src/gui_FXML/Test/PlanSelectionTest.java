package gui_FXML.Test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import clientServerClasses.ServerStartup;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.geometry.VerticalDirection;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.PlanSelection.PlanSelection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class PlanSelectionTest
{
	private Stage stage;
	private PlanSelection cont;
	private FXMLLoader loader;
	private Scene s;
	private static TransitionModel transModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		//load up the correct window
		this.stage = stage;
		loader = new FXMLLoader();
		loader.setLocation(PlanSelection.class.getResource("PlanSelection.fxml"));
		try 
		{
			s = new Scene(loader.load());
			cont = loader.getController();
			
			//set up preliminary attributes so we can actually test something
			transModel = new TransitionModel();
			model = new ClientModel(transModel);
			model.getNewClient("127.0.0.1");
			model.getClient().login("admin", "CSC300");
			model.getClient().setLocalCopy(new BusinessPlan());
			model.getBusinessPlan().setName("Test Name");
			model.getBusinessPlan().setYear("Test Year");
			model.getBusinessPlan().setId("Test ID");
			
			transModel.setStage(stage);
			cont.setTransitionModel(transModel);
			cont.setClientModel(model);
			//now that its set up, switch scenes
			stage.setScene(s);
			stage.show();
		} 
		catch (IOException e)
		{
			
			e.printStackTrace();
		}	
	}
	
	@BeforeAll
	static void setUpServer()
	{
		ServerStartup.startLocalServer();
	}
	
	@Test
	void newPlanTest(FxRobot robot)
	{
		robot.clickOn("#newButton");
		assertThat(cont.getClientModel().getClient().getLocalCopy().getId()).isEqualTo(""); //check if it didn't pull a current one
	}
	
	@Test
	void clonePlanTest(FxRobot robot)
	{
		robot.clickOn("#cloneRadio");
		robot.clickOn("##13");
		//check to see if it got the correct plan based off of plan ID number
		assertThat(cont.getClientModel().getClient().getLocalCopy().getId()).isEqualTo(""); //would be "" for cloned
		
	}
	
	@Test
	void existingSelectTest(FxRobot robot)
	{
		robot.clickOn("#originalRadio");
		robot.clickOn("##13");
		//check to see if it got the correct plan based off of plan ID number
		assertThat(cont.getClientModel().getClient().getLocalCopy().getId()).isEqualTo("#13"); //would be 13 for this specific example
	}
	
	@Test
	void scrollSelectTest(FxRobot robot)
	{
		robot.clickOn("#originalRadio");
		//scroll
		robot.moveBy(90, 413);
		robot.clickOn();
		robot.scroll(12, javafx.geometry.VerticalDirection.DOWN);
		//now click on button
		robot.clickOn("##16");
		//check to see if it got the correct plan based off of plan ID number
		assertThat(cont.getClientModel().getClient().getLocalCopy().getId()).isEqualTo("#16");
	}
}
