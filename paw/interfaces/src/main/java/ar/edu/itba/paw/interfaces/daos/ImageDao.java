package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;

import java.util.List;
import java.util.Optional;

public interface ImageDao {

    /**
     * Creates an image and saves it.
     * @param project The project image "owner".
     * @param image The image bytes.
     * @param main If true then is the main cover, if not is for slideshow.
     * @return The created image.
     */
    ProjectImage create(Project project, byte[] image, boolean main);


    /**
     * Creates an image and saves it.
     * @param image The image bytes.
     * @return The created image.
     */
    UserImage create(byte[] image);


    /**
     * Finds user only profile image.
     * @param id The image id.
     * @return The optional user image.
     */
    Optional<UserImage> findUserImage(long id);


    /**
     * Finds project main profile image.
     * @param project The project to find its image.
     * @return The optional main image.
     */
    Optional<ProjectImage> findProjectMain(Project project);


    /**
     * Finds all project images.
     * @param project The project to find its images.
     * @return A list with all the related images.
     */
    List<ProjectImage> findProjectAll(Project project);
}
