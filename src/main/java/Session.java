import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.util.Builder;
import javarow.*;
import javarow.entity.Status;
import javarow.entity.WorkoutState;

import javax.ws.rs.core.MediaType;
import java.time.Duration;

import static javarow.entity.Status.FINISHED;

public class Session {
    private Rower rower;

    public Session()
    {
        this.rower = Rower.getInstance();
    }

    public Rower getRower()
    {
        return this.rower;
    }

    public void lancerSession(WebResource wr,String distancePar,String type){
        rower.goToMenuScreen();
        rower.getMonitor();
        System.out.println("session lancé");
        WebResource.Builder builder = wr.accept(MediaType.APPLICATION_JSON).header("content-type",MediaType.APPLICATION_JSON);
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

        Command command = new Command();
        command.addCommand(ShortSpecificPMCommand.CSAFE_PM_GET_WORKOUTSTATE);
        UsbResponse resp = rower.sendCommand(command);
        Response.CSAFE_PM_GET_WORKOUTSTATE workoutState = (Response.CSAFE_PM_GET_WORKOUTSTATE) resp.specificPMResponses.get(Response.SPECIFIC_PM_CODE.CSAFE_PM_GET_WORKOUTSTATE);
        System.out.println(command.getMessage());

        int power;
        int temps;
        int distance;
        int coups_pm;
        int rythme;
        int calories_h;
        int calories;
        int frequence_bpm;
        String spower;
        ClientResponse response;
        Donnees donnees;

        int nb = 0;

        ClientResponse resp2;
        while(rower.getWorkout().getWorkoutState() != WorkoutState.WORKOUT_END)
        {
            while(rower.getStroke().getCount() <= nb || rower.getStroke().getCount() == 0)
            {
                System.out.println("STATE : " + rower.getWorkout().getWorkoutState());
                System.out.println("STATUS : " + rower.getStatus());

                if(rower.getWorkout().getWorkoutState() == WorkoutState.WORKOUT_END ||rower.getStatus() == Status.READY){
                    /*== WorkoutState.WORKOUT_REARM || rower.getWorkout().getWorkoutState() == WorkoutState.WORKOUT_TERMINATE){*/
                    System.out.println("un truc");
                    return;
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
            donnees = new Donnees(power,temps,distance,coups_pm,rythme,calories_h,calories,frequence_bpm);
            //response = builder.put(ClientResponse.class,donnees.envoie());
            System.out.println(donnees.envoie());
            nb = rower.getStroke().getCount();
        }

        Command c = new Command();
        c.addCommand(ShortCommand.CSAFE_RESET_CMD);
        rower.sendCommand(command);

        System.out.println("fini !");
    }
}
