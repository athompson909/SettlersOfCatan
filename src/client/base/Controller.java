package client.base;

import client.Client;
import javafx.beans.Observable;
import shared.model.ClientModel;

import java.util.Observer;

/**
 * Base class for controllers
 */
public abstract class Controller implements IController, Observer
{
	private IView view;
	
	protected Controller(IView view)
	{
		Client.getInstance().getClientModel().addObserver(this);
		setView(view);
	}
	
	private void setView(IView view)
	{
		this.view = view;
	}
	
	@Override
	public IView getView()
	{
		return this.view;
	}

}

