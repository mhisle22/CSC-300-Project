package gui_FXML.AddNewStatement;

import gui_FXML.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mdjcBusinessPlanClasses.Statement;
import mdjcBusinessPlanClasses.TreeBuilder;

public class AddNewStatementController
{
    @FXML
    private TextField sNameField;

    @FXML
    private TextArea sField;
    
    @FXML
    private Button addStatementButton;
    
    @FXML
    private Label titleLabel;
    
    private ClientModel clientModel;
    
    private TransitionModel transitionModel;
    
    public void setClientModel(ClientModel model)
    {
    	this.clientModel=model;
    }
    
    public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    }

    @FXML
    void addStatementToPlan(ActionEvent event) 
    {
    	//Adds the inputed statement to the plan, with necessary PopUp errors
    	
		
		TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
		Statement parent= tree.findStatement(tree.getRoot().getChildren(), transitionModel.getAddedStatementParent());
		String category;
		if(parent == null)
		{
			category = clientModel.getClient().getLocalCopy().getPlan().getCategoryList().get(0).getName();
			parent = tree.getRoot();
		}
		else
		{
			category = clientModel.getClient().getLocalCopy().getPlan().getCategoryList().get(transitionModel.getAddedStatementRank() - 1).getName();
		}
		String name = sNameField.getText();
		String data = sField.getText();
		
		//If the user didn't input anything for data, warn the user
		if(data.trim().isEmpty())
		{
			transitionModel.setPopUpTitle("No Statement Input");
			transitionModel.setOKPopUpMessage("You must input a statement before you can add it");
			
		}
		//If field for name is empty, warn the user
		else if(name.trim().isEmpty())
		{
			transitionModel.setPopUpTitle("No Name Input");
			transitionModel.setOKPopUpMessage("You must give the new statement a unique name "
					+ "before you can add it");
			
			
		}
		else
		{
			//Check to see if statement of that name already exists as a child for this 
			//Parent statement
			if(tree.findStatement(tree.getRoot().getChildren(), name) == null)
			{
				boolean success = clientModel.getClient().getLocalCopy().addStatement(data, category, parent, name);
				//If the statement fails to be added, it's because the maximum amount of that category
				//of statement already exists in the business plan
				if(!success)
				{
					transitionModel.setPopUpTitle("Too Many Statements");
					transitionModel.setOKPopUpMessage("There are too many statements in the category " + category 
							+". Please remove a statement from category " 
							+ category + " before adding a new one");
				
				}
				else
				{
					//Add was successful, so plan should be saved soon
					transitionModel.recentlySavedProperty().setValue(false);
					transitionModel.setRedrawTree(true);
					sNameField.clear();
					sField.clear();
				}
			}
			//Warns the user a statement already exists with the given name
			else
			{
				transitionModel.setPopUpTitle("Name Already Exists");
				transitionModel.setOKPopUpMessage("A statement of category " + category 
						+ " already exists with the name " + name 
						+ ". Please choose a different unique name");
				
			}
		}
	}

}




