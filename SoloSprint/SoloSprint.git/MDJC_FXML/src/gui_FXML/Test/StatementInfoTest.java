package gui_FXML.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import clientServerClasses.ServerStartup;
import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class StatementInfoTest
{
	private MainWindowController cont;
	private FXMLLoader loader;
	private Scene scene;
	private TransitionModel transitionModel;
	private ClientModel model;
	
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
		model.getBusinessPlan().setYear("2018");
		model.getBusinessPlan().setId("Test ID");
		model.getBusinessPlan().addCategory("Test", 1, 1, 5);
		model.getBusinessPlan().setCategoryList();
		model.getBusinessPlan().addStatement("This is a test statement", "Test", 
				model.getBusinessPlan().getTree().getRoot(), "Test Statement 1");
		model.getBusinessPlan().addStatement("This is to test editing a statement.", "Test", 
				model.getBusinessPlan().getTree().getRoot(), "Test Statement 2");
		model.getBusinessPlan().addStatement("This is to test a statement's children", "Test", model.getBusinessPlan().getTree()
				.findStatement(model.getBusinessPlan().getTree().getRoot().getChildren(), "Test Statement 1"), "Child Test 1");
		
		//model.getBusinessPlan().addStatement("This is to test statement children", "Test", parent, name)
		
		transitionModel.setStage(stage);
		cont.setTransitionModel(transitionModel);
		cont.setClientModel(model);
		
		stage.setScene(scene);
		stage.show();
		
	}
	@BeforeAll
	public static void setUpServer()
	{
		//Instantiates models and sets to server login controller
		ServerStartup.startLocalServer();
	}
	
	@Test
	void correctDisplayTest(FxRobot robot)
	//Tests that when you click on a Statement, StatementInfoView appears
	{
		
		//Choose a statement
		robot.doubleClickOn("Business Plan");
		//robot.clickOn("Test");
		robot.clickOn("Test Statement 1");
		
		//Assert that the Name is correctly displayed
		assertThat(robot.lookup("#statementName").queryTextInputControl().getText().equals("Test Statement 1"));
		
		//Assert that the data is correct
		assertThat(robot.lookup("#statementData").queryTextInputControl().getText().equals("This is a test statement"));
		
	}
	
	@Test 
	void editingStatementTest(FxRobot robot)
	//Tests that edits to a Statement actually work
	{
		robot.doubleClickOn("Business Plan");
		//Choose a statement
		robot.clickOn("Test Statement 2");
		//Change the name
		robot.clickOn("#statementName");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("Testing Edit Statement Info");
		
		//Change the data
		robot.clickOn("#statementData");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("Editing of a statement works");
		robot.clickOn("#saveStatement");

		//Go find and assert that the name is the same as the new one
		assertThat(model.getBusinessPlan().getTree().findStatement(model.getBusinessPlan()
				.getTree().getRoot().getChildren(), "Testing Edit Statement Info")).isNotNull();
		assertThat(model.getBusinessPlan().getTree().findStatement(model.getBusinessPlan()
				.getTree().getRoot().getChildren(), "Test Statement 2")).isNull();
		//Go find and assert that the data is the same as the new one
		assertThat(model.getBusinessPlan().getTree().findStatement(model.getBusinessPlan()
				.getTree().getRoot().getChildren(), "Testing Edit Statement Info")
				.getData().equals("Editing of a statement works"));
	}
	
}

