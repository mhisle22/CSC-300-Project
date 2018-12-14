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
import gui_FXML.ServerLoginView.ServerLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class ServerLoginTest
{
	private Stage stage;
	private ServerLoginController cont;
	private FXMLLoader loader;
	private Scene s;
	private TransitionModel transModel = new TransitionModel();
	private ClientModel model = new ClientModel(transModel);
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		this.stage=stage;
		
		loader = new FXMLLoader();
		loader.setLocation(ServerLoginController.class.getResource("ServerLoginWindow.fxml"));
		try {
			s = new Scene(loader.load());
			cont = loader.getController();
			//Instantiates models and sets to server login controller
			transModel.setStage(stage);
			cont.setClientModel(model);
			cont.setTransitionModel(transModel);
			
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
	void connectToServerTest(FxRobot robot)
	{
		robot.clickOn("#servertextboxid");
		
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
				
		robot.write("127.0.0.1");
		
		robot.clickOn("#connectButton");

		assertThat(cont.getClientModel().getClient()).isNotNull();
		assertThat(cont.getClientModel().getClient().getStub()).isNotNull();
		assertThat(cont.getClientModel().getClient().getHost()).isEqualTo("127.0.0.1");
	}
	
	@Test
	void nonExistentServerConnection(FxRobot robot)
	{
		robot.clickOn("#servertextboxid");
		
		//need to clear existing text
		robot.push(javafx.scene.input.KeyCode.SHORTCUT, javafx.scene.input.KeyCode.A);
		
		robot.write("123");
		
		robot.clickOn("#connectButton");
		
		assertThat(cont.getTransitionModel().getOKPopUpMessage()).
		isEqualTo("Unable to connect to server. \nPlease check your connection or try again later.");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Connection Error");
	}
	

	
	
	
	

}
