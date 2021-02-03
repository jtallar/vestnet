package ar.edu.itba.paw.webapp.component;

import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriInfoUtils {

    public static URI getBaseURI(UriInfo uriInfo) {
        try {
            final URL baseUrlWithPath = uriInfo.getBaseUri().toURL();

            /** Strip the path in search of the application context
             * Could be /api/ or /applicationContext/api/
             * In case is the second we use it.
             * */
            final String[] aux = baseUrlWithPath.getPath().split("\\/");
            String applicationContext = "";
            if (aux.length > 2) applicationContext = "/" + aux[1];

            /** Create the final url, adding the /# for Angular */
            final URL finalUrl = new URL(baseUrlWithPath.getProtocol(), baseUrlWithPath.getHost(), baseUrlWithPath.getPort(), applicationContext + "/#");
            return new URI(finalUrl.toString());

        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }
    }
}
