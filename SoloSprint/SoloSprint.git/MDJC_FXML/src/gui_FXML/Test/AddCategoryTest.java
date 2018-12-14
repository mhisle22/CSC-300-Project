package gui_FXML.Test;

import static org.assertj.core.api.Assertions.assertThat;



import java.io.IOException;

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
public class AddCategoryTest
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
		
		//Instantiates models and sets to server login controller
		ServerStartup.startLocalServer();
		
		transitionModel = new TransitionModel();
		model = new ClientModel(transitionModel);
		model.getNewClient("127.0.0.1");
		model.getClient().login("admin", "CSC300");
		model.getClient().setLocalCopy(new BusinessPlan());
		model.getBusinessPlan().setName("Test Name");
		model.getBusinessPlan().setYear("Test Year");
		model.getBusinessPlan().setId("Test ID");
		model.getBusinessPlan().addCategory("Testing Category Name", 1, 1, 1);
		
		transitionModel.setStage(stage);
		cont.setTransitionModel(transitionModel);
		cont.setClientModel(model);
		
		stage.setScene(scene);
		stage.show();
		
		
	}
	
	@Test
	void workingCategory(FxRobot robot)
	//Tests that a valid category can be added to a Business Plan using the program
	{
		robot.clickOn("#addCategoryButton");
		
		robot.clickOn("#catNameField");
		robot.write("Testing Category");
		robot.clickOn("#rankField");
		robot.write("2");
		robot.clickOn("#minField");
		robot.write("1");
		robot.clickOn("#maxField");
		robot.write("2");
		
		robot.clickOn("#addToPlanButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		//Asserts that the new category was actually added to the Plan

		robot.doubleClickOn("Testing Category");
		assertThat(robot.lookup("Rank: 1")).isNotNull();
		assertThat(robot.lookup("Maximum Statements: 2")).isNotNull();
		assertThat(robot.lookup("Minimum Statements: 1")).isNotNull();
		
	}
	

	@Test
	void emptyInputsTest(FxRobot robot)
	//Tests that the empty pop sequence will work if an empty category is added
	{
		robot.clickOn("#addCategoryButton");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for Empty Min
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field Minimum Number of Statements is empty. Please input a Minimum Number of Statements for this category before continuing");
		
		robot.clickOn("#minField");
		robot.write("1");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for empty Max
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field Maximum Number of Statements is empty. Please input a Maximum Number of Statements for this category before continuing");
		
		robot.clickOn("#maxField");
		robot.write("2");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for empty Rank
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field Rank of Statement is empty. Please input a Rank of Statement for this category before continuing");
		
		robot.clickOn("#rankField");
		robot.write("1");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for empty Name
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input field name is empty. Please input a name for this category before continuing");
		
		robot.clickOn("#catNameField");
		robot.write("Testing Category");
		robot.clickOn("#addToPlanButton");
		

	}
	
	@Test
	void invalidInputTest(FxRobot robot)
	//Tests that the correct PopUps display when invalid data is input
	{
	
		robot.clickOn("#addCategoryButton");
		//Type in an invalid everything (ie letters for min, max, rank, and existing for name)
		robot.clickOn("#catNameField");
		robot.write("Testing Category Name");
		robot.clickOn("#rankField");
		robot.write("rank");
		robot.clickOn("#minField");
		robot.write("min");
		robot.clickOn("#maxField");
		robot.write("max");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUP for invalid min
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input for Minimum Number of Statements is not a valid integer. Please"
				+ "input a valid integer for Minimum Number of Statements before continuing");
		//Type in valid min
		robot.clickOn("#minField");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("1");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for invalid max
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input for Maximum Number of Statements is not a valid integer. Please"
				+ "input a valid integer for Maximum Number of Statements before continuing");
		//Type in valid max
		robot.clickOn("#maxField");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("2");
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for invalid rank
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("The input for Rank of Statement is not a valid integer. Please"
				+ " input a valid integer for Rank of Statement before continuing");
		//Type in rank already exists
		robot.clickOn("#rankField");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("1"); //its not taking the 2 I put here as an integer?
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for existing rank
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("There's already a category with this rank or name."
				+ " Please check the Category view and input a different name or rank.");
		//Type in rank that doesn't exist
		robot.clickOn("#rankField");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("2"); //its not taking the 2 I put here as an integer?
		robot.clickOn("#addToPlanButton");
		
		//Test for PopUp for existing name
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("There's already a category with this rank or name."
				+ " Please check the Category view and input a different name or rank.");
		robot.clickOn("#catNameField");
		robot.push(javafx.scene.input.KeyCode.SHORTCUT,
				javafx.scene.input.KeyCode.A);
		robot.write("Extinct Animal Test");
		robot.clickOn("#addToPlanButton");
		
	

		
		
	}
}

