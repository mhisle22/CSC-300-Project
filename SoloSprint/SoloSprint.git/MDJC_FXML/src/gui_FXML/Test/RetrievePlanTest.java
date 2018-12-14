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
import gui_FXML.InfoView.InfoViewController;
import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.StatementView.StatementViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class RetrievePlanTest
{
	private MainWindowController cont;
	private StatementViewController treeCont;
	private Scene s;
	private InfoViewController infoCont;
	private static TransitionModel transitionModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindowController.class.getResource("MainWindow.fxml"));
		try {
			s = new Scene(loader.load());
			cont = loader.getController();
			
			transitionModel = new TransitionModel();
			model = new ClientModel(transitionModel);
			model.getNewClient("127.0.0.1");
			model.getClient().login("admin", "CSC300");
			
			model.getClient().setLocalCopy(new BusinessPlan());
			
			transitionModel.setStage(stage);
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(model);
			treeCont = cont.getStatementViewController();
			infoCont = cont.getInfoViewController();
			treeCont.expandRoot();
			transitionModel.setSelectedTreeName("");
			
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
	//Tests that if current plan was not saved recently, a popup should be prompted
	void retrievePopupTest(FxRobot robot)
	{
		robot.clickOn("#retrieveButton");
		WaitForAsyncUtils.waitForFxEvents();
		robot.clickOn("#VMOSA_2018");
		
		assertThat(cont.getTransitionModel().getSavePopUpMessage()).
		isEqualTo("This plan has not been submitted recently."
				+ " Would you like to submit to the server?");
		
		assertThat(cont.getTransitionModel().getPopUpTitle()).isEqualTo("Plan Not Submitted");
		assertThat(cont.getTransitionModel().isRecentlySaved()).isFalse();
		
		
	}
	
	@Test
	//Tests that an original plan can be retrieved and displayed properly
	void originalRetrieveTest(FxRobot robot)
	{
		transitionModel.setRecentlySaved(true);
		robot.clickOn("#retrieveButton");
		WaitForAsyncUtils.waitForFxEvents();
		robot.clickOn("#VMOSA_2018");
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.from(infoCont.getCenter()).lookup("#emptyInfoText").queryText()).isNotNull();
		assertThat(robot.lookup("#nameField").queryAs(TextField.class).getText()).isEqualTo("VMOSA");
		assertThat(robot.lookup("#idField").queryAs(TextField.class).getText()).isEqualTo("VMOSA_2018");
		assertThat(robot.lookup("#yearField").queryAs(TextField.class).getText()).isEqualTo("2018");
		assertThat(model.getClient().getLocalCopy().getTree().findStatement(
				model.getClient().getLocalCopy().getTree().getRoot().getChildren(), "Functionality")).isNotNull();
	}
	
	@Test
	//Tests that a cloned plan can be retrieved and displayed properly
	void cloneRetrieveTest(FxRobot roboto)
	{
		transitionModel.setRecentlySaved(true);
		roboto.clickOn("#retrieveButton");
		WaitForAsyncUtils.waitForFxEvents();
		roboto.clickOn("#clone");
		roboto.clickOn("#VMOSA_2018");
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(roboto.from(infoCont.getCenter()).lookup("#emptyInfoText").queryText()).isNotNull();
		assertThat(roboto.lookup("#nameField").queryAs(TextField.class).getText()).isEqualTo("VMOSA");
		assertThat(roboto.lookup("#idField").queryAs(TextField.class).getText()).isEmpty();
		assertThat(roboto.lookup("#yearField").queryAs(TextField.class).getText()).isEmpty();
		assertThat(model.getClient().getLocalCopy().getTree().findStatement(
				model.getClient().getLocalCopy().getTree().getRoot().getChildren(), "Functionality")).isNotNull();
	}
}