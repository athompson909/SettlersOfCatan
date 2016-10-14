package client.maritime;

import shared.definitions.*;
import shared.definitions.ResourceType.*;
import client.base.*;
import shared.model.ClientModel;
import shared.model.map.Port;
import java.util.Observable;
import shared.definitions.CatanColor;

import java.util.Set;
import java.util.HashMap;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	ClientModel clientModel;

	private IMaritimeTradeOverlay tradeOverlay;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		setTradeOverlay(tradeOverlay);
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {

		int currentPlayer = clientModel.getTurnTracker().getCurrentTurn();
		Set ports = clientModel.getMap().getPlayersPorts(currentPlayer);

		HashMap<PortType, boolean[]> isTradable = clientModel.getCurrentPlayer().canMaritimeTrade(ports);

		ResourceType[] giveOptions = new ResourceType[5];

		for(PortType p: isTradable.keySet()) {
			switch(p) {
				case WOOD:
					if((isTradable.get(p)[0] == true) || (isTradable.get(p)[1] == true) || (isTradable.get(p)[2] == true))
					{
						giveOptions[0] = ResourceType.WOOD;
					}
					break;
				case WHEAT:
					if((isTradable.get(p)[0] == true) || (isTradable.get(p)[1] == true) || (isTradable.get(p)[2] == true))
					{
						giveOptions[1] = ResourceType.WHEAT;
					}
					break;
				case BRICK:
					if((isTradable.get(p)[0] == true) || (isTradable.get(p)[1] == true) || (isTradable.get(p)[2] == true))
					{
						giveOptions[2] = ResourceType.BRICK;
					}
					break;
				case ORE:
					if((isTradable.get(p)[0] == true) || (isTradable.get(p)[1] == true) || (isTradable.get(p)[2] == true))
					{
						giveOptions[3] = ResourceType.ORE;
					}
					break;
				case SHEEP:
					if((isTradable.get(p)[0] == true) || (isTradable.get(p)[1] == true) || (isTradable.get(p)[2] == true))
					{
						giveOptions[4] = ResourceType.SHEEP;
					}
					break;
			}

		}

		//TODO: THE FOLLOWING LINES ARE HARD CODED AND WILL NEED TO BE DELETED

//		ResourceType[] giveOptions = new ResourceType[5];
//		giveOptions[0] = ResourceType.BRICK;
//		giveOptions[1] = ResourceType.WOOD;

		getTradeOverlay().showGiveOptions(giveOptions);
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {

	}

	@Override
	public void setGiveResource(ResourceType resource) {

	}

	@Override
	public void unsetGetValue() {

	}

	@Override
	public void unsetGiveValue() {

	}

	@Override
	public void update(Observable o, Object arg) {
		clientModel = (ClientModel)o;

	}
}

