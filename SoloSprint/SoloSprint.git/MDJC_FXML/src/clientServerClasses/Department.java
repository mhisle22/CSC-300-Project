package clientServerClasses;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import mdjcBusinessPlanClasses.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Department implements Serializable
{
	String departmentName;
	
	@XmlElementWrapper(name = "planList")
	@XmlElement(name = "plans")
	ArrayList<BusinessPlan> plans;
	
	//departmentName == The name for this Department.
	public Department(String departmentName)
	{
		this.departmentName = departmentName;
		this.plans = new ArrayList<BusinessPlan>();
	}
	
	public Department() 
	{ 
	}

	//Finds and returns the BusinessPlan with id == bpid.
	//If no BusinessPlan is found to have an id that matches bpid, then null is returned.
	public BusinessPlan retrieve(String bpid)
	{
		BusinessPlan plan = null;
		//For each BusinessPlan in the list of plans
		for(int i =0; i < plans.size(); i++)
		{
			BusinessPlan currPlan = plans.get(i);
			//Return the plan if the plan's id == bpid
			if((currPlan.getId()).equals(bpid))
			{
				plan = currPlan;
				return plan;
			}
		}
		
		return plan;
	}
	
	//Searches through plans to find a BusinessPlan with an id equivalent to bp's id attribute.
	//If no matching BusinessPlan was found, then bp is added to the list of BusinessPlan.
	public void save(BusinessPlan bp, boolean isAdmin)
	{
		if(bp != null)
		{
			int index = 0;
			BusinessPlan currPlan = null;
			String neededId = bp.getId();
			
			//Look for a BusinessPlan with a matching id
			boolean foundMatchingBP = false;
			//While a BusinessPlan with id matching the given BusinessPlan hasn't been found
			while( (!foundMatchingBP) && index < plans.size())
			{
				currPlan = plans.get(index);
				String currPlanId = currPlan.getId();
				//If the current id matches the given id, we found the plan
				if(neededId.equals(currPlanId))
				{
					foundMatchingBP = true;
				}
				//Otherwise, continue checking other plans
				else
				{
					index++;
				}
			}
			
			//If a matching BusinessPlan was found, replace the old one. If not, add the new one to the list.
			if(foundMatchingBP)
			{
				//If the user is an admin or the BusinessPlan is editable, then we want to save the BusinessPlan.
				if(isAdmin || plans.get(index).isEditable())
				{
					plans.set(index, bp);
				}
			}
			//Otherwise, add the new BusinessPlan to the list of plans
			else
			{
				plans.add(bp);
			}
		}
	}
	
	//Iterates through every BusinessPlan in plans and saves their id, name, and year into a String[][], which is then returned.
	//The data is saved in the order specified above (i.e. String[0][1] is the name in plans.get(0)).
	//Here is a cheat sheet to reference when dealing with the String[][] that is returned:
	//
	//String[0][0] == id 
	//String[0][1] == name
	//String[0][2] == year
	
	public String[][] view()
	{
		String[][] data = new String[plans.size()][4];
		for(int index = 0; index < plans.size(); index++)
		{
			BusinessPlan currPlan = plans.get(index);
			
			data[index][0] = currPlan.getId();
			data[index][1] = currPlan.getName();
			data[index][2] = currPlan.getYear();
			data[index][3] = currPlan.getQuarter();
		}
		
		return data;
	}
	
	//Get a Business Plan via an index.
	BusinessPlan getBusinessPlan(int index)
	{
		return plans.get(index);
	}

	//Basic getters and setters.
	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public ArrayList<BusinessPlan> getPlans()
	{
		return plans;
	}
	
	public void setPlans(ArrayList<BusinessPlan> plans)
	{
		this.plans = plans;
	}

}
