package DAO_tests;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;
import server.plugins.data_access.FileGameDAO;
import shared.locations.*;
import shared.model.commandmanager.moves.BuildRoadCommand;
import shared.model.commandmanager.moves.BuildSettlementCommand;
import shared.model.map.VertexObject;

/**
 *
 * Created by Sierra on 12/1/16.
 */
public class fileGameDAOTest  extends TestCase {

    FileGameDAO fileGameDAO = new FileGameDAO();
    BuildRoadCommand brc1;
    BuildSettlementCommand bsc1;
    JSONObject brc1JSON;
    JSONObject bsc1JSON;

    Gson gsonTranslator = new Gson();

    @Test
    public void setUp() throws Exception {
        super.setUp();

        setUpSampleCmds();
    }

    public void setUpSampleCmds(){

            HexLocation brc1HL1 = new HexLocation(-1,1);
            EdgeLocation brc1EL1 = new EdgeLocation(brc1HL1, EdgeDirection.North);
        brc1 = new BuildRoadCommand(brc1EL1, 0, true);

        brc1JSON = new JSONObject(gsonTranslator.toJson(brc1));  //should look just like how the server usually wants it

            HexLocation bsc1HL1 = new HexLocation(0,2);
            VertexLocation bscVL1 = new VertexLocation(bsc1HL1, VertexDirection.SouthEast);
            VertexObject bscVO1 = new VertexObject(bscVL1);
            bscVO1.setOwner(0);
        bsc1 = new BuildSettlementCommand(bscVO1, false);

        bsc1JSON = new JSONObject(gsonTranslator.toJson(bsc1));


    }

    @Test
    public void testWriteCommand() throws Exception {
        System.out.println(">TESTING WRITECOMMAND!");

        //this fn should:
        //create a new file called cmds0.json inside the /json_files/games folder
        //write sample Command brc1 to it (in JSON)

        fileGameDAO.writeCommand(brc1JSON, 3); //3 is the test gameID
        fileGameDAO.writeCommand(bsc1JSON, 3);  //testing whether it appends to same file after creating it
                //works! :D

        //assert file exists? or just read it in again
    }



}
