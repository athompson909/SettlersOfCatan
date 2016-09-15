package model.map;

import java.util.List;

/**
 * Created by adamthompson on 9/15/16.
 */
public class Map {


    private List<Hex> hexes;

    Map() {

    }


    public List<Hex> getHexes() {
        return hexes;
    }

    public void setHexes(List<Hex> hexes) {
        this.hexes = hexes;
    }
}
