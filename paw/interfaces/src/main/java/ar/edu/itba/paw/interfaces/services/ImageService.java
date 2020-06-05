package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    /**
     * Creates an image and saves it.
     * @param id The image "owner" project's id.
     * @param image The image bytes.
     * @param main If true then is the main cover, if not is for slideshow.
     * @return The created image.
     */
    ProjectImage create(long id, byte[] image, boolean main);


    /**
     * Creates an image and saves it.
     * @param id The image "owner" user's id.
     * @param image The image bytes.
     * @return The created image.
     */
    UserImage create(long id, byte[] image);


    /**
     * Finds user only profile image.
     * @param id The user unique id to find its image.
     * @return The optional user image.
     */
    Optional<UserImage> findUserImage(long id);


    /**
     * Finds project main profile image.
     * @param id The unique project id to find its image.
     * @return The optional main image.
     */
    Optional<ProjectImage> findProjectMain(long id);


    /**
     * Finds all project images.
     * @param id The unique project id to find its images.
     * @return A list with all the related images.
     */
    List<ProjectImage> findProjectAll(long id);
}
