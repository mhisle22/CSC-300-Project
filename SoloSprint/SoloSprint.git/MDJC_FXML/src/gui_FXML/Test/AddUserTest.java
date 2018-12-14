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

import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.ServerLoginView.ServerLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class AddUserTest
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
		try 
		{
			s = new Scene(loader.load());
			cont = loader.getController();
			//Instantiates models and sets to server login controller
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
	
	@Test
	void clientTest(FxRobot robot)
	{
		//type in all data for a valid user
		robot.clickOn("#addUserButton");
		
		robot.clickOn("#usernameField");
		robot.write("Test Dummy");
		
		robot.clickOn("#passwordField");
		robot.write("password");
		
		robot.clickOn("#departmentField");
		robot.write("Safety");
		
		robot.clickOn("#notAdmin");
		
		//add it
		robot.clickOn("#addUButton");
		
		try
		{
			assertThat(model.getClient().getStub().authenticate("Test Dummy", "password") != null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			assertThat(false);
		}
	}
	
	@Test
	void adminTest(FxRobot robot)
	{
		//type in data for a valid user
		robot.clickOn("#addUserButton");
		
		robot.clickOn("#usernameField");
		robot.write("Test Dummy");
		
		robot.clickOn("#passwordField");
		robot.write("password");
		
		robot.clickOn("#departmentField");
		robot.write("Safety");
		
		//this one will be an admin
		robot.clickOn("#isAdmin");
		
		robot.clickOn("#addUButton");
		
		try
		{
			assertThat(model.getClient().getStub().authenticate("Test Dummy", "password") != null);
			//missing code for admin
		}
		catch(IOException e)
		{
			e.printStackTrace();
			assertThat(false);
		}
		
	}
	
}

