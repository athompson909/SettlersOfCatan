package client.maritime;

import client.ClientFacade;
import client.ClientUser;
import client.base.Controller;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.ClientModel;
import shared.model.commandmanager.moves.MaritimeTradeCommand;

import java.util.*;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private ClientModel clientModel;

	private IMaritimeTradeOverlay tradeOverlay;

	private ResourceType giveResource;

	private ResourceType getResource;

	private List<ResourceType> giveOptions;

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
		this.tradeOverlay = tradeOverlay;
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}

	@Override
	public void startTrade() {
		tradeOverlay.reset();
		//make sure trade button is disabled here
//		getTradeView().enableMaritimeTrade(false);
		tradeOverlay.showGiveOptions(getGiveOption());

		tradeOverlay.showModal();
	}

	private ResourceType[] getGiveOption() {
		giveOptions = new ArrayList<>();

		int currentPlayer = ClientUser.getInstance().getIndex();
		Set ports = clientModel.getMap().getPlayersPorts(currentPlayer);
		HashMap<PortType, boolean[]> isTradable = clientModel.getClientPlayer().canMaritimeTrade(ports);

		for(PortType p: isTradable.keySet()) {
			switch(p) {
				case WOOD: setTradeOption(isTradable, ResourceType.WOOD, p); break;
				case WHEAT: setTradeOption(isTradable, ResourceType.WHEAT, p); break;
				case BRICK: setTradeOption(isTradable, ResourceType.BRICK, p); break;
				case ORE: setTradeOption(isTradable, ResourceType.ORE, p); break;
				case SHEEP: setTradeOption(isTradable, ResourceType.SHEEP, p); break;
			}
		}

		return giveOptionsArr();
	}

	private ResourceType[] giveOptionsArr() {
		return giveOptions.toArray(new ResourceType[giveOptions.size()]);
	}

	private void setTradeOption(HashMap<PortType, boolean[]> isTradable, ResourceType resourceType, PortType p) {
		if(isTradable.get(p)[0]) {//2:1
			giveOptions.add(resourceType);
			tradeRates.put(resourceType, 2);
		}
		else if(isTradable.get(p)[1]) {//3:1
			giveOptions.add(resourceType);
			tradeRates.put(resourceType, 3);
		}
		else if(isTradable.get(p)[2]) {//4:1
			giveOptions.add(resourceType);
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
		unsetGetValue();
		tradeOverlay.hideGetOptions();
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
		//enable trade button here
//		getTradeView().enableMaritimeTrade(true);
	}

	@Override
	public void unsetGiveValue() {
		tradeOverlay.showGiveOptions(giveOptionsArr());
	}

	@Override
	public void unsetGetValue() {
		tradeOverlay.showGetOptions(getOptions);
	}

	@Override
	public void update(Observable o, Object arg) {
		clientModel = (ClientModel)o;
		int currentTurn = clientModel.getTurnTracker().getCurrentTurn();
		if(currentTurn == ClientUser.getInstance().getIndex()) {
			getTradeView().enableMaritimeTrade(getGiveOption().length > 0);
		}else{
			getTradeView().enableMaritimeTrade(false);
		}

	}
}

