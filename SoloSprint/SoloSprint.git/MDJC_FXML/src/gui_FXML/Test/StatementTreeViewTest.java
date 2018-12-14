package gui_FXML.Test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import static org.assertj.core.api.Assertions.assertThat;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

@ExtendWith(ApplicationExtension.class)
public class StatementTreeViewTest
{
	private MainWindowController cont;
	private StatementViewController treeCont;
	private Scene s;
	private InfoViewController infoCont;
	private static TransitionModel transModel;
	private static ClientModel model;
	
	@Start
	public void onStart(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
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
			transModel.setSelectedTreeName("");
			
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
	//If you select to add to the root or a child statement, the addStatementView appears
	void addStatementTest(FxRobot robot)
	{
		robot.clickOn("Business Plan");
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#addStatementButton").queryButton()).isNotNull();
		assertThat(cont.getTransitionModel().getAddedStatementParent()).isEqualTo("root");
		assertThat(cont.getTransitionModel().getAddedStatementRank()).isEqualTo(1);
		
		assertThat(robot.lookup("Value1")).isNotNull();
		robot.clickOn("Value1");
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#addStatementButton").queryButton()).isNotNull();
		assertThat(cont.getTransitionModel().getAddedStatementParent()).isEqualTo("Value1");
		assertThat(cont.getTransitionModel().getAddedStatementRank()).isEqualTo(2);
	}
	
	@Test
	//Tests that the context menu items are disabled if nothing is selected in the tree
	void disabledContextMenuTest(FxRobot robot)
	{
		infoCont.showEmptyLayout();
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#addNewStatementButton").queryButton()).isNull();
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#removeMenuItem");
		WaitForAsyncUtils.waitForFxEvents();
		
		assertThat(robot.lookup("Value1")).isNotNull();
		assertThat(robot.lookup("Business Plan")).isNotNull();	
		assertThat(robot.lookup("Value2")).isNotNull();
		assertThat(robot.lookup("Mission1")).isNotNull();
	}
	
	@Test
	//Tests that remove does work correctly
	void removeStatementTest(FxRobot robot)
	{
		robot.clickOn("Value2");
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#removeMenuItem");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(cont.getClient().getLocalCopy().getTree().findStatement(
				cont.getClient().getLocalCopy().getTree().getRoot().getChildren(), "Value2")).isNull();
	}
	
	@Test
	//Test that removing too many statements signals a popup and doesn't remove the statement
	void removeStatementPopupTest(FxRobot robot)
	{
		robot.clickOn("Mission1");
		
		robot.rightClickOn("#statementTree");
		robot.clickOn("#removeMenuItem");
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(cont.getClient().getLocalCopy().getTree().findStatement(
				cont.getClient().getLocalCopy().getTree().getRoot().getChildren(), "Mission1")).isNotNull();
		assertThat(transModel.getPopUpTitle()).isEqualTo("Too Few Statements For Removal");
		assertThat(transModel.getOKPopUpMessage()).isEqualTo("There are too few statements in category Mission" 
						+ " to remove this statement. Please add another statement before removing this one");
	}
	
	@Test
	//Test that the StatementInfo displays properly when a treeItem is clicked
	void clickOnStatementInfoTest(FxRobot robot)
	{
		robot.clickOn("Value1");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#statementName").queryAs(TextField.class).getText()).isEqualTo("Value1");
		assertThat(robot.lookup("#statementData").queryAs(TextArea.class).getText()).isEqualTo("I am a Value!");
		assertThat(robot.lookup("#categoryName").queryAs(Label.class).getText()).isEqualTo("Values");
		
		robot.clickOn("Value2");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#statementName").queryAs(TextField.class).getText()).isEqualTo("Value2");
		assertThat(robot.lookup("#statementData").
				queryAs(TextArea.class).getText()).isEqualTo("I am another Value!");
		assertThat(robot.lookup("#categoryName").queryAs(Label.class).getText()).isEqualTo("Values");
		
		robot.clickOn("Mission1");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#statementName").queryAs(TextField.class).getText()).isEqualTo("Mission1");
		assertThat(robot.lookup("#statementData").queryAs(TextArea.class).getText()).
		isEqualTo("I am the only Mission!");
		assertThat(robot.lookup("#categoryName").queryAs(Label.class).getText()).isEqualTo("Mission");
		
		robot.clickOn("Business Plan");
		
		WaitForAsyncUtils.waitForFxEvents();
		assertThat(robot.lookup("#emptyInfoText").queryAs(Text.class)).isNotNull();
	}
	
	@Test
	//Check that if you attempt to add a child to a statement with no child category, a popup is prompted
	void noLowerCategoryPopup(FxRobot robot)
	{
		robot.clickOn("Mission1");
		robot.rightClickOn("#statementTree");
		robot.clickOn("#addMenuItem");
		
		assertThat(transModel.getPopUpTitle()).isEqualTo("No Lower Category Error");
		assertThat(transModel.getOKPopUpMessage()).isEqualTo(
				"Unable to add child as there is no lower category to this statement.");
	}
}
