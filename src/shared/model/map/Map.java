package shared.model.map;

import client.map.MapController;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.locations.*;
import shared.model.resourcebank.ResourceList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mitchell on 9/15/2016.
 * <p>
 * contains all data related to hexes, hex values, ports, objects on map, dimensions, and the robber
 */
public class Map {



    /**
     * List of all hexes used to create map
     */
    private HashMap<HexLocation, Hex> hexes = new HashMap<>();

    /**
     * Hashmap of all ports used to create map
     */
    private HashMap<HexLocation, Port> ports = new HashMap<>();

    /**
     * Hashmap of all the vertex objects on the map,
     * including unoccupied vertex objects (null), settlements, and cities.
     */
    private HashMap<VertexLocation, VertexObject> vertexObjects = new HashMap<>(); //These are the Settlements and Cities

    /**
     * Hashmap of all possible edgeValues
     * including unoccupied edges (null), and roads.
     */
    private HashMap<EdgeLocation, EdgeValue> edgeValues = new HashMap<>(); //These are the Roads

    /**
     * List of the static vertex locations
     */
    private HashMap<VertexLocation, Port> portVertexLocations = new HashMap<>();

    /**
     * A building manager for all road functionality.
     */
    public BuildRoadManager buildRoadManager = new BuildRoadManager(this);

    /**
     * A building manager for all settlement funcitonality.
     */
    public BuildSettlementManager buildSettlementManager = new BuildSettlementManager(this);

    /**
     * A building manager for all city functionality.
     */
    public BuildCityManager buildCityManager = new BuildCityManager(this);

    /**
     * Robber object
     */
    private Robber robber = new Robber(this);

    /**
     * Manages the checking and building of roads, settlements,
     * and cities (after being checked within Player class)
     */
    private static List<Integer> numberOrder = Arrays.asList(5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11);

    /**
     * An iterator to go through all the numbers assigned to the hexes when the map is initialized.
     */
    private static Iterator<Integer> numberIterator = numberOrder.iterator();

    /**
     * the radius of the map, according to the JSON
     */
    private int radius = 0;


    /**
     * the constructor for a Map object
     * is called when a user starts a new game and the map needs to be created (in initialization mode)
     */
    public Map(boolean randomlyPlaceNumbers, boolean randomlyPlaceHexes, boolean randomlyPlacePorts) {
        createAllWaterHexes();
        placeAllPorts(randomlyPlacePorts);
        createAllLandHexes(randomlyPlaceHexes, randomlyPlaceNumbers);
        createAllVertexObjects();
        populatePortVertexLocations();
        createAllEdgeValues();

    }

    //this constructor is for when the new model comes back and we need to make a new Map object
    // WITHOUT creating/placing all hexes again.
    public Map(HashMap<HexLocation, Hex> newHexes, HashMap<HexLocation, Port> newPorts,
               HashMap<VertexLocation, VertexObject> newVertexLocs, HashMap<EdgeLocation, EdgeValue> newEdgeValues) {
        setHexes(newHexes);
        setPorts(newPorts);
        setEdgeValues(newEdgeValues);
        setVertexObjects(newVertexLocs);
        //TODO: Look into this...
    }

    /**
     * Updates all map data members to match the newly updated model
     * <p>
     * Updates robber to new position
     * Updates list of EdgeValues where roads are stored
     * Updates list of VertexObjects where settlements are built
     * Updates list of VertexObjects where cities are built
     * <p>
     * Nothing else about the map changes during the game, so keep everything else the same!
     *
     * @param newMap updated map received from the updated clientModel
     */
    public void updateMap(Map newMap) {
        this.edgeValues = newMap.edgeValues; //Updates Roads
        this.robber = newMap.robber; //Updates Robber
        this.vertexObjects = newMap.vertexObjects; //Updates Settlements and Cities
        this.hexes = newMap.hexes;
        this.ports = newMap.ports;
    }

