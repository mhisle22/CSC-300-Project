package mdjcBusinessPlanClasses;

import java.util.ArrayList;

public interface Commentable
{
	void setComments(ArrayList<String> comments);
	ArrayList<String> getComments();
	String getName();
}
