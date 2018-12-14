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

import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;
import mdjcBusinessPlanClasses.Statement;

@ExtendWith(ApplicationExtension.class)
public class CommentsTest
{
	private MainWindowController cont;
	private FXMLLoader loader;
	private Scene scene;
	private static TransitionModel transitionModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws IOException 
	{		
		loader = new FXMLLoader();
		loader.setLocation(MainWindowController.class.getResource("MainWindow.fxml"));
		
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
	
	@Test
	void categoryComments(FxRobot robot)
	{
		//Click sequence to make it display in the center
		robot.clickOn("Test");
		robot.rightClickOn("Test");
		robot.clickOn("#comments");
		robot.clickOn("#leaveButton");
		robot.clickOn("#scrollPane");
		robot.clickOn("New Comment");
		//necessary backspaces....
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		
		robot.write("Writing Comment");
		robot.clickOn("Save");
		
		robot.clickOn("Test");
		robot.rightClickOn("Test");
		robot.clickOn("#comments");
		robot.clickOn("#leaveButton");
		robot.clickOn("#scrollPane");
		robot.clickOn("New Comment");
		
		assertThat(model.getCurrStatement().getComments().contains("Writing Comment"));
		assertThat(model.getCurrStatement().getComments().contains("New Comment"));
		
		robot.clickOn("Test");
		robot.clickOn("Limited");
		robot.rightClickOn("Limited");
		robot.clickOn("#comments");
		assertThat(model.getCurrStatement().getComments().isEmpty());
		
		robot.clickOn("#leaveButton");
		robot.clickOn("New Comment");
		//necessary backspaces....
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		
		robot.write("Change Test");
		robot.clickOn("Save");
		
		robot.clickOn("Test");
		robot.clickOn("Limited");
		robot.rightClickOn("Limited");
		robot.clickOn("#comments");
		assertThat(!model.getCurrStatement().getComments().isEmpty());
		
		robot.clickOn("Change Test");
		robot.push(KeyCode.BACK_SPACE);
		robot.push(KeyCode.BACK_SPACE);
		assertThat(model.getCurrStatement().getComments().contains("Change Te"));
	}
	
	@Test
	void statementComments(FxRobot robot)
	{
		//Click sequence to make it display in the center
				robot.doubleClickOn("Business Plan");
				robot.rightClickOn("Test Statement 1");
				robot.clickOn("#comments");
				robot.clickOn("#leaveButton");
				robot.clickOn("#scrollPane");
				robot.clickOn("New Comment");
				//necessary backspaces....
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				robot.push(KeyCode.BACK_SPACE);
				
				robot.write("Writing Comment");
				robot.clickOn("Save");
				
				robot.clickOn("Business Plan");
				robot.rightClickOn("Test Statement 1");
				robot.clickOn("#comments");
				robot.clickOn("#leaveButton");
				robot.clickOn("#scrollPane");
				robot.clickOn("New Comment");
				
				assertThat(model.getCurrStatement().getComments().contains("Writing Comment"));
				assertThat(model.getCurrStatement().getComments().contains("New Comment"));
				
				robot.clickOn("Business Plan");
				robot.doubleClickOn("Test Statement 1");
				robot.rightClickOn("Filling Statement");
				robot.clickOn("#comments");
				assertThat(model.getCurrStatement().getComments().isEmpty());
	}
}