    /**
     * Creates 18 Water hexes at these specified locations starting with north-west corner going counterclockwise
     */
    private void createAllWaterHexes() {
        createWaterHex(-3, 0);
        createWaterHex(-3, 1);
        createWaterHex(-3, 2);
        createWaterHex(-3, 3);
        createWaterHex(-2, 3);
        createWaterHex(-1, 3);
        createWaterHex(0, 3);
        createWaterHex(1, 2);
        createWaterHex(2, 1);
        createWaterHex(3, 0);
        createWaterHex(3, -1);
        createWaterHex(3, -2);
        createWaterHex(3, -3);
        createWaterHex(2, -3);
        createWaterHex(1, -3);
        createWaterHex(0, -3);
        createWaterHex(-1, -2);
        createWaterHex(-2, -1);
    }

    /**
     * Generates a single water hex at the specified location.
     *
     * @param x coordinate on map.
     * @param y coordinate on map.
     */
    private void createWaterHex(int x, int y) {
        Hex waterHex = new Hex(new HexLocation(x, y), HexType.WATER);
        hexes.put(waterHex.getLocation(), waterHex);
    }

    /**
     * Places all the ports on the map.
     *
     * @param randomlyPlacePorts if the ports are being randomized.
     */
    private void placeAllPorts(boolean randomlyPlacePorts) {
        //Default Port Order
        List<PortType> resourcePortList = new LinkedList<PortType>(
                Arrays.asList(PortType.ORE, PortType.BRICK, PortType.WOOD, PortType.WHEAT, PortType.SHEEP));

        if (randomlyPlacePorts) {
            //Shuffle the port order
            long seed = System.nanoTime();
            Collections.shuffle(resourcePortList, new Random(seed));

            //Insert the 3:1 ports into the port list at every other location.
            resourcePortList.add(1, PortType.THREE);
            resourcePortList.add(3, PortType.THREE);
            resourcePortList.add(5, PortType.THREE);
            resourcePortList.add(7, PortType.THREE);

        } else {
            //Insert the 3:1 ports into the port list according to the default map layout.
            resourcePortList.add(2, PortType.THREE);
            resourcePortList.add(5, PortType.THREE);
            resourcePortList.add(7, PortType.THREE);
            resourcePortList.add(8, PortType.THREE);
        }

        //Place each port, starting with the northwest corner going counterclockwise.
        placePort(-3, 0, resourcePortList.get(0), EdgeDirection.SouthEast);
        placePort(-3, 2, resourcePortList.get(1), EdgeDirection.NorthEast);
        placePort(-2, 3, resourcePortList.get(2), EdgeDirection.NorthEast);
        placePort(0, 3, resourcePortList.get(3), EdgeDirection.North);
        placePort(2, 1, resourcePortList.get(4), EdgeDirection.NorthWest);
        placePort(3, -1, resourcePortList.get(5), EdgeDirection.NorthWest);
        placePort(3, -3, resourcePortList.get(6), EdgeDirection.SouthWest);
        placePort(1, -3, resourcePortList.get(7), EdgeDirection.South);
        placePort(-1, -2, resourcePortList.get(8), EdgeDirection.South);
    }

    /**
     * Places an individual port
     *
     * @param x             coordinate
     * @param y             coordinate
     * @param portType      to place.
     * @param edgeDirection of the port.
     */
    private void placePort(int x, int y, PortType portType, EdgeDirection edgeDirection) {
        HexLocation hexLocation = new HexLocation(x, y);
        Port newPort = new Port(portType, hexLocation, edgeDirection);
        ports.put(hexLocation, newPort);
    }

