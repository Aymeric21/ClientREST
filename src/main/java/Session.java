import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.util.Builder;
import javarow.*;
import javarow.entity.Status;
import javarow.entity.WorkoutState;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.time.Duration;

import static javarow.entity.Status.FINISHED;

public class Session {
    private Rower rower;
    private WebResource wr;

    public Session(WebResource wr)
    {
        this.rower = Rower.getInstance();
        this.wr = wr;
    }

    public Rower getRower()
    {
        return this.rower;
    }

    public void initSession(String type, String distancePar){
        rower.getMonitor();
        rower.goToMenuScreen();
        if(type.equals("distance"))
        {
            int dist = Integer.parseInt(distancePar);
            rower.setWorkoutDistance(dist);
        }
        else if(type.equals("temps")){
            double t = Double.parseDouble(distancePar);
            Duration time = Duration.ofSeconds((long) t);
            rower.setWorkoutTime(time);
        }
        System.out.println("Session initialisé");
    }

    public boolean lancerSession(String distancePar,String type, int idRameur) throws JSONException {
        initSession(type,distancePar);

        boolean b = false;

        int power;
        int temps;
        int distance;
        int coups_pm;
        int rythme;
        int calories_h;
        int calories;
        int frequence_bpm;

        ClientResponse response;
        DonneeJson dj = new DonneeJson();
        int nb = 0;

        while(rower.getWorkout().getWorkoutState() != WorkoutState.WORKOUT_END)
        {
            while(rower.getStroke().getCount() <= nb || rower.getStroke().getCount() == 0)
            {
                if(rower.getWorkout().getWorkoutState() == WorkoutState.WORKOUT_END ||rower.getStatus() == Status.READY){
                    System.out.println("fini break");
                    b = true;
                    return b;
                }

            }
            power = rower.getMonitor().getPower();
            temps = rower.getMonitor().getTimeCentisecond();
            distance = rower.getMonitor().getDistanceDecimeter();
            coups_pm = rower.getMonitor().getSpm();
            rythme = rower.getMonitor().getPaceSecondPerKilometer();
            calories_h = rower.getMonitor().getCaloriesPerHour();
            calories = rower.getMonitor().getCalories();
            frequence_bpm = rower.getMonitor().getHeartrateBpm();

            String s = dj.addValeur(power,temps,distance,coups_pm,rythme,calories_h,calories,frequence_bpm,idRameur).toJSONString();
            System.out.println(s);
            response = wr.accept("text/plain").put(ClientResponse.class,s);
            nb = rower.getStroke().getCount();
        }

        b = true;
        System.out.println("fini !");
        return b;
    }
}
