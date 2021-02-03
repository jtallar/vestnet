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

            /** Remove the /api/ to obtain either an empty string or the application context */
            String applicationContext = baseUrlWithPath.getPath().replace("/api/", "");

            /** Create the final url, adding the /# for Angular */
            final URL finalUrl = new URL(baseUrlWithPath.getProtocol(), baseUrlWithPath.getHost(), baseUrlWithPath.getPort(), applicationContext + "/#");
            return new URI(finalUrl.toString());

        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }
    }
}
