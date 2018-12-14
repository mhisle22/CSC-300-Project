package gui_FXML.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class InfoControllerTest
{
	private MainWindowController cont;
	private FXMLLoader loader;
	private Scene s;
	private StatementViewController treeCont;
	private InfoViewController infoCont;
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
			model.getBusinessPlan().addCategory("Values", 1, 1, 2);
			model.getBusinessPlan().addCategory("Mission", 2, 1, 5);
			model.getBusinessPlan().setCategoryList();
			
			model.getBusinessPlan().addStatement("I am a Value!", "Values",
			model.getBusinessPlan().getTree().getRoot(), "Value1");
			
			model.getBusinessPlan().addStatement("I am another Value!", "Values", 
					model.getBusinessPlan().getTree().getRoot(), "Value2");
			
			model.getBusinessPlan().addStatement("I am the only Mission!", 
					"Mission", model.getBusinessPlan().getTree().
					findStatement(model.getBusinessPlan().getTree().getRoot().getChildren(), "Value1"), "Mission1");
			
			transModel.setStage(stage);
			cont.setTransitionModel(transModel);
			cont.setClientModel(model);
			treeCont = cont.getStatementViewController();
			infoCont = cont.getInfoViewController();
			treeCont.expandRoot();
			treeCont.getTree().getRoot().getChildren().get(0).setExpanded(true);
			treeCont.deselectAllItems();
			
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
	//Cycle through view transitions to ensure they all execute as expected
	void viewTransitionTest(FxRobot robot)
	{
		for(int i = 0; i < 2; i++)
		{
			robot.clickOn("#retrieveButton");
			WaitForAsyncUtils.waitForFxEvents();
			assertThat(robot.lookup("#clone").queryAs(RadioButton.class)).isNotNull();
			
			robot.clickOn("#addUserButton");
			WaitForAsyncUtils.waitForFxEvents();
			assertThat(robot.lookup("#addUButton").queryButton()).isNotNull();
			
			robot.clickOn("Value1");
			assertThat(robot.lookup("#statementName").queryAs(TextField.class).getText()).isEqualTo("Value1");
			assertThat(robot.lookup("#statementData").queryAs(TextArea.class).getText()).isEqualTo("I am a Value!");
			assertThat(robot.lookup("#categoryName").queryAs(Label.class).getText()).isEqualTo("Values");
			
			robot.clickOn("Business Plan");
			
			robot.rightClickOn("#statementTree");
			robot.clickOn("#addMenuItem");
			
			WaitForAsyncUtils.waitForFxEvents();
			assertThat(robot.lookup("#addStatementButton").queryButton()).isNotNull();
			assertThat(cont.getTransitionModel().getAddedStatementParent()).isEqualTo("root");
			assertThat(cont.getTransitionModel().getAddedStatementRank()).isEqualTo(1);
			
			robot.clickOn("#addCategoryButton");
			
			assertThat(robot.lookup("#minField").queryAs(TextField.class)).isNotNull();
		}
	}
	
}