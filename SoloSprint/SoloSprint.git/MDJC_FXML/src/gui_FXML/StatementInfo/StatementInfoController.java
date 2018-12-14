package gui_FXML.StatementInfo;
import gui_FXML.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mdjcBusinessPlanClasses.Statement;
import mdjcBusinessPlanClasses.TreeBuilder;


public class StatementInfoController
{
	    @FXML
	    private TextField statementName;

	    @FXML
	    private TextArea statementData;

	    @FXML
	    private Label categoryName;

	    @FXML
	    private Button saveStatement;
	    
	    private ClientModel clientModel;
	    private TransitionModel transitionModel;
		
		public void setClientModel(ClientModel model)
		{
			this.clientModel = model;
		}
		
		public void setTransitionModel(TransitionModel model)
		{
			this.transitionModel = model;
			setStatementInfoLayout();
		}
		
		
		public void setStatementInfoLayout()
		{ 
			String value = transitionModel.getSelectedTreeName();
			//Finds statement object in tree with a given String name
			TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
			Statement statement = tree.findStatement(tree.getRoot().getChildren(), value);
			//If a statement was found (Should never return null)
			if(statement != null)
			{
				//Create an editable layout of the Statement's attributes
				statementName.setText(statement.getName());
				statementData.textProperty().set(statement.getData());
				categoryName.textProperty().set(statement.getType().getName());
				
				//Saves any edits to the displayed statement
				saveStatement.setText("Save Changes to " + statement.getName());	
			}
			
			if((!clientModel.getBusinessPlan().isEditable()) && (!clientModel.getClient().isAdmin()))
			{
				statementName.setDisable(true);
				statementData.setDisable(true);
				saveStatement.setDisable(true);
			}
			transitionModel.setCommentsView(false);
		}

		@FXML
		void saveThisStatement(ActionEvent event)
		//Changes the statement data in the tree to the new values
		{
			String value = transitionModel.getSelectedTreeName();
			TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
			Statement statement = tree.findStatement(tree.getRoot().getChildren(), value);
			statement.setName(statementName.getText());
			statement.setData(statementData.getText());
			
			transitionModel.recentlySavedProperty().setValue(false);
			transitionModel.setRedrawTree(true);

		}
}
