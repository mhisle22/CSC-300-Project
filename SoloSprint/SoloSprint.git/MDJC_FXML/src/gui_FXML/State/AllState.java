package gui_FXML.State;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AllState extends State
{
	
	public AllState()
	{
		this.name = "All";
	}
	
	@Override
	public VBox buttonHelper(boolean isClone)
	{
		//adds buttons representing plans to a VBox, and returns the VBox
		ArrayList<Button> buttons = new ArrayList<Button>();
		VBox vbox = new VBox();
		String[][] plans = model.getClient().view();
		for(String[] plan : plans)
		{
			String id = plan[0];
			String name = plan[1];
			String year = plan[2];
			Button button = new Button(id + ": " + name + ", " + year + ", " + plan[3]);
			button.setMinWidth(285);
			button.setMaxWidth(285);
			buttons.add(button);
			button.setOnAction(e -> {retrievePlan(id, isClone);});
			button.setId(id); //needed for testing 
			vbox.getChildren().add(button);
			button.setAlignment(Pos.CENTER);
		}
		return vbox;
	}
	

}
