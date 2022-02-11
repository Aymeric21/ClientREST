import org.json.simple.JSONObject;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class DonneeJson {
    private JSONObject jo;

    public DonneeJson(){
        jo = new JSONObject();
    }

    public JSONObject addValeur(int power, int temps, int distance, int coups_pm, int rythme, int calories_h, int calories, int frequence_bpm, int id){

        jo.put("idRameur", new Integer(id));
        jo.put("tempsCs",new Integer(temps));
        jo.put("distanceCm",new Integer(distance));
        jo.put("coupsPm",new Integer(coups_pm));
        jo.put("puissanceW",new Integer(power));
        jo.put("rythmeMs",new Integer(rythme));
        jo.put("caloriesH",new Integer(calories_h));
        jo.put("calories",new Integer(calories));
        jo.put("frequenceBpm",new Integer(frequence_bpm));
        return jo;
    }
}
