import org.json.simple.JSONObject;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class DonneeJson {
    private JSONObject jo;

    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject addValeur(int puissance){

        jo.put("puissanc",new Integer(puissance));
        return jo;
    }
}
