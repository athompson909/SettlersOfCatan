package Client.model.map;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class Hex {

    /**
     * X and Y coordinates of hex location
     */
    private HexLocation location;

    /**
     * Number associated with hex
     * When this number is rolled, players will receive resources
     */
    private int Number;

    /**
     * Client.model.resourcebank.Resource type provided by this hex
     */
    private HexType hexType;

    /**
     * @return Location of hex
     */
    public HexLocation getLocation() {
        return location;
    }

    /**
     * Sets HexLocation of hex
     * @param location
     */
    public void setLocation(HexLocation location) {
        this.location = location;
    }

    /**
     * @return Number associated with hex
     */
    public int getNumber() {
        return Number;
    }

    /**
     * Sets Number
     * @param number Number associated with hex
     */
    public void setNumber(int number) {
        Number = number;
    }

    /**
     * @return Client.model.resourcebank.Resource type that hex provides
     */
    public HexType getHexType() {
        return hexType;
    }

    /**
     * Sets HexType to correct resource
     * @param hexType resource that hex provides
     */
    public void setHexType(HexType hexType) {
        this.hexType = hexType;
    }
}
