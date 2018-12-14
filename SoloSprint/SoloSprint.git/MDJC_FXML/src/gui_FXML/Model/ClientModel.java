package gui_FXML.Model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientServerClasses.Client;
import clientServerClasses.ServerInterface;
import gui_FXML.State.AllState;
import gui_FXML.State.FirstState;
import gui_FXML.State.FourthState;
import gui_FXML.State.SecondState;
import gui_FXML.State.State;
import gui_FXML.State.ThirdState;
import gui_FXML.State.YearlyState;
import mdjcBusinessPlanClasses.BusinessPlan;
import mdjcBusinessPlanClasses.Commentable;

public class ClientModel
{

	private final String localhost = "127.0.0.1";
	// Reference to client
	Client client;
	//Used solely to alert the popup window
	TransitionModel model;
	Commentable currStatement;
	State currentState;
	//will hold the states. Yeah.
	public State[] states = new State[6];
	
	
	public ClientModel(TransitionModel model)
	{
		this.model = model;
		states[0] = new YearlyState();
		states[1] = new FirstState();
		states[2] = new SecondState();
		states[3] = new ThirdState();
		states[4] = new FourthState();
		states[5] = new AllState();
	}

	// Called by Server Login to connect new client to requested server
	public boolean getNewClient(String hostInput)
	{
		String host;
		if (hostInput.trim().isEmpty() || hostInput.trim().equals("localhost"))
		{
			host = localhost;
		} else
		{
			host = hostInput.trim();
		}
		try
		{
			Registry registry = LocateRegistry.getRegistry(host);
			System.setProperty("java.rmi.server.hostname", host);

			// Get server from the registry and give it to client.
			ServerInterface stub = (ServerInterface) registry.lookup("Server");
			client = new Client(stub);
			client.setHost(host);
			return true;
		}

		catch (RemoteException e )
		{
			model.setPopUpTitle("Connection Error");
			model.setOKPopUpMessage("Unable to connect to server. \nPlease check your connection or try again later.");
			

			return false;

		} 
		catch (NotBoundException e)
		{
			model.setPopUpTitle("Server Does Not Exist");
			model.setOKPopUpMessage("Unable to connect to server, server does not exist at this host address.");
			
			return false;
		}
	}

	public BusinessPlan getBusinessPlan()
	{
		return client.getLocalCopy();
	}

	public Client getClient()
	{
		return client;
	}

	public void setTransitionModel(TransitionModel transModel)
	{
		this.model = transModel;
	}

	public Commentable getCurrStatement()
	{
		return currStatement;
	}

	public void setCurrStatement(Commentable currStatement)
	{
		this.currStatement = currStatement;
	}

	public State getCurrentState()
	{
		return currentState;
	}

	public void setCurrentState(State currentState)
	{
		this.currentState = currentState;
	}
	
}
