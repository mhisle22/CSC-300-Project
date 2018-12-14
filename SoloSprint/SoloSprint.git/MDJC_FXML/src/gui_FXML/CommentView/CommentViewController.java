package gui_FXML.CommentView;

import java.util.ArrayList;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CommentViewController
{
	@FXML
    private Button leaveButton;
    @FXML
    private Label commentLabel;
    @FXML
    private ScrollPane scrollPane;
    
    private ClientModel clientModel;
    
    private TransitionModel transitionModel;
    
    public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    }
    
    public void setTransitionModel(TransitionModel model)
    {
    	loadComments();
    	this.transitionModel = model;
    }
    
    @FXML
    void leaveComment(ActionEvent event) 
    {
    	clientModel.getCurrStatement().getComments().add("New Comment");
    	loadComments();
    }
    
    public void loadComments()
    {
    	commentLabel.setText(clientModel.getCurrStatement().getName());
    	//set up the view
		VBox vbox = new VBox();
		int idHelper = 0; //adds numbers to each name so they can be more easily identified
		ArrayList<String> comments = clientModel.getCurrStatement().getComments();
		for(String comment: comments)
		{
			HBox hbox = new HBox();
			//text field
			TextField oneComment = new TextField(comment);
			oneComment.setMinSize(338, 30);
			oneComment.setMaxSize(338, 30);
			oneComment.setId(comment + idHelper);
			oneComment.setAlignment(Pos.CENTER_LEFT);
			//save button
			Button save = new Button("Save");
			save.setMinSize(39,30);
			save.setOnAction(e -> {save(vbox, oneComment);});
			save.setId(comment + idHelper + "Save");
			//remove button
			Button remove = new Button("Remove");
			remove.setMinSize(39,30);
			remove.setOnAction(e -> {delete(comment);}); //button will retrieve the plan with its same id
			remove.setId(comment + idHelper + "Remove"); //needed for testing
			
			hbox.getChildren().addAll(oneComment, save, remove); 
			vbox.getChildren().add(hbox);
			hbox.setAlignment(Pos.CENTER);
			idHelper++;
		}
		scrollPane.setContent(vbox);
    }
    
    private void delete(String comment)
    {
    	clientModel.getCurrStatement().getComments().remove(comment);
    	loadComments();
    }
    
    private void save(VBox all, TextField id)
    {
    	//if this comment doesn't already exist
    	if(!clientModel.getCurrStatement().getComments().contains(id.getText()))
    	{
    		clientModel.getCurrStatement().getComments().add(id.getText());
    		
    		//delete default
    		if(clientModel.getCurrStatement().getComments().contains("New Comment"))
        	{
        		clientModel.getCurrStatement().getComments().remove("New Comment");
        	}
    	}
    	//remove last comment
    	//if the comment doesnt show up in one of the boxes, its the old one
    	for(String comment: clientModel.getCurrStatement().getComments())
    	{
    		boolean found = false;
    		for(Node node : all.getChildren())
    		{
    			HBox hbox = (HBox)node;
        		TextField field = (TextField)hbox.getChildren().get(0);
    			if(comment.equals(field.getText()))
    			{
    				found = true;
    			}
    		}
    		if(!found)
    		{
    			clientModel.getCurrStatement().getComments().remove(comment);
    		}
    	}
    	transitionModel.setCommentsView(false);

    }

	public Label getCommentLabel()
	{
		return commentLabel;
	}

	public void setCommentLabel(Label commentLabel)
	{
		this.commentLabel = commentLabel;
	}

	public ScrollPane getScrollPane()
	{
		return scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane)
	{
		this.scrollPane = scrollPane;
	}

	public ClientModel getClientModel()
	{
		return clientModel;
	}

	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}
    
}
