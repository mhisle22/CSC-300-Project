package gui_FXML.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import clientServerClasses.ServerStartup;
import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class AddStatementTest
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
	void workingAddStatement(FxRobot robot)
	//Tests that a valid Statement is actually added to a BP when called to
	{
		//Click sequence to make it display in the center
		robot.clickOn("Business Plan");
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		//Assert that the label of the center pane says Add Statement
		//assertThat(robot.lookup("#titleLabel").queryAs(Label.class).getTest().isEqualTo("Add New Category"));
		//Input good name and data
		robot.clickOn("#sNameField");
		robot.write("Testing Statement");
		robot.clickOn("#sField");
		robot.write("This tests for adding a working statement");
		
		robot.clickOn("#addStatementButton");
		WaitForAsyncUtils.waitForFxEvents();
		//Assert that the statement exists in the list
		assertThat(cont.getClient().getLocalCopy().getTree().findStatement(cont.getClient()
				.getLocalCopy().getTree().getRoot().getChildren(), "Testing Statement")).isNotNull();
		robot.clickOn("#addCategoryButton");
		robot.doubleClickOn("Business Plan");
		assertThat(robot.lookup("Testing Statement")).isNotNull();
		
		
	}
	
	@Test
	void emptyInputTest(FxRobot robot)
	//Tests that the correct PopUps display when an empty Statement is added
	{
		//Click stuff to make it display in the center
		robot.clickOn("Business Plan");
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		robot.clickOn("#addStatementButton");
		//Test that PopUp for blank data displays
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("You must input a statement before you can add it");
		//write in data
		robot.clickOn("#sField");
		robot.write("This used to be empty");
		robot.clickOn("#addStatementButton");
		//Test that PopUp for blank Name
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("You must give the new statement a unique name before you can add it");
		//Write in name
		robot.clickOn("#sNameField");
		robot.write("Empty Tester");
		robot.clickOn("#addStatementButton");
	
	}
	
	@Test
	void tooManyStatementsTest(FxRobot robot)
	//Tests that the correct PopUp displays when max number os statements has been reached
	{
		//Add specific category with restricting Max num of statements
		//Click stuff to make it display in the center
		robot.doubleClickOn("Business Plan");
		robot.doubleClickOn("Test Statement 1");
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		//Write in valid name and data
		robot.clickOn("#sNameField");
		robot.write("Excess Statement");
		robot.clickOn("#sField");
		robot.write("There should be too many to get in.");
		robot.clickOn("#addStatementButton");
		WaitForAsyncUtils.waitForFxEvents();
		//Test that PopUp for Too Many Statements
		assertThat(cont.getClient().getLocalCopy().getTree().findStatement(cont.getClient()
				.getLocalCopy().getTree().getRoot().getChildren(), "Excess Statement")).isNull();
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("There are too many statements in the category Limited. Please remove a statement"
				+ " from category Limited before adding a new one");
		
		
	}
	
	@Test
	void alreadyExistsTest(FxRobot robot)
	//Tests that the correct popUp displays when an added statement already exists
	{
		//Click sequence to make it display in the center
		robot.clickOn("Business Plan");
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
	
		//write in same data
		robot.clickOn("#sNameField");
		robot.write("Test Statement 1");
		robot.clickOn("#sField");
		robot.write("I may already exist");
		robot.clickOn("#addStatementButton");
		//Test that PopUp for Name Already Exists 
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("A statement of category Test already exists with the name Test Statement 1. "
				+ "Please choose a different unique name");
	}
	
}
