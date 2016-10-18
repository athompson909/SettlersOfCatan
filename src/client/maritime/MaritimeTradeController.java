package client.maritime;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.MaritimeTradeCommand;

import java.util.HashMap;
import java.util.Observable;
import java.util.Set;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private ClientModel clientModel;

	private IMaritimeTradeOverlay tradeOverlay;

	private ResourceType giveResource;

	private ResourceType getResource;

	private ResourceType[] giveOptions;

	private final ResourceType[] getOptions = {
			ResourceType.WOOD,
			ResourceType.WHEAT,
			ResourceType.BRICK,
			ResourceType.ORE,
			ResourceType.SHEEP
	};

	private HashMap<ResourceType, Integer> tradeRates = new HashMap<>();
	
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

		tradeOverlay.showGiveOptions(getGiveOption());

		tradeOverlay.showModal();
	}

	private ResourceType[] getGiveOption() {
		giveOptions = new ResourceType[5];

		int currentPlayer = clientModel.getTurnTracker().getCurrentTurn();
		Set ports = clientModel.getMap().getPlayersPorts(currentPlayer);
		HashMap<PortType, boolean[]> isTradable = clientModel.getCurrentPlayer().canMaritimeTrade(ports);

		for(PortType p: isTradable.keySet()) {
			switch(p) {
				case WOOD: setTradeOption(giveOptions, isTradable, ResourceType.WOOD, p); break;
				case WHEAT: setTradeOption(giveOptions, isTradable, ResourceType.WHEAT, p); break;
				case BRICK: setTradeOption(giveOptions, isTradable, ResourceType.BRICK, p); break;
				case ORE: setTradeOption(giveOptions, isTradable, ResourceType.ORE, p); break;
				case SHEEP: setTradeOption(giveOptions, isTradable, ResourceType.SHEEP, p); break;
			}
		}
		return giveOptions;
	}

	private void setTradeOption(ResourceType[] giveOptions, HashMap<PortType, boolean[]> isTradable,
								ResourceType resourceType, PortType p) {
		if(isTradable.get(p)[0]) {//2:1
			giveOptions[0] = resourceType;
			tradeRates.put(resourceType, 2);
		}
		else if(isTradable.get(p)[1]) {//3:1
			giveOptions[0] = resourceType;
			tradeRates.put(resourceType, 3);
		}
		else if(isTradable.get(p)[2]) {//4:1
			giveOptions[0] = resourceType;
			tradeRates.put(resourceType, 4);
		}

	}

	/**
	 * tells the server to make a trade (giveResource and getResource already exist)
	 */
	@Override
	public void makeTrade() {
		//create command
		MaritimeTradeCommand command = new MaritimeTradeCommand(ClientUser.getInstance().getIndex(),
				tradeRates.get(giveResource), giveResource, getResource);
		ClientFacade.getInstance().maritimeTrade(command);
		tradeOverlay.closeModal();
	}

	@Override
	public void cancelTrade() {

		tradeOverlay.closeModal();
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		giveResource = resource;
		tradeOverlay.selectGiveOption(resource, tradeRates.get(resource));
		tradeOverlay.showGetOptions(getOptions);
	}

	@Override
	public void setGetResource(ResourceType resource) {
		getResource = resource;
		tradeOverlay.selectGetOption(resource, 1);
	}

	@Override
	public void unsetGiveValue() {
		tradeOverlay.showGiveOptions(giveOptions);
	}

	@Override
	public void unsetGetValue() {
		tradeOverlay.showGetOptions(getOptions);
	}

	@Override
	public void update(Observable o, Object arg) {
		clientModel = (ClientModel)o;

	}
}

