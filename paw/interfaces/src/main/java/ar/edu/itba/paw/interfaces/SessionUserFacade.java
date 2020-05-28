package ar.edu.itba.paw.interfaces;

public interface SessionUserFacade {

    /**
     * Checks if the logged user is Anonymous.
     * @return True if it is not logged in, false otherwise.
     */
    boolean isAnonymous();

    /**
     * Checks if the logged user is an Investor.
     * @return True if the logged user is Investor, false otherwise.
     */
    boolean isInvestor();

    /**
     * Checks if the logged user is an Entrepreneur.
     * @return True if the logged user is Entrepreneur, false otherwise.
     */
    boolean isEntrepreneur();

    /**
     * Gets the logged user ID.
     * @return The unique logged user's ID.
     */
    long getId();

    /**
     * Gets the logged user mail address.
     * @return The logged user's mail address.
     */
    String getMail();

}
