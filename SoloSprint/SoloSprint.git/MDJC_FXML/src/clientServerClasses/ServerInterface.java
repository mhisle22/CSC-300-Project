package clientServerClasses;
import java.rmi.*;

import javafx.beans.property.BooleanProperty;
import mdjcBusinessPlanClasses.BusinessPlan;

public interface ServerInterface extends Remote {

	String authenticate(String username, String password) throws RemoteException;
	String[][] view(String userToken) throws RemoteException;
	BusinessPlan retrieve(String userToken, String bpid) throws RemoteException;
	void save(String userToken, BusinessPlan bpid) throws RemoteException;
	boolean addUser(String userToken, String username, String password, String departmentName, boolean isAdmin) throws RemoteException;
	boolean getClientPrivilege(String userToken) throws RemoteException;
}
