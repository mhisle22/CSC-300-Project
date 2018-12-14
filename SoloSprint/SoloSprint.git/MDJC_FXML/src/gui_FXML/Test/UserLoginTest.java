package gui_FXML.Test;

import java.io.IOException;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import clientServerClasses.ServerStartup;

import static org.testfx.assertions.api.Assertions.assertThat;


import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.UserLoginView.UserLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class UserLoginTest
{
	private Stage stage;
	private UserLoginController cont;
	private FXMLLoader loader;
	private Scene s;
	private TransitionModel transModel = new TransitionModel();
	private ClientModel model = new ClientModel(transModel);
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		this.stage=stage;
		
		loader = new FXMLLoader();
		loader.setLocation(UserLoginController.class.getResource("UserLoginWindow.fxml"));
		try {
			s = new Scene(loader.load());
			cont = loader.getController();
			
			transModel = new TransitionModel();
			model = new ClientModel(transModel);
			model.getNewClient("127.0.0.1");
			
			cont.setTransitionModel(transModel);
			cont.setClientModel(model);
			
			stage.setScene(s);
			stage.show();
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
	@BeforeAll
	static void setUpServer()
	{
		ServerStartup.startLocalServer();
	}
	
	@BeforeEach
	void setStage()
	{
		transModel.setStage(stage);
		model.setTransitionModel(transModel);
	}
	
	@AfterAll
	static void breakDownServer(FxRobot robot)
	{
		ServerStartup.unbindServer();
	}
	
	@Test
	void userLoginTest(FxRobot robot)
	{
		robot.clickOn("#usernamefield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("admin");
		
		robot.clickOn("#passwordfield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("CSC300");
		
		robot.clickOn("#connect");
			
		assertThat(robot.lookup("#cloneRadio")).isNotNull();

	}
	
	// Verifies that wrong username and wrong password fails
	@Test
	void firstFailTest(FxRobot robot)
	{
		robot.clickOn("#usernamefield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("wronguser");
		
		robot.clickOn("#passwordfield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("wrongpass");
		
		robot.clickOn("#connect");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("You have entered an invalid username and password combination");
		assertThat(cont.getTransitionModel().getPopUpTitle()).
		isEqualTo("User Login Failed");
	}
		
	// Verifies that right username and wrong password fails	
	@Test
	void secondFailTest(FxRobot robot)
	{	
		robot.clickOn("#usernamefield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("admin");
		
		robot.clickOn("#passwordfield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("wrongpass");
		
		robot.clickOn("#connect");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("You have entered an invalid username and password combination");
		assertThat(cont.getTransitionModel().getPopUpTitle()).
		isEqualTo("User Login Failed");
	}	
		
	// Verifies that wrong username and fails password fails		
	@Test
	void thirdFailTest(FxRobot robot)
	{	
		robot.clickOn("#usernamefield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("wronguser");
		
		robot.clickOn("#passwordfield");
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		robot.write("CSC300");
		
		robot.clickOn("#connect");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("You have entered an invalid username and password combination");
		assertThat(cont.getTransitionModel().getPopUpTitle()).
		isEqualTo("User Login Failed");
	}
	

	
	
	
	

}