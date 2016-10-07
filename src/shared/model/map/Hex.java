package shared.model.map;

import shared.definitions.HexType;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import java.util.ArrayList;

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
     * if it's 0, it's an ocean hex
     */
    private int number = 0;

    /**
     * Resource type provided by this hex
     * also can be ocean/water
     */
    private HexType resource = null;

    public Hex(HexLocation location, HexType hexType){
        this.location = location;
        this.resource = hexType;
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
    public HexType getResource() {
        return resource;
    }

    /**
     * Sets HexType to correct resource
     * @param resource resource that hex provides
     */
    public void setResource(HexType resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Hex{" +
                "location=" + location +
                ", number=" + number +
                ", resource=" + resource.toString() +
                '}';
    }




}