    /**
     * Creates all the land hexes, with their accompanying numbers.
     *
     * @param randomlyPlaceHexes   if the hexes are to be placed randomly.
     * @param randomlyPlaceNumbers if the numbers are to be placed randomly.
     */
    private void createAllLandHexes(boolean randomlyPlaceHexes, boolean randomlyPlaceNumbers) {
        //The default land hex order starting with the north-west corner and spiraling in counter-clockwise
        List<HexType> landHexTypeOrder = Arrays.asList(
                HexType.ORE, HexType.WHEAT, HexType.WOOD, HexType.ORE, HexType.WHEAT,
                HexType.SHEEP, HexType.WHEAT, HexType.SHEEP, HexType.WOOD, HexType.BRICK, HexType.DESERT,
                HexType.BRICK, HexType.SHEEP, HexType.SHEEP, HexType.WOOD, HexType.BRICK, HexType.ORE,
                HexType.WOOD, HexType.WHEAT);

        if (randomlyPlaceHexes) { //Shuffle the hex order
            long seed = System.nanoTime();
            Collections.shuffle(landHexTypeOrder, new Random(seed));
        }

        if (randomlyPlaceNumbers) { //shuffle the number order
            long seed = System.nanoTime();
            Collections.shuffle(numberOrder, new Random(seed));
        }
        numberIterator = numberOrder.iterator();

        //Create all 19 LandHexes at their specified coordinates
        createLandHex(-2, 0, landHexTypeOrder.get(0)); //North West Corner
        createLandHex(-2, 1, landHexTypeOrder.get(1));
        createLandHex(-2, 2, landHexTypeOrder.get(2)); //South West Corner
        createLandHex(-1, 2, landHexTypeOrder.get(3));
        createLandHex(0, 2, landHexTypeOrder.get(4)); //South Corner
        createLandHex(1, 1, landHexTypeOrder.get(5));
        createLandHex(2, 0, landHexTypeOrder.get(6)); //South East Corner
        createLandHex(2, -1, landHexTypeOrder.get(7));
        createLandHex(2, -2, landHexTypeOrder.get(8)); //North East Corner
        createLandHex(1, -2, landHexTypeOrder.get(9));
        createLandHex(0, -2, landHexTypeOrder.get(10)); //North Corner
        createLandHex(-1, -1, landHexTypeOrder.get(11));
        createLandHex(-1, 0, landHexTypeOrder.get(12));
        createLandHex(-1, 1, landHexTypeOrder.get(13));
        createLandHex(0, 1, landHexTypeOrder.get(14)); //Below center
        createLandHex(1, 0, landHexTypeOrder.get(15));
        createLandHex(1, -1, landHexTypeOrder.get(16));
        createLandHex(0, -1, landHexTypeOrder.get(17)); //Above Center
        createLandHex(0, 0, landHexTypeOrder.get(18)); //Center
    }

    /**
     * Creates a single land hex.
     *
     * @param x       coordinate.
     * @param y       y coordinate
     * @param hexType for the hex.
     */
    private void createLandHex(int x, int y, HexType hexType) {
        Hex landHex = new Hex(new HexLocation(x, y), hexType);
        if (hexType != HexType.DESERT) {
            landHex.setNumber(numberIterator.next().intValue());
        } else {
            robber.placeRobber(landHex.getLocation());
        }
        hexes.put(landHex.getLocation(), landHex);
    }

    /**
     * Creates all the vertex objects, starting with the west most column.
     * All vertex objects are created in terms of Northeast and Northwest directions of a hex.
     */
    private void createAllVertexObjects() {
        //First Column (West Most Column)
        createSingleVertexObject(-3, 1, VertexDirection.NorthEast);
        createSingleVertexObject(-3, 2, VertexDirection.NorthEast);
        createSingleVertexObject(-3, 3, VertexDirection.NorthEast);

        //Second Column
        createTwoVertexObject(-2, 0);
        createTwoVertexObject(-2, 1);
        createTwoVertexObject(-2, 2);
        createTwoVertexObject(-2, 3);

        //Third Column
        createTwoVertexObject(-1, -1);
        createTwoVertexObject(-1, 0);
        createTwoVertexObject(-1, 1);
        createTwoVertexObject(-1, 2);
        createTwoVertexObject(-1, 3);

        //Center Column
        createTwoVertexObject(0, -2);
        createTwoVertexObject(0, -1);
        createTwoVertexObject(0, 0);
        createTwoVertexObject(0, 1);
        createTwoVertexObject(0, 2);
        createTwoVertexObject(0, 3);

        //5th Column
        createTwoVertexObject(1, -2);
        createTwoVertexObject(1, -1);
        createTwoVertexObject(1, 0);
        createTwoVertexObject(1, 1);
        createTwoVertexObject(1, 2);

        //6th Column
        createTwoVertexObject(2, -2);
        createTwoVertexObject(2, -1);
        createTwoVertexObject(2, 0);
        createTwoVertexObject(2, 1);

        //East Most Column
        createSingleVertexObject(3, -2, VertexDirection.NorthWest);
        createSingleVertexObject(3, -1, VertexDirection.NorthWest);
        createSingleVertexObject(3, 0, VertexDirection.NorthWest);
    }

