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
public class MainWindowTest
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
	void ViewCheckTest(FxRobot robot)
	{

		// Checks StatementView
		assertThat(robot.lookup("#statementTree")).isNotNull();
		
		// Checks CategoryView
		assertThat(robot.lookup("#categoryTree")).isNotNull();
		
		// Checks InfoView
		assertThat(robot.lookup("#emptyInfoText")).isNotNull();
		
		// Checks PlanInfoView
		assertThat(robot.lookup("#exitButton")).isNotNull();
		
	}

}