package shared.model.map;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.locations.HexLocation;

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
    private int number;

    /**
     * Resource type provided by this hex
     */
    private HexType hexType;

    public Hex(){

    }

    public Hex(HexLocation location, HexType hexType){
        this.location = location;
        this.hexType = hexType;
    }

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
        return number;
    }

    /**
     * Sets Number
     * @param number Number associated with hex
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return Resource type that hex provides
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

    @Override
    public String toString() {
        return "Hex{" +
                "location=" + location +
                ", number=" + number +
                ", hexType=" + hexType.toString() +
                '}';
    }
}
