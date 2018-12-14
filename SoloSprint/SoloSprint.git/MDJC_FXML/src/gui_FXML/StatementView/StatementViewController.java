package gui_FXML.StatementView;

import java.util.ArrayDeque;
import java.util.ArrayList;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.*;

public class StatementViewController
{

    @FXML
    private TreeView<String> statementTree;
    
    private TransitionModel transitionModel;
    private ClientModel clientModel;
    
    @FXML
    private ContextMenu contextMenu;

    @FXML
    MenuItem removeMenuItem;
    @FXML
    MenuItem addMenuItem;
    @FXML
    MenuItem comments;
    
	private Stage stage;
    
    
    public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    	//If the addedStatement property of the model becomes true. run method addTreeItem()
    	transitionModel.redrawTreeProperty().addListener((v, oldValue, newValue) -> 
    	{ if(newValue.booleanValue()){onLoadClient();}});
    }
    
    @FXML
    private void disableAdd()
    {
    	addMenuItem.setDisable(true);
    }
    
    @FXML
    private void enableAdd()
    {
    	addMenuItem.setDisable(false);
    }
    
    @FXML
    private void disableRemove()
    {
    	removeMenuItem.setDisable(true);
    }
    
    @FXML
    private void enableRemove()
    {
    	removeMenuItem.setDisable(false);
    }
    
    @FXML
    private void enableComments()
    {
    	comments.setDisable(false);
    }

    @FXML
    private void disableComments()
    {
    	comments.setDisable(true);
    }
    
	public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    	onLoadClient();
    }
    
    //Initializes the tree view from the current business plan
    private void onLoadClient()
    {
    	TreeBuilder tree = clientModel.getBusinessPlan().getTree();
    	TreeItem<String> businessPlanRoot = new TreeItem<String> ("Business Plan");
		
		//Sets up a queue of statements and treeItems to search through the plan tree and represent ALL
		//Statements while preserving hierarchy
		ArrayDeque<TreeItem<String>> treeItemQueue = new ArrayDeque<TreeItem<String>>();
		ArrayDeque<Statement> statementQueue = new ArrayDeque<Statement>();
		statementQueue.add(tree.getRoot());
		treeItemQueue.add(businessPlanRoot);
		
		
		//For every statement in the tree, make a treeItem to represent it and set it as a child 
		//To its parent Statement treeItem
		while(!statementQueue.isEmpty())
		{
			Statement currStatement = statementQueue.pop();
			TreeItem<String> currTreeItem = treeItemQueue.pop();
			for(int i = 0; i < currStatement.getChildren().size(); i++)
			{
				statementQueue.add(currStatement.getChildren().get(i));
				TreeItem<String> newTreeItem = new TreeItem<String>(currStatement.getChildren().get(i).getName());
				currTreeItem.getChildren().add(newTreeItem);
				treeItemQueue.add(newTreeItem);
			}
		}
		
		//Create the view for all treeItems
		statementTree.setRoot(businessPlanRoot);
		//Do not show the null root, start with the root's children
		statementTree.setShowRoot(true);
		//If an item in the view is selected, an editable StatementInfo window will appear in the Info window
		//NOTE: To deselect a treeItem, press CTRL + Left click on the item
		statementTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> 
																{if(newValue != null) 
																{
																	if(clientModel.getClient().isAdmin() || clientModel.getBusinessPlan().isEditable())
																	{enableAdd();
																	enableRemove();
																	enableComments();}
																	transitionModel.setSelectedTreeName("");
																	transitionModel.setAddUserView(false);
																	transitionModel.setRetrievePlan(false);
																	transitionModel.setSelectedTreeName(newValue.getValue());
																}else
																{disableAdd();disableRemove();disableComments();}});
		transitionModel.getStage().show();
		disableAdd();disableRemove();disableComments();
    }

    @FXML
	void removeStatementFromPlan(ActionEvent event)
	{
    	if(statementTree.getSelectionModel().getSelectedItem().getValue() != null)
    	{
			String value = statementTree.getSelectionModel().getSelectedItem().getValue();
			TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
			Statement statement = tree.findStatement(tree.getRoot().getChildren(), value);
			boolean success = clientModel.getClient().getLocalCopy().removeStatement(statement);
			//A removal will be unsuccessful if you try to remove a statement when the minimum amount of
			//statements has been reached
			if(!success)
			{
				transitionModel.setPopUpTitle("Too Few Statements For Removal");
				transitionModel.setOKPopUpMessage("There are too few statements in category " + statement.getType().getName() 
						+ " to remove this statement. Please add another statement before removing this one");
			}
			else
			{
				transitionModel.recentlySavedProperty().setValue(false);
				onLoadClient();
			}
    	}
	}
    
    @FXML
    void viewComments(ActionEvent event)
    {
    	if(!transitionModel.getSelectedTreeName().isEmpty())
    	{
    		//set up the view and the current statement being viewed
    		String value = statementTree.getSelectionModel().getSelectedItem().getValue();
    		//if its root node, stop before it all breaks
    		if(value.equals("Business Plan"))
    		{
    			return;
    		}
    		TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
    		Statement statement = tree.findStatement(tree.getRoot().getChildren(), value);
    		clientModel.setCurrStatement(statement);
    		transitionModel.setCommentsView(true);
    	}
    }
    
    @FXML
    void addChildToPlan(ActionEvent event)
    {
    	//Check that there is an item selected in the tree
    	if(!transitionModel.getSelectedTreeName().isEmpty())
    	{
	    	String value = transitionModel.getSelectedTreeName();
	    	//If the item is the tree root, rename value as "root"
	    	if(value.equals("Business Plan"))
	    	{
	    		value = "root";
	    	}
			TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
			Statement statement = tree.findStatement(tree.getRoot().getChildren(), value);
			//If a matching statement couldn't be found, the parent of the added statement is the root
			if(statement == null)
			{
				statement = tree.getRoot();
			}
			ArrayList<Category> categoryList = clientModel.getClient().getLocalCopy().getPlan().getCategoryList();
			int index = 0;
			//Search for index of the parent category
			for(int i = 0; i < categoryList.size(); i++)
			{
				if(value.equals("root"))
				{
					index = -1;
				}
				else if(categoryList.get(i).getName().equals(statement.getType().getName()))
				{
					index = i;
				}
			}
			int childIndex = index + 1;
			
			//If a category of higher rank than the parent category exists, 
			// continue to request the AddStatementView
			if(categoryList.size() != childIndex)
			{
				transitionModel.setAddedStatementParent(statement.getName());
				transitionModel.setAddedStatementRank(childIndex +1);
				transitionModel.setAddStatementView(true);
				transitionModel.setSelectedTreeName("");
			}
			//If a lower category doesn't exist, pop up and say so
			else
			{
				transitionModel.setPopUpTitle("No Lower Category Error");
				transitionModel.setOKPopUpMessage("Unable to add child as there is no lower category to this statement.");
			}
    	}
    }
    
    
	public void setStage(Stage stage)
	{
		this.stage = stage;	
	}
	
	//Sets statement tree root to expanded
	public void expandRoot()
	{
		statementTree.getRoot().setExpanded(true);
	}
	
	//Deselects any statement tree items
	public void deselectAllItems()
	{
		statementTree.getSelectionModel().clearSelection();
	}
	
	public TreeView<String> getTree()
	{
		return statementTree;
	}
}
