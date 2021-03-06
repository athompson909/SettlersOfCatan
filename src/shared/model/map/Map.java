package shared.model.map;

import client.data.RobPlayerInfo;
import shared.definitions.*;
import shared.locations.*;
import shared.model.resourcebank.ResourceList;

import java.lang.reflect.Array;
import java.util.*;

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
     * Hashmap of all the vertex objects on the map, including settlements and cities.
     */
    private HashMap<VertexLocation, VertexObject> vertexObjects = new HashMap<>(); //These are the Settlements and Cities

    /**
     * Hashmap of all the valid vertex locations for placing a settlement.
     * Hence, this list will not include vertices in the ocean.
     */
    private HashMap<VertexLocation, VertexLocation> allValidVertexLocations = new HashMap<>();

    /**
     * Hashmap of all possible edgeValues which are roads that have been built.
     */
    private HashMap<EdgeLocation, EdgeValue> edgeValues = new HashMap<>(); //These are the Roads

    /**
     * Hashmap of all the valid edge locations for building roads.
     * Hence, this list will not include edge locations in the ocean.
     */
    private HashMap<EdgeLocation, EdgeLocation> allValidEdgeLocations = new HashMap<>();

    /**
     * List of the static vertex locations
     */
    private HashMap<VertexLocation, Port> portVertexLocations = new HashMap<>();

    /**
     * A building manager for all road functionality.
     */
    public transient BuildRoadManager buildRoadManager = new BuildRoadManager(this);

    /**
     * A building manager for all settlement funcitonality.
     */
    public transient BuildSettlementManager buildSettlementManager = new BuildSettlementManager(this);

    /**
     * A building manager for all city functionality.
     */
    public transient BuildCityManager buildCityManager = new BuildCityManager(this);

    /**
     * Robber object
     */
    private Robber robber = new Robber(this);

    /**
     * Manages the checking and building of roads, settlements,
     * and cities (after being checked within Player class)
     */
    private List<Integer> numberOrder = Arrays.asList(5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11);

    /**
     * An iterator to go through all the numbers assigned to the hexes when the map is initialized.
     */
    private Iterator<Integer> numberIterator = numberOrder.iterator();

    /**
     * the radius of the map, according to the JSON
     */
    private int radius = 0;

    /**
     * the constructor for a Map object
     * is called when a user starts a new game and the map needs to be created (in initialization model)
     */
    public Map(boolean randomlyPlaceHexes, boolean randomlyPlaceNumbers, boolean randomlyPlacePorts) {
        //System.out.println("MAP RANDOM BACON: " + randomlyPlaceHexes + " " + randomlyPlaceNumbers + " " + randomlyPlacePorts);
        //createAllWaterHexes();
        createAllLandHexes(randomlyPlaceHexes, randomlyPlaceNumbers);
        createValidEdgeLocations();
        createValidVertexLocations();
        placeAllPorts(randomlyPlacePorts);
        populatePortVertexLocations();
    }

    /**
     * This is the constructor used to update the model.
     *
     * @param newHexes
     * @param newPorts
     * @param newVertexLocs
     * @param newEdgeValues
     * @param newRobberLocation
     */
    public Map(HashMap<HexLocation, Hex> newHexes, HashMap<HexLocation, Port> newPorts,
               HashMap<VertexLocation, VertexObject> newVertexLocs, HashMap<EdgeLocation, EdgeValue> newEdgeValues, HexLocation newRobberLocation) {
        setHexes(newHexes);
        setPorts(newPorts);
        setEdgeValues(newEdgeValues);
        setVertexObjects(newVertexLocs);
        placeRobber(newRobberLocation);
                //populatePortVertexLocations();     //????

        createValidEdgeLocations();
        createValidVertexLocations();
    }

    /**
     * Updates all map data members to match the newly updated model
     * Updates robber to new position
     * Updates list of EdgeValues where roads are stored
     * Updates list of VertexObjects where settlements are built
     * Updates list of VertexObjects where cities are built
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
    /*private void createAllWaterHexes() {
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
    }*/

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
    private void createValidVertexLocations() {
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
        allValidVertexLocations.put(northWestVertextLocation, northWestVertextLocation);
        //vertexObjects.put(northWestVertextLocation, new VertexObject(northWestVertextLocation));
        VertexLocation northEastVertextLocation = new VertexLocation(new HexLocation(x, y), VertexDirection.NorthEast);
        allValidVertexLocations.put(northEastVertextLocation, northEastVertextLocation);
        //vertexObjects.put(northEastVertextLocation, new VertexObject(northEastVertextLocation));
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
        allValidVertexLocations.put(newVertextLocation, newVertextLocation);
        // vertexObjects.put(newVertextLocation, new VertexObject(newVertextLocation));
    }

    /**
     * Creates a map containing vertex locations and their associated ports
     * This list will not change throughout the game
     * **note: this will be changed when Client.startGame == true
     * <p>
     * Purpose: In maritimeTrade state, this enables us to learn what type of ports a player is associated with.
     */
    public void populatePortVertexLocations() {

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
    private void createValidEdgeLocations() {
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
        allValidEdgeLocations.put(newEdgeLocation, newEdgeLocation);
        //edgeValues.put(newEdgeLocation, new EdgeValue(newEdgeLocation));
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
        allValidEdgeLocations.put(newEdgeLocation, newEdgeLocation);
        //edgeValues.put(newEdgeLocation, new EdgeValue(newEdgeLocation));

        EdgeLocation northEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.North);
        allValidEdgeLocations.put(northEdgeLocation, northEdgeLocation);
        //edgeValues.put(northEdgeLocation, new EdgeValue(northEdgeLocation));
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
        allValidEdgeLocations.put(northEdgeLocation, northEdgeLocation);
        //edgeValues.put(northEdgeLocation, new EdgeValue(northEdgeLocation));

        EdgeLocation northEastEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.NorthEast);
        allValidEdgeLocations.put(northEastEdgeLocation, northEastEdgeLocation);
        //edgeValues.put(northEastEdgeLocation, new EdgeValue(northEastEdgeLocation));

        EdgeLocation northWestEdgeLocation = new EdgeLocation(currentHex, EdgeDirection.NorthWest);
        allValidEdgeLocations.put(northWestEdgeLocation, northWestEdgeLocation);
        //edgeValues.put(northWestEdgeLocation, new EdgeValue(northWestEdgeLocation));
    }

    //todo: fix this function
    public Set<PortType> getPlayersPorts(int playerIndex) {
        Set<PortType> ports = new HashSet<>();
        Set myKeys = portVertexLocations.keySet();
        for (Object vertLoc : myKeys) {
            if (vertexObjects.containsKey(vertLoc)) {
                if (vertexObjects.get(vertLoc).getOwner() == playerIndex) {
                    PortType type = portVertexLocations.get(vertLoc).getResource();
                    ports.add(type);
                }
            }
        }
        return ports;
    }

    /**
     * @param hexLoc The hex to evaluate
     * @return an arraylist of all the players that have a settlement or city on a hexes vertex
     */
    public ArrayList getPlayersAdjacentToHex(HexLocation hexLoc) {
        ArrayList<Integer> adjacentPlayers = new ArrayList();

        VertexLocation northWestVertex = new VertexLocation(hexLoc, VertexDirection.NorthWest);
        addPlayeratVertex(northWestVertex, adjacentPlayers);

        VertexLocation northEastVertex = new VertexLocation(hexLoc, VertexDirection.NorthEast);
        addPlayeratVertex(northEastVertex, adjacentPlayers);

        VertexLocation eastVertex = new VertexLocation(hexLoc.getNeighborLoc(EdgeDirection.SouthEast), VertexDirection.NorthWest);
        addPlayeratVertex(eastVertex, adjacentPlayers);

        VertexLocation westVertex = new VertexLocation(hexLoc.getNeighborLoc(EdgeDirection.SouthWest), VertexDirection.NorthEast);
        addPlayeratVertex(westVertex, adjacentPlayers);

        VertexLocation southEastVertex = new VertexLocation(hexLoc.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthEast);
        addPlayeratVertex(southEastVertex, adjacentPlayers);

        VertexLocation southWestVertex = new VertexLocation(hexLoc.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthWest);
        addPlayeratVertex(southWestVertex, adjacentPlayers);

        return adjacentPlayers;
    }

    /**
     * Checks if a player is at the specificed vertex Location, and adds that player's index
     * to an array list if the player is not already in that array list.
     *
     * @param vertexLocation
     * @param adjacentPlayers
     */
    private void addPlayeratVertex(VertexLocation vertexLocation, ArrayList adjacentPlayers) {
        if (vertexObjects.containsKey(vertexLocation)) {
            int ownerIndex = vertexObjects.get(vertexLocation).getOwner();
            if (ownerIndex != -1) {
                if (!adjacentPlayers.contains(ownerIndex)) {
                    adjacentPlayers.add(ownerIndex);
                }
            }
        }
    }


    /**
     * Calculates the resources associated with a dice roll
     * @param diceRollNumber that is rolled
     * @return a ResourceList[] of size 4, the indexes correspond with the player indexes.
     */
    public ResourceList[] getDiceRollResults(int diceRollNumber) {
        ResourceList[] results = new ResourceList[4];
        for(int i = 0; i < results.length; i ++) {
            results[i] = new ResourceList();
        }
        for (HexLocation key : hexes.keySet()) {
            if (hexes.get(key).getNumber() == diceRollNumber &&
                    !robber.getCurrentHexlocation().equals(hexes.get(key).getLocation())) {
                getCardsFromVertices(hexes.get(key), results);
            }
        }
        return results;
    }

    /**
     * Gets the cards from the vertices
     * @param hexWithNumber passes in a hex that matches the dice roll number.
     * @param results resourcelist with the cards the player will recieve.
     */
    private void getCardsFromVertices(Hex hexWithNumber, ResourceList[] results) {
        HexLocation hexLocation = hexWithNumber.getLocation();
        ResourceType resource = determineResource(hexWithNumber.getResource());

        VertexLocation northWestVertex = new VertexLocation(hexLocation, VertexDirection.NorthWest);
        addCards(resource, vertexObjects.get(northWestVertex), results);

        VertexLocation northEastVertex = new VertexLocation(hexLocation, VertexDirection.NorthEast);
        addCards(resource, vertexObjects.get(northEastVertex), results);

        VertexLocation eastVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.SouthEast), VertexDirection.NorthWest);
        addCards(resource, vertexObjects.get(eastVertex), results);

        VertexLocation westVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.SouthWest), VertexDirection.NorthEast);
        addCards(resource, vertexObjects.get(westVertex), results);

        VertexLocation southEastVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthEast);
        addCards(resource, vertexObjects.get(southEastVertex), results);

        VertexLocation southWestVertex = new VertexLocation(hexLocation.getNeighborLoc(EdgeDirection.South), VertexDirection.NorthWest);
        addCards(resource, vertexObjects.get(southWestVertex), results);
    }

    /**
     * Adds cards to the result array.
     * @param resource to be added.
     * @param vertexObject being considered
     * @param results array to add cards too.
     */
    private void addCards(ResourceType resource, VertexObject vertexObject, ResourceList[] results) {
        if(vertexObject != null) {
            if (vertexObject.getOwner() != -1) {
                results[vertexObject.getOwner()].addCardByType(resource);
                if (vertexObject.getPieceType() == PieceType.CITY) {
                    results[vertexObject.getOwner()].addCardByType(resource);
                }
            }
        }
    }

    /**
     * Converts a hextype resource to a ResourceType resource.
     * @param hexType being considered.
     * @return the associated ResourceType.
     */
    private ResourceType determineResource(HexType hexType) {
        switch (hexType) {
            case WOOD:
                return ResourceType.WOOD;
            case BRICK:
                return ResourceType.BRICK;
            case SHEEP:
                return ResourceType.SHEEP;
            case WHEAT:
                return ResourceType.WHEAT;
            case ORE:
                return ResourceType.ORE;
            default:
                return null;
        }
    }

    /**
     * Calculates what resources the player should recieve from placing their second settlement.
     * @param secondSettlementLocation of where the settlement is being placed.
     * @return a resource list with their new cards.
     */
    public ResourceList calculateSecondSettlementResources(VertexLocation secondSettlementLocation){
        ResourceList secondSettlementResources = new ResourceList();

        //South Hex of the vertex (the one it belongs too).
        HexLocation southHexLocation = secondSettlementLocation.getHexLoc();
        addResourceByHexType(secondSettlementResources, southHexLocation);

        //North hex of the vertex.
        HexLocation northHexLocation = secondSettlementLocation.getHexLoc().getNeighborLoc(EdgeDirection.North);
        addResourceByHexType(secondSettlementResources, northHexLocation);

        //The northEast or northWest hex of the vertex.
        if(secondSettlementLocation.getDir() == VertexDirection.NorthEast){
            HexLocation northEastHexLocation = secondSettlementLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast);
            addResourceByHexType(secondSettlementResources, northEastHexLocation);
        } else{
            HexLocation northWestHexLocation = secondSettlementLocation.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest);
            addResourceByHexType(secondSettlementResources, northWestHexLocation);
        }
        return secondSettlementResources;
    }

    /**
     * Takens in a hexLocation, and adds the resource type of that hexLocation to the specified resourceList.
     * @param secondSettlementResources
     * @param hexLocation
     */
    private void addResourceByHexType(ResourceList secondSettlementResources, HexLocation hexLocation){
        if(this.getHexes().containsKey(hexLocation)) {
            HexType hexType = this.getHexes().get(hexLocation).getResource();
            if (!hexType.equals(HexType.DESERT) && !hexType.equals(HexType.WATER)) {
                ResourceType resourceCard = determineResource(hexType);
                secondSettlementResources.addCardByType(resourceCard);
            }
        }
    }


    /**
     * Checks to see if the robber can be placed on a desired hex.
     *
     * @param desiredHexLoc for the robber to be placed.
     * @return
     */
    public boolean canPlaceRobber(HexLocation desiredHexLoc) {
        if (hexes.containsKey(desiredHexLoc)) {
            if (!desiredHexLoc.equals(robber.getCurrentHexlocation())) {
                return true;
            }
        }
        return false;
    }

    public void placeRobber(HexLocation desiredHexLoc) {
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

    public HashMap<EdgeLocation, EdgeValue> getEdgeValues() {
        return edgeValues;
    }

    public int getRadius() {
        return radius;
    }

    public HashMap<VertexLocation, VertexLocation> getAllValidVertexLocations() {
        return allValidVertexLocations;
    }

    public HashMap<EdgeLocation, EdgeLocation> getAllValidEdgeLocations() {
        return allValidEdgeLocations;
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


    //FOR TESTING ONLY
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;

        Map map = (Map) o;

        if (radius != map.radius) return false;

        //hexes map
        if (!hexes.keySet().equals(map.getHexes().keySet()))
            return false;

        //ports map
        if (!ports.keySet().equals(map.getPorts().keySet()))
            return false;

        //vertexObjects
          //  Set<VertexLocation> tempVMe = vertexObjects.keySet();
          //  Set<VertexLocation> tempVYou = map.getVertexObjects().keySet();

        if (!vertexObjects.keySet().equals(map.getVertexObjects().keySet()))
            return false;

        //edgeValues
         //   Set<EdgeLocation> tempEMe = edgeValues.keySet();
         //   Set<EdgeLocation> tempEYou = map.getEdgeValues().keySet();

        if (!edgeValues.keySet().equals(map.getEdgeValues().keySet()))
            return false;

        //robber
        if (!(robber.getCurrentHexlocation().getX() == map.getRobber().getCurrentHexlocation().getX() &&
                robber.getCurrentHexlocation().getY() == map.getRobber().getCurrentHexlocation().getY()))
            return false;

        //passed all tests!
        return true;
    }

}
