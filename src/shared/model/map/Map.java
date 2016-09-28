package shared.model.map;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.locations.*;

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
     * List of all ports used to create map
     */
    private HashMap<HexLocation, Port> ports = new HashMap<>();

    private HashMap<VertexLocation, VertexObject> vertexObjects = new HashMap<>();

    private HashMap<EdgeLocation, EdgeValue> edgeObjects = new HashMap<>();

    private List<VertexLocation> portVertexLocations = new ArrayList<>();

    /**
     * Robber object
     */
    private Robber robber;

    /**
     * Manages the checking and building of roads, settlements,
     * and cities (after being checked within Player class)
     */
    private BuildingManager buildingManager;

    private static List<Integer> numberOrder = Arrays.asList(5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11);

    private static Iterator<Integer> numberIterator = numberOrder.iterator();

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

    }

    //this is for when the new model comes back and we need to make a new Map object WITHOUT
    //creating/placing all hexes again
    public Map(HashMap<HexLocation, Hex> allHexes, HashMap<HexLocation, Port> allPorts)
    {
        setHexes(allHexes);
        setPorts(allPorts);
    }

    private void createAllWaterHexes() {
        //18 Water hexes at these specified locations starting with north-west corner going counterclockwise
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

    private void placePort(int x, int y, PortType portType, EdgeDirection edgeDirection) {
        HexLocation hexLocation = new HexLocation(x, y);
        Port newPort = new Port(portType, hexLocation, edgeDirection);
        ports.put(hexLocation, newPort);
    }

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

        if(randomlyPlaceNumbers){ //shuffle the number order
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

    private void createLandHex(int x, int y, HexType hexType) {
        Hex landHex = new Hex(new HexLocation(x, y), hexType);
        if (hexType != HexType.DESERT) {

            landHex.setNumber(numberIterator.next().intValue());
        }
        hexes.put(landHex.getLocation(), landHex);
    }

    private void createAllVertexObjects() {

        //VertexLocation

        createVertexObject(-2, 0, VertexDirection.NorthWest);
        createVertexObject(-2, 0, VertexDirection.NorthEast);
    }

    private void createVertexObject(int x, int y, VertexDirection direction){
        VertexLocation vertexLocation = new VertexLocation(new HexLocation(x,y), direction);
        VertexObject vertexObject = new VertexObject(vertexLocation);
    }

    private void populatePortVertexLocations(){
        portVertexLocations.add(new VertexLocation(new HexLocation(-2,0), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(-3,1), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(-3,2), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(-2,2), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(-2,3), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(-1,3), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(0,3), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(0,3), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(1,2), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(2,1), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(2,0), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(3,-1), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(3,-2), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(2,-2), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(1,-2), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(1,-2), VertexDirection.NorthWest));
        portVertexLocations.add(new VertexLocation(new HexLocation(-1,-1), VertexDirection.NorthEast));
        portVertexLocations.add(new VertexLocation(new HexLocation(-1,-1), VertexDirection.NorthWest));
    }


    /**
     * Updates all map data members to match the newly updated model
     * <p>
     * Updates robber to new position
     * Updates list of EdgeValues where roads are stored
     * Updates list of VertexObjects where settlements are built
     * Updates list of VertexObjects where cities are built
     *
     * @param newMap updated map received from the updated clientModel
     */
    public void updateMap(Map newMap) {
        setRobber(newMap.robber);
    }

    public HashMap<HexLocation, Hex> getHexes() {
        return hexes;
    }

    public HashMap<HexLocation, Port> getPorts() {
        return ports;
    }

    public Robber getRobber() {
        return robber;
    }

    public void setRobber(Robber robber) {
        this.robber = robber;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    public void setBuildingManager(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
    }
}
