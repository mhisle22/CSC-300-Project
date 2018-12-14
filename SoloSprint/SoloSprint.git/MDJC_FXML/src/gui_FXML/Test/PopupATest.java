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
import org.testfx.util.WaitForAsyncUtils;

import clientServerClasses.ServerStartup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.assertions.api.Assertions.assertThat;

import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.ServerLoginView.ServerLoginController;
import gui_FXML.popupA.PopupA;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class PopupATest
{
	private PopupA cont;
	private FXMLLoader loader;
	private Scene s;
	private static TransitionModel transModel;
	private static ClientModel model;
	private Stage stage;
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		transModel = new TransitionModel();
		PopupA.setModel(transModel);
		PopupA.setStage(stage);
		transModel.setPopUpTitle("Test Window");
		transModel.setOKPopUpMessage("Test!");
	}
	
	@BeforeAll
	static void setUpServer()
	{
		ServerStartup.startLocalServer();
	}
	
	@Test
	void okayTest(FxRobot robot)
	{
		assertThat(PopupA.getStage().getTitle()).isEqualTo("Test Window");
		assertThat(robot.lookup("#labelA").queryAs(Label.class).getText()).isEqualTo("Test!");
		robot.clickOn("#OkayButton");
		
		assertThat(stage.isShowing()).isFalse();
		
	}
}
