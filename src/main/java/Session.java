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

    public void initSession(String type, String distancePar, int repos, int repetition){
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
        }else if(type.equals("entrainement")){
            int dist = Integer.parseInt(distancePar);
            rower.setWorkoutDistance(dist);
        }
        System.out.println("Session initialisé");
    }

    public boolean lancerSession(String distancePar,String type, int idRameur,int repos, int repetition) throws JSONException {
        initSession(type,distancePar,repos,repetition);

        boolean b = false;

        int power = 0;
        int temps = 0;
        int distance = 0;
        int coups_pm = 0;
        int rythme = 0;
        int calories_h = 0;
        int calories = 0;
        int frequence_bpm = 0;

        ClientResponse response;
        DonneeJson dj = new DonneeJson();
        int nb = 0;

        int dist = 0;
        int temp = 0;
        if(type.equals("distance")) {
            dist = Integer.parseInt(distancePar)*10;
        }

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
            int temp2=0;
            if(type.equals("distance")) {
                if(dist == distance){
                    power = 0;
                    temps = 0;
                    distance = 0;
                    coups_pm = 0;
                    rythme = 0;
                    calories_h = 0;
                    calories = 0;
                    frequence_bpm = 0;
                }else {
                    System.out.println("distance programmée : " + dist);
                    System.out.println("distance restante : " + distance);

                    if(temp != 0)
                        temp2 = temp - distance;
                    else
                        temp2 = dist - distance;
                    temp = distance;

                    System.out.println("distance parcourue : " + temp2);
                }
            }

            String s = dj.addValeur(power,temps,temp2,coups_pm,rythme,calories_h,calories,frequence_bpm,idRameur).toJSONString();
            System.out.println(s);
            response = wr.accept("text/plain").put(ClientResponse.class,s);
            nb = rower.getStroke().getCount();
        }

        b = true;
        System.out.println("fini !");
        return b;
    }
}
