package shared.model;

import com.google.gson.annotations.SerializedName;
import shared.model.resourcebank.ResourceList;

/**
 * TradeOffer is used during domestic trade operations:
 * it holds the sender index, receiver index, and ResourceList trading offer data.
 * Each Player can have either 0 or 1 trade offer active at any time, according to the JSON spec:
 * " + tradeOffer (TradeOffer, optional): The current trade offer, if there is one."
 *
 * Created by Sierra on 9/18/16.
 */
public class TradeOffer {

    @SerializedName("sender")
    private int senderIndex = 0;

    @SerializedName("receiver")
    private int receiverIndex = 0;

    @SerializedName("offer")
    private ResourceList tradeOfferList = new ResourceList();

    public TradeOffer(){}

    /**
     * Constructor for TradeOffer: receives individual int counts for each resource:
     *  Positive numbers are resources being offered. Negative are resources being asked for.
     *  This constructor is called when the user finishes selecting resources to trade
     *  and pushes the "Trade!" button on the modal.
     *  The controller can send this constructor each resource count being requested by the user.
     *  This constructor uses these counts to build its ResourceList object, tradeOfferList, to send to the server.
     *
     * @param senderIndex - the index of the player asking to trade
     * @param receiverIndex - the index of the player being offered a trade by the user
     * @param tradeOffer - the amount of resources requested/offered by the user; negative means requested; positive is offered
     */
    public TradeOffer(int senderIndex, int receiverIndex, ResourceList tradeOffer){
        this.senderIndex = senderIndex;
        this.receiverIndex = receiverIndex;
        tradeOfferList = tradeOffer;
    }


    /**
     * SendToServer() sends this tradeOffer data to the ServerProxy/translator to be eventually sent to the server.
     *
     * @pre  You have the resources you are giving
     * @pre For ratios less than 4, you have the correct port for the trade resource
     * @post The trade has been executed (the offered resources are in the bank,
     *       and the requested resource has been received)
     */
    public void sendToServer(){

    }

    public void updateTradeOffer(TradeOffer newTradeOffer) {
        this.tradeOfferList = newTradeOffer.tradeOfferList;
        senderIndex = newTradeOffer.senderIndex;
        receiverIndex = newTradeOffer.receiverIndex;
        //The controller needs to notice if this has changed for their player and change views
    }
    public int getSenderIndex() {
        return senderIndex;
    }

    public void setSenderIndex(int senderIndex) {
        this.senderIndex = senderIndex;
    }

    public int getReceiverIndex() {
        return receiverIndex;
    }

    public void setReceiverIndex(int receiverIndex) {
        this.receiverIndex = receiverIndex;
    }

    public ResourceList getTradeOfferList() {
        return tradeOfferList;
    }

    public void setTradeOfferList(ResourceList tradeOfferList) {
        this.tradeOfferList = tradeOfferList;
    }

    @Override
    public String toString() {
        return "TradeOffer{" +
                "senderIndex=" + senderIndex +
                ", receiverIndex=" + receiverIndex +
                ", tradeOfferList=" + tradeOfferList +
                '}';
    }

    /**
     * tra
     * @return
     */
    public boolean tradeToAccept(){
        if(receiverIndex != senderIndex){
            return true;
        }else {
            return false;
        }
    }


    /**
     * Determines if a player can accept a trade.
     * @param playerResources the player must have.
     * @return true if the player can accept.
     */
    public boolean canPlayerAccept(ResourceList playerResources){
        //resources required for player to send will be negative so as long as all counts >= 0 they can accept
        int woodTotal = tradeOfferList.getWoodCardCount() + playerResources.getWoodCardCount();
        int brickTotal = tradeOfferList.getBrickCardCount() + playerResources.getBrickCardCount();
        int sheepTotal = tradeOfferList.getSheepCardCount() + playerResources.getSheepCardCount();
        int wheatTotal = tradeOfferList.getWheatCardCount() + playerResources.getWheatCardCount();
        int oreTotal = tradeOfferList.getOreCardCount() + playerResources.getOreCardCount();

        if(woodTotal >= 0 && brickTotal >= 0 && sheepTotal >= 0 && wheatTotal >= 0 && oreTotal >= 0){
            return true;
        }else{
            return false;
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeOffer)) return false;

        TradeOffer that = (TradeOffer) o;

        if (senderIndex != that.senderIndex) return false;
        if (receiverIndex != that.receiverIndex) return false;
        return tradeOfferList != null ? tradeOfferList.equals(that.tradeOfferList) : that.tradeOfferList == null;

    }
}
