package ar.edu.itba.paw.interfaces;

public interface TokenExtractor {

    /**
     * Extract token from payload
     * @param payload full authorization header content
     * @return token without authorization type
     */
    String extract(String payload);
}
