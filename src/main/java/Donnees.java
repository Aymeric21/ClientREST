import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class Donnees {
    private int power;
    private int temps;
    private int distance;
    private int coups_pm;
    private int rythme;
    private int calories_h;
    private int calories;
    private int frequence_bpm;

    public Donnees(int power, int temps, int distance, int coups_pm, int rythme, int calories_h, int calories, int frequence_bpm) {
        this.power = power;
        this.temps = temps;
        this.distance = distance;
        this.coups_pm = coups_pm;
        this.rythme = rythme;
        this.calories_h = calories_h;
        this.calories = calories;
        this.frequence_bpm = frequence_bpm;
    }

    @Produces({MediaType.APPLICATION_JSON})
    public Donnees envoie(){
        return this;
    }
}
