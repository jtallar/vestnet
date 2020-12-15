package ar.edu.itba.paw.webapp.component;

import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriInfoUtils {
    // TODO: PROBAR SI ESTO FUNCA BIEN EN DEPLY, PUEDE QUE ELIMINE EL paw2020-a-5
    public static URI getBaseURI(UriInfo uriInfo) {
        try {
            final URL baseUrlWithPath = uriInfo.getBaseUri().toURL();
            final URL finalUrl = new URL(baseUrlWithPath.getProtocol(), baseUrlWithPath.getHost(), baseUrlWithPath.getPort(), "/#");
            return new URI(finalUrl.toString());
        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }
    }
}
