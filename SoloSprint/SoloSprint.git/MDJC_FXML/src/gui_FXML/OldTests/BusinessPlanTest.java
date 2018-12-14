package gui_FXML.OldTests;
import mdjcBusinessPlanClasses.*;

public class BusinessPlanTest
{
	public BusinessPlanTest()
	{
	}

	public static void main(String[] args)
	{

		// BUSINESS PLAN 1//d
		BusinessPlan org = new BusinessPlan();
		TreeBuilder tree = org.getTree();
		org.addCategory("Vision", 1, 1, 1);
		org.addCategory("Mission", 2, 2, 4);
		org.addCategory("Objective", 3, 4, 8);
		org.setCategoryList();
		
		org.addStatement("Here is the text of our vision statement", "Vision", tree.getRoot(), "Vision Statement");
		Statement s1 = tree.findStatement(tree.getRoot().getChildren(), "Vision Statement");
		org.addStatement("First mission statement", "Mission", s1, "First Mission");
		org.addStatement("Second mission statement", "Mission", s1, "Second Mission");
		org.addStatement("Third mission statement", "Mission", s1, "Third Mission");
		org.addStatement("Fourth mission statement", "Mission", s1, "Fourth Mission");
		
		Statement s2 = tree.findStatement(tree.getRoot().getChildren(), "First Mission");
		Statement s3 = tree.findStatement(tree.getRoot().getChildren(), "Second Mission");
		Statement s4 = tree.findStatement(tree.getRoot().getChildren(), "Third Mission");
		Statement s5 = tree.findStatement(tree.getRoot().getChildren(), "Fourth Mission");


		assert s1.getData().equals("Here is the text of our vision statement");
		assert s1.getType().getName().equals("Vision");
		// checking that s2's parent is s1
		assert s2.getParent().equals(s1) : "s2's parent should assert to be s1.";
		assert s1.getChildren().contains(s2) : "s2 should be one of s1's children.";
		assert s1.getChildren().contains(s3) : "s3 should be one of s1's children.";
		
		org.addStatement("First objective statement", "Objective", s2, "First Objective");
		org.addStatement("Second objective statement", "Objective", s2, "Second Objective");
		org.addStatement("Third objective statement", "Objective", s3, "Third Objective");
		org.addStatement("Fourth objective statement", "Objective", s3, "Fourth Objective");
		org.addStatement("Fifth objective statement", "Objective", s4, "Fifith Objective");
		org.addStatement("Sixth objective statement", "Objective", s4, "Sixth Objective");
		org.addStatement("Seventh objective statement", "Objective", s5, "Seventh Objective");
		org.addStatement("Eighth objective statement", "Objective", s5, "Eighth Objective");
		
		Statement s6 = tree.findStatement(tree.getRoot().getChildren(), "First Objective");
		Statement s7 = tree.findStatement(tree.getRoot().getChildren(), "Second Objective");
		Statement s8 = tree.findStatement(tree.getRoot().getChildren(), "Third Objective");
		Statement s9 = tree.findStatement(tree.getRoot().getChildren(), "Fourth Objective");
		Statement s10 = tree.findStatement(tree.getRoot().getChildren(), "Fifth Objective");
		Statement s11 = tree.findStatement(tree.getRoot().getChildren(), "Sixth Objective");
		Statement s12 = tree.findStatement(tree.getRoot().getChildren(), "Seventh Objective");
		Statement s13 = tree.findStatement(tree.getRoot().getChildren(), "Eighth Objective");


		assert s6.getData().equals("First objective statement");
		assert s6.getType().getName().equals("Objective");
		assert s6.getParent().equals(s2);
		assert s2.getChildren().contains(s6);

		// Attempting to add a statement to a full category. Should print 3 error
		// messages.
		
		org.addStatement("Ninth objective statement", "Objective", s5, "Ninth Objective");
		org.addStatement("Fifth mission statement", "Mission", s1, "Fifth Mission");
		org.addStatement("Second vision statement", "Vision", org.getTree().getRoot(), "Second Vision");
		
		Statement s14 = tree.findStatement(tree.getRoot().getChildren(), "Ninth Objective");
		Statement s15 = tree.findStatement(tree.getRoot().getChildren(), "Fifth Mission");
		Statement s16 = tree.findStatement(tree.getRoot().getChildren(), "Second Vision");
		

		// Attempting to remove a statement to a category at/below its min # statements.
		// 2 error messages.
		org.removeStatement(s1); // first error after this line
		org.removeStatement(s2);
		org.removeStatement(s3);
		org.removeStatement(s4); // second error after this line

		// Removing statements
		org.removeStatement(s13);
		org.removeStatement(s12);
		assert s13.getData() == null;
		assert s13.getParent() == null;
		assert s13.getChildren() == null;
		assert s12.getData() == null;
		assert s12.getParent() == null;
		assert s12.getChildren() == null;

		// Testing if statements down the tree from removed statements are also removed

		org.removeStatement(s2);
		assert s6.getParent() == null;
		assert s6.getChildren() == null;
		assert s6.getData() == null;

		// Editing statement data
		org.editStatement(s1, "NEW MISSION STATEMENT!!!");
		assert s1.getData().equals("NEW MISSION STATEMENT!!!");
		// If successful, will print a successfully stored message
		org.addToHistory();
		// If successful, will print a successfully loaded message
		//org.loadPlanFromFile();
		
		
	}

}
