import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javarow.*;
import javarow.entity.WorkoutState;

public class Main{
    public static void main(String[] args) {

        ///////////////URL de connexion au serveur/////////////////////////////////////////////////////////////////////////////

        String url = "http://10.169.195.150:8080/ServeurEJBRameurTutore-1.0-SNAPSHOT/rest/ressource/";
        String urlValeur = url+"valeur";
        String urlType = url+"type";
        String urlPerformance = url+"donnees";
        String urlid = url+"ajoutRameur";
        String urliden = url+"identifiant";

        //////////////Pramétrage du client et des ressources serveur//////////////////////////////////////////////////////////
        Integer interval = 100000;
        Client client = Client.create();
        client.setReadTimeout(interval);
        WebResource webResourceValeur = client.resource(urlValeur);
        WebResource webResourceType = client.resource(urlType);
        WebResource webResourcePerf = client.resource(urlPerformance);
        WebResource webResourceId = client.resource(urlid);
        WebResource webResourceiden = client.resource(urliden);

        ClientResponse responseValeur;
        ClientResponse responseType;
        ClientResponse responseId;
        ClientResponse responseIden;


        String s;
        String t;
        int i;
        Session session = new Session();


        /////////////////récupération de l'id de notre client générer par le serveur//////////////////////////////////////////

        responseIden = webResourceiden.accept("text/plain").get(ClientResponse.class);
        String id = responseIden.getEntity(String.class);
        if(id == null || id.equals(""))
        {
            System.out.println("mauvaise récupération de l'id");
            System.exit(2);
        }


        ///////////////initialisation des paramètre du rameur//////////////////////////////////////////////////////////////////
        responseId = webResourceId.accept("text/plain").put(ClientResponse.class,id);


        //////////////boucle infini pour attendre une session puis la lancer///////////////////////////////////////////////////
        while(true){

            //////////récupération du type de course en temps ou en distance ainsi/////////////////////////////////////////////
            //////////que de la distance ou de temps de la session/////////////////////////////////////////////////////////////
            responseValeur = webResourceValeur.accept("text/plain").put(ClientResponse.class,id);
            s = responseValeur.getEntity(String.class);
            responseType = webResourceType.accept("text/plain").put(ClientResponse.class,id);
            t = responseType.getEntity(String.class);

            /////////test que la valeur de distance ou de temps est bien récupérer/////////////////////////////////////////////
            if(!s.equals(""))
            {
                i = Integer.parseInt(s);
                if(i > 0 && (t.equals("distance") || t.equals("temps"))){
                    session.lancerSession(/*webResourcePerf,*/s,t);
                }
                i = 0;
            }

            /////////réinitialisation de l'état du client pour signifier au serveur que la session est fini///////////////////
            responseId = webResourceId.accept("text/plain").put(ClientResponse.class,id);
        }

    }
}
