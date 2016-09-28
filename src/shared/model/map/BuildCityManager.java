package shared.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 *
 * updates shared.model to show that a city is built at specified location
 */
public class BuildCityManager {

    private Map map;

    public BuildCityManager(Map map){
        this.map = map;
    }

    /**
     * Validates whether or not player may place city at location
     * Checks to see that player currently owns specified vertex
     * Checks that player has a settlemnt at specified location
     *
     * @param vert VertexObject where player is trying to build city
     * @return true if player currently owns location AND has a settlement on it
     */
    public boolean canPlaceCity(VertexObject vert) {
        return false;
    }


    /**
     * executed on the server side
     * Updates model to reflect a new city
     *
     * @param vert Vertex where city is built
     */
    public void build(VertexObject vert) {

    }
}