    /**
     * Creates two vertex objects on the Northwest and NorthEast Locations of the hex.
     *
     * @param x coordinate of the hex.
     * @param y coordinate of the hex.
     */
    private void createTwoVertexObject(int x, int y) {
        VertexLocation northWestVertextLocation = new VertexLocation(new HexLocation(x, y), VertexDirection.NorthWest);
        vertexObjects.put(northWestVertextLocation, new VertexObject(northWestVertextLocation));
        VertexLocation northEastVertextLocation = new VertexLocation(new HexLocation(x, y), VertexDirection.NorthEast);
        vertexObjects.put(northEastVertextLocation, new VertexObject(northEastVertextLocation));
    }

    /**
     * Creates a single vertex object on a hex, to be used for west and east columns of ocean hexes.
     *
     * @param x         coordinate of the hex.
     * @param y         coordinate of the hex.
     * @param direction of the hex, should be NorthEast or NorthWest.
     */
    private void createSingleVertexObject(int x, int y, VertexDirection direction) {
        VertexLocation newVertextLocation = new VertexLocation(new HexLocation(x, y), direction);
        vertexObjects.put(newVertextLocation, new VertexObject(newVertextLocation));
    }

    /**
     * Creates a map containing vertex locations and their associated ports
     * This list will not change throughout the game
     * <p>
     * Purpose: In maritimeTrade state, this enables us to learn what type of ports a player is associated with.
     */
    private void populatePortVertexLocations() {

        portVertexLocations.put(new VertexLocation(new HexLocation(-2, 0), VertexDirection.NorthWest), ports.get(new HexLocation(-3, 0)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-3, 1), VertexDirection.NorthEast), ports.get(new HexLocation(-3, 0)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-3, 2), VertexDirection.NorthEast), ports.get(new HexLocation(-3, 2)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-2, 2), VertexDirection.NorthWest), ports.get(new HexLocation(-3, 2)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-2, 3), VertexDirection.NorthEast), ports.get(new HexLocation(-2, 3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-1, 3), VertexDirection.NorthWest), ports.get(new HexLocation(-2, 3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(0, 3), VertexDirection.NorthWest), ports.get(new HexLocation(0, 3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(0, 3), VertexDirection.NorthEast), ports.get(new HexLocation(0, 3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(1, 2), VertexDirection.NorthEast), ports.get(new HexLocation(2, 1)));
        portVertexLocations.put(new VertexLocation(new HexLocation(2, 1), VertexDirection.NorthWest), ports.get(new HexLocation(2, 1)));
        portVertexLocations.put(new VertexLocation(new HexLocation(2, 0), VertexDirection.NorthEast), ports.get(new HexLocation(3, -1)));
        portVertexLocations.put(new VertexLocation(new HexLocation(3, -1), VertexDirection.NorthWest), ports.get(new HexLocation(3, -1)));
        portVertexLocations.put(new VertexLocation(new HexLocation(3, -2), VertexDirection.NorthWest), ports.get(new HexLocation(3, -3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(2, -2), VertexDirection.NorthEast), ports.get(new HexLocation(3, -3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(1, -2), VertexDirection.NorthEast), ports.get(new HexLocation(1, -3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(1, -2), VertexDirection.NorthWest), ports.get(new HexLocation(1, -3)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-1, -1), VertexDirection.NorthEast), ports.get(new HexLocation(-1, -2)));
        portVertexLocations.put(new VertexLocation(new HexLocation(-1, -1), VertexDirection.NorthWest), ports.get(new HexLocation(-1, -2)));
    }

    /**
     * Creates all of the edge values, starting with the west most values, going through each column.
     */
    private void createAllEdgeValues() {
        //First Column (West Most Column)
        createSingleEdgeValue(-3, 1, EdgeDirection.NorthEast);
        createSingleEdgeValue(-3, 2, EdgeDirection.NorthEast);
        createSingleEdgeValue(-3, 3, EdgeDirection.NorthEast);

        //Second Column
        createTripleEdgeValue(-2, 0);
        createTripleEdgeValue(-2, 1);
        createTripleEdgeValue(-2, 2);
        createNorthAndSpecifiedEdgeValue(-2, 3, EdgeDirection.NorthEast);

        //Third Column
        createTripleEdgeValue(-1, -1);
        createTripleEdgeValue(-1, 0);
        createTripleEdgeValue(-1, 1);
        createTripleEdgeValue(-1, 2);
        createNorthAndSpecifiedEdgeValue(-1, 3, EdgeDirection.NorthEast);

        //Center Column
        createTripleEdgeValue(0, -2);
        createTripleEdgeValue(0, -1);
        createTripleEdgeValue(0, 0);
        createTripleEdgeValue(0, 1);
        createTripleEdgeValue(0, 2);
        createSingleEdgeValue(0, 3, EdgeDirection.North);

        //5th Column
        createTripleEdgeValue(1, -2);
        createTripleEdgeValue(1, -1);
        createTripleEdgeValue(1, 0);
        createTripleEdgeValue(1, 1);
        createNorthAndSpecifiedEdgeValue(1, 2, EdgeDirection.NorthWest);

        //6th Column
        createTripleEdgeValue(2, -2);
        createTripleEdgeValue(2, -1);
        createTripleEdgeValue(2, 0);
        createNorthAndSpecifiedEdgeValue(2, 1, EdgeDirection.NorthWest);

        //East Most Column
        createSingleEdgeValue(3, -2, EdgeDirection.NorthWest);
        createSingleEdgeValue(3, -1, EdgeDirection.NorthWest);
        createSingleEdgeValue(3, 0, EdgeDirection.NorthWest);
    }

    /**
     * Creates a single edge value in the specified direction.
     *
     * @param x         coordinate of the hex.
     * @param y         coordinate of the hex.
     * @param direction of the edge value. Should only be northeast, northwest, or north.
     */
    private void createSingleEdgeValue(int x, int y, EdgeDirection direction) {
        EdgeLocation newEdgeLocation = new EdgeLocation(new HexLocation(x, y), direction);
        edgeValues.put(newEdgeLocation, new EdgeValue(newEdgeLocation));
    }

    /**
     * Creates a north edge value and an additional edge value in the specified direction.
     *
     * @param x         coordinate of the hex.
     * @param y         coordinate of the hex.
     * @param direction of the edge value to be created, in addition to the north value.
     */
    private void createNorthAndSpecifiedEdgeValue(int x, int y, EdgeDirection direction) {
        HexLocation currentHex = new HexLocation(x, y);

        EdgeLocation newEdgeLocation = new EdgeLocation(currentHex, direction);
        edgeValues.put(newEdgeLocation, new EdgeValue(newEdgeLocation));

        EdgeLocation northEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.North);
        edgeValues.put(northEdgeLocation, new EdgeValue(northEdgeLocation));
    }

    /**
     * Creates 3 edge values for North, Northwest, and NorthEast.
     * This function is only called for coordinates that correspond to land hexes.
     *
     * @param x coordinate of the hex.
     * @param y coordinate of the hex.
     */
    private void createTripleEdgeValue(int x, int y) {
        HexLocation currentHex = new HexLocation(x, y);

        EdgeLocation northEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.North);
        edgeValues.put(northEdgeLocation, new EdgeValue(northEdgeLocation));

        EdgeLocation northEastEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.NorthEast);
        edgeValues.put(northEastEdgeLocation, new EdgeValue(northEastEdgeLocation));

        EdgeLocation northWestEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.NorthWest);
        edgeValues.put(northWestEdgeLocation, new EdgeValue(northWestEdgeLocation));
    }

    public Set<PortType> getPlayersPorts(int playerIndex) {
        Set<PortType> ports = new HashSet<>();
        Set myKeys = portVertexLocations.keySet();
        for (Object vertLoc : myKeys) {
            if (vertexObjects.get(vertLoc).getOwner() == playerIndex) {
                PortType type = portVertexLocations.get(vertLoc).getResource();
                ports.add(type);
            }
        }
        return ports;
    }



