package gui_FXML.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.rmi.server.ExportException;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class PlanInfoTest
{
	private MainWindowController cont;
	private FXMLLoader loader;
	private Scene s;
	private static TransitionModel transModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		loader = new FXMLLoader();
		loader.setLocation(MainWindowController.class.getResource("MainWindow.fxml"));
		try {
			s = new Scene(loader.load());
			cont = loader.getController();
			
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
			
			stage.setScene(s);
			stage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	@BeforeAll
	public static void setUpServer()
	{
		ServerStartup.startLocalServer();
	}
	
	//New button provides popUp message to create a PopupB if not recently saved
	//Tests that this does happen
	@Test
	void newPlanPopupTest(FxRobot robot)
	{
		robot.clickOn("#newButton");
		
		assertThat(cont.getTransitionModel().getSavePopUpMessage()).
		isEqualTo("This plan has not been submitted recently."
				+ " Would you like to submit to the server?");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Plan Not Submitted");
		assertThat(cont.getTransitionModel().isRecentlySaved()).isFalse();
	}
	
	//If plan was recently saved, a new plan is created and the main window is redrawn
	@Test
	void newPlanCreatedTest(FxRobot robot)
	{
		cont.getTransitionModel().setRecentlySaved(true);
		
		robot.clickOn("#newButton");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(model.getBusinessPlan().getName()).isEmpty();
		assertThat(model.getBusinessPlan().getYear()).isEmpty();
		assertThat(model.getBusinessPlan().getId()).isEmpty();
		
		assertThat(robot.lookup("#statementTree").queryAs(TreeView.class).getRoot().getChildren()).isEmpty();
		assertThat(robot.lookup("#categoryTree").queryAs(TreeView.class).getRoot().getChildren()).isEmpty();
		assertThat(robot.lookup("#emptyInfoText").queryAs(Text.class)).isNotNull();
	}
	
	@Test
	//If the Retrieve Plan button is pressed, the RetrievePlanView should be displayed
	void retrievePlanTest(FxRobot robot)
	{
		cont.getTransitionModel().setRecentlySaved(true);
		
		robot.clickOn("#retrieveButton");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#clone").queryAs(RadioButton.class)).isNotNull();
	}
	
	@Test
	//If submit is pressed, the plan should be saved in the server (and show up in the RetrievePlanView)
	void submitPlanTest(FxRobot robot)
	{
		cont.getClientModel().getBusinessPlan().setName("TestPlan");
		cont.getClientModel().getBusinessPlan().setYear("2000");
		cont.getClientModel().getBusinessPlan().setId("Test ID");
		
		robot.clickOn("#submitButton");
		
		WaitForAsyncUtils.waitForFxEvents();
		robot.clickOn("#retrieveButton");
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("Test ID: TestPlan, 2000").queryButton()).isNotNull();
	}
	
	@Test
	//Tests that the plan will not be submitted unless valid inputs are given for the plan info
	void dataValidateSubmitTest(FxRobot robot)
	{
		cont.getClientModel().getBusinessPlan().setName("");
		cont.getClientModel().getBusinessPlan().setYear("");
		cont.getClientModel().getBusinessPlan().setId("");
		
		robot.clickOn("#submitButton");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field name is empty. Please input a name for this business plan before saving");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Empty Input Field");
		cont.getClientModel().getBusinessPlan().setName("Valid Name");
		
		robot.clickOn("#submitButton");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field ID is empty. Please input an ID for this business plan before saving");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Empty Input Field");
		cont.getClientModel().getBusinessPlan().setId("Valid ID");
		
		robot.clickOn("#submitButton");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field year is empty. Please input a year for this business plan before saving");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Empty Input Field");
		cont.getClientModel().getBusinessPlan().setYear("Invalid Year");
		
		robot.clickOn("#submitButton");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("Input for 'year' is not a valid integer."
				+ " Please enter a valid input for the business plan year.");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Invalid Year Input");
		cont.getClientModel().getBusinessPlan().setYear("1998");
		
		robot.clickOn("#submitButton");
		
		WaitForAsyncUtils.waitForFxEvents();
		robot.clickOn("#retrieveButton");
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("Valid ID: Valid Name, 1998").queryButton()).isNotNull();
	}
	
	@Test
	//Tests that when the Add User button is clicked, the AddUserVew is displayed
	void addUserTest(FxRobot robot)
	{
		robot.clickOn("#addUserButton");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#addUButton").queryButton()).isNotNull();
	}
	
	@Test
	//The name/id/year fields should save to the plan whenever the text field loses focus
	void textFieldSaveTest(FxRobot robot)
	{
		robot.clickOn("#nameField");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("ThisShouldSaveName");
		
		robot.clickOn("#idField");
		assertThat(cont.getClientModel().getBusinessPlan().getName()).isEqualTo("ThisShouldSaveName");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("ThisShouldSaveID");
		
		robot.clickOn("#yearField");
		assertThat(cont.getClientModel().getBusinessPlan().getId()).isEqualTo("ThisShouldSaveID");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("ThisShouldSaveYear");
		
		robot.clickOn("#noEdit");
		assertThat(cont.getClientModel().getBusinessPlan().getYear()).isEqualTo("ThisShouldSaveYear");
		
		robot.clickOn("#canEdit");
		assertThat(cont.getClientModel().getBusinessPlan().isEditable()).isFalse();
		
		robot.clickOn("#nameField");
		assertThat(cont.getClientModel().getBusinessPlan().isEditable()).isTrue();
		
		
	}
	
}
