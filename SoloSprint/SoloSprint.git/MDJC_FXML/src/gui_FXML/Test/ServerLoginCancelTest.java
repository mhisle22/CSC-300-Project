package gui_FXML.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import clientServerClasses.ServerStartup;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.ServerLoginView.ServerLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class ServerLoginCancelTest
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
		
	
	@Test
	void testCancelButton(FxRobot robot)
	{
		robot.clickOn("#cancelButton");
		
		assertThat(stage.isShowing()).isFalse();
		assertThat(ServerStartup.getUnbound()).isTrue();
	}
	

}
