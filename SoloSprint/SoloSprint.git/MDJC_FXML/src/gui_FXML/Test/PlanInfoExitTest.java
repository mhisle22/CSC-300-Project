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
public class PlanInfoExitTest
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
	
	@Test
	//Checks that if plan is NOT recently saved, a save popup is initiated
	public void clickExitProgramPopup(FxRobot robot)
	{
		robot.clickOn("#exitButton");
		
		assertThat(cont.getTransitionModel().getSavePopUpMessage()).
		isEqualTo("This plan has not been submitted recently."
				+ " Would you like to submit to the server?");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Plan Not Submitted");
		assertThat(cont.getTransitionModel().isRecentlySaved()).isFalse();
	}
	
	@Test
	//Checks that if a plan IS recently saved, the program closes correctly
	public void clickExitProgramTest(FxRobot robot)
	{
		cont.getTransitionModel().setRecentlySaved(true);
		
		robot.clickOn("#exitButton");
		assertThat(cont.getTransitionModel().getStage().isShowing()).isFalse();
		assertThat(ServerStartup.getUnbound()).isTrue();
	}
}