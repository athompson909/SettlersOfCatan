package shared.model.map;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;

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

    /**
     * List of EdgeValues where roads are built
     */
    private List<EdgeValue> roads;

    /**
     * List of vertexObjects where settlements are built
     */
    private List<VertexObject> settlements;

    /**
     * List of vertexObjects where cities are built
     */
    private List<VertexObject> cities;

    /**
     * radius of map (=3). The center is (0,0), and has 3 hexes in any direction (including ocean borders)
     */
    private final int RADIUS = 3;

    /**
     * Robber object
     */
    private Robber robber;

    /**
     * Manages the checking and building of roads, settlements,
     * and cities (after being checked within Player class)
     */
    private BuildingManager buildingManager;

    /**
     * the constructor for a Map object
     * is called when a user starts a new game and the map needs to be created (in initialization mode)
     */
    Map(boolean randomlyPlaceNumbers, boolean randomlyPlaceHexes, boolean randomlyPlacePorts) {
        createAllWaterHexes();
        placeAllPorts(randomlyPlacePorts);
        createAllLandHexes(randomlyPlaceHexes);


    }

    private void createAllWaterHexes() {
        //18 Water hexes at these specified locations
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
        List<PortType> resourcePortList = Arrays.asList(PortType.ORE, PortType.BRICK, PortType.WOOD, PortType.WHEAT, PortType.SHEEP);

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

    private void createAllLandHexes(boolean randomlyPlaceHexes) {

    }




    /**
     * called if boolean isRandom is true in Map constructor
     * randomly places hexes, numbers, and ports on the map
     */
    private void createRandomMap() {
    }


    /**
     * is called if boolean isRandom is false in Map constructor
     * creates map in the default format
     */
    private void createDefaultMap() {
    }

    /**
     * randomly assigns locations to hexes in the map
     */
    private void assignHexes() {
    }

    /**
     * randomly assigns number values to each resource hex in the map
     */
    private void assignHexValues() {
    }


    /**
     * randomly assigns ports with random values to ocean hexes
     * constraints on randomness:
     * no two ports can be adjacent
     * order of hexes: 2:1, 3:1, 2:1, 3:1, 2:1, 3:1, 2:1 (the first and last 2:1 hexes will not have a 3:1 in between themselves)
     */
    private void assignPorts() {
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
        setRoads(newMap.roads);
        setSettlements(newMap.settlements);
        setCities(newMap.cities);

    }

    public HashMap<HexLocation, Hex> getHexes() {
        return hexes;
    }

    public void setHexes(HashMap<HexLocation, Hex> hexes) {
        this.hexes = hexes;
    }

    public HashMap<HexLocation, Port> getPorts() {
        return ports;
    }

    public void setPorts(HashMap<HexLocation, Port> ports) {
        this.ports = ports;
    }

    public List<EdgeValue> getRoads() {
        return roads;
    }

    public void setRoads(List<EdgeValue> roads) {
        this.roads = roads;
    }

    public List<VertexObject> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<VertexObject> settlements) {
        this.settlements = settlements;
    }

    public List<VertexObject> getCities() {
        return cities;
    }

    public void setCities(List<VertexObject> cities) {
        this.cities = cities;
    }

    public int getRadius() {
        return RADIUS;
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
