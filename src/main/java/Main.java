import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javarow.*;
import javarow.entity.WorkoutState;
import org.codehaus.jettison.json.JSONException;

import java.time.Duration;

public class Main{

    private final static String url = "http://10.169.195.150:8080/ServeurEJBRameurTutore-1.0-SNAPSHOT/rest/ressource/";
    private final static String urlPerformance = "http://10.169.195.150:8080/ServeurEJBRameurTutore-1.0-SNAPSHOT/rest/donneeJson/puissance";
    private final static String urlValeur = url+"valeur";
    private final static String urlType = url+"type";
    private final static String urlRepos = url+"repos";
    private final static String urlRepetition = url+"repetition";
    private final static String urlid = url+"ajoutRameur";
    private final static String urliden = url+"identifiant";

    private final static Integer interval = Integer.MAX_VALUE;

    public static void main(String[] args) throws InterruptedException, JSONException {

        //////////////Pramétrage du client et des ressources serveur//////////////////////////////////////////////////////////
        Client client = Client.create();
        client.setReadTimeout(interval);
        WebResource webResourceDelete = client.resource(url);
        WebResource webResourceValeur = client.resource(urlValeur);
        WebResource webResourceType = client.resource(urlType);
        WebResource webResourceRepos = client.resource(urlRepos);
        WebResource webResourceRepetition = client.resource(urlRepetition);
        WebResource webResourcePerf = client.resource(urlPerformance);
        WebResource webResourceId = client.resource(urlid);
        WebResource webResourceiden = client.resource(urliden);
        ClientResponse response;


        /////////////////récupération de l'id de notre client généré par le serveur//////////////////////////////////////////
        response = webResourceiden.accept("text/plain").get(ClientResponse.class);
        String id = response.getEntity(String.class);
        int iid = Integer.
                parseInt(id);

        ///////////////initialisation des paramètre du rameur//////////////////////////////////////////////////////////////////
        response = webResourceId.accept("text/plain").put(ClientResponse.class,id);

        String s;
        String t;
        String rps;
        String rptt;
        int i;
        Session session = new Session(webResourcePerf);
        boolean b = false;

        //////////////boucle infini pour lancer des session les une à la suite des autres///////////////////////////////////////////////////
        while(true){

            //////////récupération du type de course en temps ou en distance ainsi/////////////////////////////////////////////
            //////////que de la distance ou de temps de la session/////////////////////////////////////////////////////////////
            do{
                response = webResourceValeur.accept("text/plain").put(ClientResponse.class,id);
                s = response.getEntity(String.class);
                //System.out.println(s);
                response = webResourceType.accept("text/plain").put(ClientResponse.class,id);
                t = response.getEntity(String.class);
                //System.out.println(t);
                response = webResourceRepos.accept("text/plain").put(ClientResponse.class,id);
                rps = response.getEntity(String.class);
                //System.out.println(rps);
                response = webResourceRepetition.accept("text/plain").put(ClientResponse.class,id);
                rptt = response.getEntity(String.class);
                //System.out.println(rptt);
            }while(response.getStatus() != 200);

            /////////test que la valeur de distance ou de temps est bien récupérée/////////////////////////////////////////////
            i=0;
            try{
                i = Integer.parseInt(s);
            }finally {
                if(i == 0){
                    response = webResourceDelete.accept("text/plain").delete(ClientResponse.class,id);
                    System.out.println("erreur lors de la récupération de la valeur");
                    System.exit(2);
                }
            }

            int repetition = Integer.parseInt(rptt);
            int repos = Integer.parseInt(rps);

            if(i > 0 && (t.equals("distance") || t.equals("temps") || t.equals("entrainement"))){
                //System.out.println("J'entre dans la boucle if");
                if(t.equals("entrainement"))
                {
                    /*
                    System.out.println("Temps de repos : "+rps);
                    System.out.println("Nb répétition : "+rptt);
                    System.out.println("Distance : "+s);*/
                    for (int j = 0; j < repetition; j++) {
                        session.getRower().goToMenuScreen();
                        b = session.lancerSession(s,t,iid,repos,repetition);
                        //Thread.sleep(5000);
                        //session.getRower().goToMenuScreen();
                        System.out.println("Pause de "+repos+" secondes");
                        //session.getRower().setWorkoutTime(Duration.ofSeconds(Long.parseLong(rps)));
                        Thread.sleep(repos*1000);
                    }
                }else if(t.equals("distance") || t.equals("temps")) {
                    b = session.lancerSession(s, t, iid, repos, repetition);
                }
            }

            Thread.sleep(5000);
            /////////réinitialisation de l'état du client pour signifier au serveur que la session est fini///////////////////
            response = webResourceId.accept("text/plain").put(ClientResponse.class,id);
        }

    }
}
