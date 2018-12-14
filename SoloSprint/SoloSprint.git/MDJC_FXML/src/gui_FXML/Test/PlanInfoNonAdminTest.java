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
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class PlanInfoNonAdminTest
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
			transModel = new TransitionModel();
			model = new ClientModel(transModel);
			model.getNewClient("127.0.0.1");
			model.getClient().login("user", "amUser");
			model.getClient().setLocalCopy(new BusinessPlan());
			model.getBusinessPlan().setName("Test Name");
			model.getBusinessPlan().setYear("Test Year");
			model.getBusinessPlan().setId("Test ID");
			model.getBusinessPlan().setIsEditable(false);
			
			s = new Scene(loader.load());
			cont = loader.getController();
			
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
	//Make sure that if the plan is NOT editable and the user is NOT an admin, all text fields, radio buttons,
	// and the Add User button is disabled
	void disabledPlanInfoTest(FxRobot robot)
	{
		assertThat(robot.lookup("#nameField").queryAs(TextField.class).isDisabled()).isTrue();
		assertThat(robot.lookup("#idField").queryAs(TextField.class).isDisabled()).isTrue();
		assertThat(robot.lookup("#yearField").queryAs(TextField.class).isDisabled()).isTrue();
		assertThat(robot.lookup("#noEdit").queryAs(RadioButton.class).isDisabled()).isTrue();
		assertThat(robot.lookup("#canEdit").queryAs(RadioButton.class).isDisabled()).isTrue();
		assertThat(robot.lookup("#addUserButton").queryAs(Button.class).isDisabled()).isTrue();
	}
	
}