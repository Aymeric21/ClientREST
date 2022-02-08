import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javarow.*;
import javarow.entity.WorkoutState;

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

    public void lancerSession(/*WebResource wr,*/String distancePar,String type){
        rower.goToMenuScreen();
        rower.getMonitor();
        System.out.println("session lancé");
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

        int nb = 0;

        ClientResponse resp2;
        while(rower.getWorkout().getWorkoutState() != WorkoutState.WORKOUT_END)
        {
            System.out.println(rower.getStatus().compareTo(FINISHED));
            while(rower.getStroke().getCount() <= nb || rower.getStroke().getCount() == 0) {
                if(rower.getWorkout().getWorkoutState() == WorkoutState.WORKOUT_END){
                    break;
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

            spower = ""+power;
            //resp2 = wr.accept("text/plain").put(ClientResponse.class,spower);

            nb = rower.getStroke().getCount();
        }

        Command c = new Command();
        c.addCommand(ShortCommand.CSAFE_RESET_CMD);
        rower.sendCommand(command);

        System.out.println("fini !");
    }
}
