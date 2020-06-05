package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import ar.edu.itba.paw.model.location.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ImageJpaDao implements ImageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ProjectImage create(Project project, byte[] image, boolean main) {
        final ProjectImage projectImage = new ProjectImage(project, image, main);
        entityManager.persist(projectImage);
        return projectImage;
    }

    @Override
    public UserImage create(User user, byte[] image) {
        final UserImage userImage = new UserImage(user, image);
        entityManager.persist(userImage);
        return userImage;
    }

    @Override
    public Optional<UserImage> findUserImage(User user) {
        final TypedQuery<UserImage> query = entityManager.createQuery("from UserImage where user = :user", UserImage.class);
        query.setParameter("user", user);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<ProjectImage> findProjectMain(Project project) {
        final TypedQuery<ProjectImage> query = entityManager.createQuery("from ProjectImage where project = :project and main = true", ProjectImage.class);
        query.setParameter("project", project);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<ProjectImage> findProjectAll(Project project) {
        final TypedQuery<ProjectImage> query = entityManager.createQuery("from ProjectImage where project = :project", ProjectImage.class);
        query.setParameter("project", project);
        return query.getResultList();
    }
}
