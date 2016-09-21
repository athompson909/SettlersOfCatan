package client.model;

import client.model.resourcebank.ResourceList;

/**
 * TradeOffer is used during domestic trade operations:
 * it holds the sender index, receiver index, and ResourceList trading offer data.
 * Each Player can have either 0 or 1 trade offer active at any time, according to the JSON spec:
 * " + tradeOffer (TradeOffer, optional): The current trade offer, if there is one."
 *
 * Created by Sierra on 9/18/16.
 */
public class TradeOffer {

    private int senderIndex = 0;
    private int receiverIndex = 0;
    private ResourceList tradeOfferList = new ResourceList();

    /**
     * Constructor for TradeOffer: receives individual int counts for each resource:
     *  Positive numbers are resources being offered. Negative are resources being asked for.
     *  This constructor is called when the user finishes selecting resources to trade
     *  and pushes the "Trade!" button on the modal.
     *  The controller can send this constructor each resource count being requested by the user.
     *  This constructor uses these counts to build its ResourceList object, tradeOfferList, to send to the server.
     *
     * @param receiverIndex - the index of the player being offered a trade by the user
     * @param numWoodRequested - the amount of wood requested/offered by the user
     * @param numBricksRequested - the amount of bricks requested/offered by the user
     * @param numSheepRequested - the amount of sheep requested/offered by the user
     * @param numWheatRequested - the amount of wheat requested/offered by the user
     * @param numOreRequested - the amount of ore requested/offered by the user
     */
    public void TradeOffer(int receiverIndex, int numWoodRequested, int numBricksRequested, int numSheepRequested,
                           int numWheatRequested, int numOreRequested){

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
}
