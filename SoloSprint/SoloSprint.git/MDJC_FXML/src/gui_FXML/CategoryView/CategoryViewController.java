package gui_FXML.CategoryView;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import mdjcBusinessPlanClasses.*;

public class CategoryViewController
{

    @FXML
    private TreeView<String> categoryTree;

    @FXML
    private Button addCategoryButton;
    
    @FXML
    MenuItem comments;
    
    private TransitionModel transitionModel;
    private ClientModel clientModel;

    public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    	
    	//Sets change listener to be alerted when a view asks for the tree to be redrawn
    	transitionModel.redrawTreeProperty().addListener((v, oldValue, newValue) -> {if(newValue.booleanValue()) {onLoadClient();}});
    }
    
    //When the client model is set, draw the category tree
    public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    	onLoadClient();
    }
    
    //Creates a hierarchy of categories in a TreeView object based on the current business plan
    @SuppressWarnings("unchecked")
	private void onLoadClient()
    {
    	PlanDesign plan = clientModel.getBusinessPlan().getPlan();
		TreeItem<String> dummyRoot = new TreeItem<String> ();
		categoryTree.setRoot(dummyRoot);
		categoryTree.setShowRoot(false);
		
		//Creates a tree view info layout for each Category, with the children being data (category rank,
		//minimum statements, maximum statements)
		for(int i = 0; i < plan.getCategoryList().size(); i++)
		{
			Category currCategory = plan.getCategoryList().get(i);
			String categoryLabel = currCategory.getName();
			TreeItem<String> categoryItem = new TreeItem<String>(categoryLabel);
			
			TreeItem<String> rankItem = new TreeItem<String>("Rank: " + currCategory.getRank());
			TreeItem<String> maxItem = new TreeItem<String>("Maximum Statements: " + currCategory.getMax());
			TreeItem<String> minItem = new TreeItem<String>("Minimum Statements: " + currCategory.getMin());
			
			categoryItem.getChildren().addAll(rankItem, maxItem, minItem);
			dummyRoot.getChildren().add(categoryItem);			
		}
		
		//If the user is not an admin AND the business plan is NOT editable, disable the add category button
		if(!clientModel.getBusinessPlan().isEditable() && !clientModel.getClient().isAdmin())
		{
			addCategoryButton.setDisable(true);
		}
		
		categoryTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> 
		{if(newValue != null) 
		{
			if(clientModel.getClient().isAdmin() || clientModel.getBusinessPlan().isEditable())
			{
				transitionModel.setSelectedTreeName(newValue.getValue());
				//transitionModel.setSelectedTreeName("");
				enableComments();
			}
			
		}
		else
		{
			disableComments();
		}
		});
		
		disableComments();
		categoryTree.refresh();
    }
    
    //displays comment view
    @FXML
    void showComments(ActionEvent event)
    {
    	if(!transitionModel.getSelectedTreeName().isEmpty())
    	{
    		//set up the view and the current statement being viewed
    		String value = categoryTree.getSelectionModel().getSelectedItem().getValue();
    		//if its root node, stop before it all breaks
    		if(value.equals("Business Plan"))
    		{
    			return;
    		}
    		TreeBuilder tree = clientModel.getClient().getLocalCopy().getTree();
    		Category category = tree.findCategory(value);
    		clientModel.setCurrStatement(category);
    		transitionModel.setCommentsView(true);
    	}
    }
    
    //If the add category button is clicked, reset necessary flags to other views as false and alert the 
    //InfoViewController to display the AddCategoryView
    @FXML
    void addCategoryViewRequest(ActionEvent event) 
    {
    	transitionModel.setSelectedTreeName("");
		transitionModel.setAddUserView(false);
		transitionModel.setRetrievePlan(false);
    	transitionModel.setAddCategoryView(true);
    	
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
}