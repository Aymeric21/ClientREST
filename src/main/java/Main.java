import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javarow.*;
import javarow.entity.WorkoutState;

public class Main {
    public static void main(String[] args) {
        String url = "http://10.169.195.150:8080/ServeurEJBRameurTutore-1.0-SNAPSHOT/rest/ressource/";
        String urlValeur = url+"valeur";
        String urlType = url+"type";
        String urlPerformance = url+"donnees";
        String urlid = url+"ajoutRameur";
        String urliden = url+"identifiant";
        Client client = Client.create();
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
        responseIden = webResourceiden.accept("text/plain").get(ClientResponse.class);
        String id = responseIden.getEntity(String.class);
        responseId = webResourceId.accept("text/plain").put(ClientResponse.class,id);
        while(true){
            responseValeur = webResourceValeur.accept("text/plain").put(ClientResponse.class,id);
            s = responseValeur.getEntity(String.class);
            responseType = webResourceType.accept("text/plain").put(ClientResponse.class,id);
            t = responseType.getEntity(String.class);
            if(!s.equals(""))
            {
                i = Integer.parseInt(s);
            }
            else
            {
                i = 0;
            }
            System.out.println(i);
            System.out.println(t);
            if(i > 0 && (t.equals("distance") || t.equals("temps"))){
                session.lancerSession(/*webResourcePerf,*/s,t);
            }
            responseId = webResourceId.accept("text/plain").put(ClientResponse.class,id);
        }

    }
}