/*
    //TODO: THIS IS NOT COMPLETE YET
    public void getDiceRollResults(int diceRollNumber) {
        for (HexLocation key : hexes.keySet()) {
            if (hexes.get(key).getNumber() == diceRollNumber) {
                getVertices(hexes.get(key));
            }
        }
    }

    //TODO: THIS IS NOT COMPLETE YET, to be called by getDiceRollResults
    private void getVertices(Hex hexWithNumber){
        ResourceList[] results = new ResourceList[4];
        HexLocation hexLocation = hexWithNumber.getLocation();




        VertexLocation northWestVertex = new VertexLocation(hexLocation, VertexDirection.NorthWest);
        VertexObject currentVertexObject = vertexObjects.get(northWestVertex);
        if(currentVertexObject.getOwner() != -1){
            if(currentVertexObject.getPieceType().equals(PieceType.SETTLEMENT)){




            } else { //It is a city
                results[currentVertexObject.getOwner()].incWoodCardCount(2);
            }
        }
        VertexLocation northEastVertex = new VertexLocation(hexLocation, VertexDirection.NorthEast);
        if(vertexObjects.get(northEastVertex).getOwner() != -1){

        }
        VertexLocation eastVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.SouthEast), VertexDirection.NorthWest);
        if(vertexObjects.get(eastVertex).getOwner() != -1){

        }

        VertexLocation westVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.SouthWest), VertexDirection.NorthEast);
        if(vertexObjects.get(westVertex).getOwner() != -1){

        }
        VertexLocation southEastVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthEast);
        if(vertexObjects.get(southEastVertex).getOwner() != -1){

        }
        VertexLocation southWestVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthWest);
        if(vertexObjects.get(southWestVertex).getOwner() != -1){

        }
    }

    private void addResources(){
        switch(getHexes().get(hexLocation).getResource()) {
            case WOOD:
                results[currentVertexObject.getOwner()].incWoodCardCount(1);
                break;
            case BRICK:
                results[currentVertexObject.getOwner()].incBrickCardCount(1);
                break;
            case WHEAT:
                results[currentVertexObject.getOwner()].incWheatCardCount(1);
                break;
            case SHEEP:
                results[currentVertexObject.getOwner()].incSheepCardCount(1);
                break;
            case ORE:
                results[currentVertexObject.getOwner()].incOreCardCount(1);
                break;
        }
    }
*/
    public boolean canPlaceRobber(HexLocation desiredHexLoc) {
        if (hexes.get(desiredHexLoc).getResource().equals(HexType.WATER) ||
                desiredHexLoc.equals(robber.getCurrentHexlocation())) {
            return false;
        } else{
            return true;
        }
    }

    public void placeRobber(HexLocation desiredHexLoc){
        robber.setCurrentHexlocation(desiredHexLoc);
    }


    //GETTERS
    public HashMap<VertexLocation, Port> getPortVertexLocations() {
        return portVertexLocations;
    }

    public HashMap<HexLocation, Hex> getHexes() {
        return hexes;
    }

    public HashMap<HexLocation, Port> getPorts() {
        return ports;
    }

    public void setPorts(HashMap<HexLocation, Port> ports) {
        this.ports = ports;
    }

    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
    }

    public HashMap<VertexLocation, VertexObject> getVertexObjects() {
        return vertexObjects;
    }

    public HashMap<EdgeLocation, EdgeValue> getEdgeObjects() {
        return edgeValues;
    }

    public int getRadius() {
        return radius;
    }

    //SETTERS

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setHexes(HashMap<HexLocation, Hex> hexes) {
        this.hexes = hexes;
    }

    public void setVertexObjects(HashMap<VertexLocation, VertexObject> vertexObjects) {
        this.vertexObjects = vertexObjects;
    }

    public void setEdgeValues(HashMap<EdgeLocation, EdgeValue> edgeValues) {
        this.edgeValues = edgeValues;
    }

    public void setPortVertexLocations(HashMap<VertexLocation, Port> portVertexLocations) {
        this.portVertexLocations = portVertexLocations;
    }
}
