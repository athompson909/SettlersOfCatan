package model.map;

/**
 * Created by Mitchell on 9/15/2016.
 */
public class Hex {

    private HexLocation location;

    private String source;

    private int Number;

    private HexType hexType;


    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public HexType getHexType() {
        return hexType;
    }

    public void setHexType(HexType hexType) {
        this.hexType = hexType;
    }
}
