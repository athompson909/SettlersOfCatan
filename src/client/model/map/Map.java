package client.model.map;

import java.util.List;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * contains all data related to hexes, hex values, ports, objects on map, dimensions, and the robber
 */
public class Map {

    /**
     * List of all hexes used to create map
     */
    private List<Hex> hexes;

    /**
     * List of all ports used to create map
     */
    private List<Port> ports;

    /**
     * List of EdgeValues created
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
     * radius of map (=4)
     */
    private int radius;

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
     *
     * @param isRandom true if user wants to randommize map
     */
    Map(boolean isRandom) {

    }

    /**
     * called if boolean isRandom is true in Map constructor
     * randomly places hexes, numbers, and ports on the map
     */
    private void createRandomMap() {}


    /**
     * is called if boolean isRandom is false in Map constructor
     * creates map in the default format
     */
    private void createDefaultMap() {}

    /**
     * randomly assigns locations to hexes in the map
     */
    private void assignHexes() {}

    /**
     * randomly assigns number values to each resource hex in the map
     */
    private void assignHexValues() {}


    /**
     * randomly assigns ports with random values to ocean hexes
     * constraints on randomness:
     *  no two ports can be adjacent
     *  order of hexes: 2:1, 3:1, 2:1, 3:1, 2:1, 3:1, 2:1 (the first and last 2:1 hexes will not have a 3:1 in between themselves)
     */
    private void assignPorts() {}


    public List<Hex> getHexes() {
        return hexes;
    }

    public void setHexes(List<Hex> hexes) {
        this.hexes = hexes;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
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
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
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
