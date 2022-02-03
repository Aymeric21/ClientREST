import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javarow.*;
import javarow.entity.WorkoutState;

public class Main {
    public static void main(String[] args) {
        String url = "http://10.169.195.150:8080/ServeurREST-1.0-SNAPSHOT/api/hello-world";
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response;
        String s;
        int i;
        Session session = new Session();

        while(true){
            response = webResource.accept("text/plain").get(ClientResponse.class);
            s = response.getEntity(String.class);
            i = Integer.parseInt(s);
            System.out.println(s);
            if(i > 0){
                session.lancerSession(webResource,i);
            }
        }

    }
}
