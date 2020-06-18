package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public UserImage create(byte[] image) {
        final UserImage userImage = new UserImage(image);
        entityManager.persist(userImage);
        return userImage;
    }


    @Override
    public Optional<UserImage> findUserImage(long id) {
        return Optional.ofNullable(entityManager.find(UserImage.class, id));
    }


    @Override
    public List<ProjectImage> findProjectImages(Project project, boolean main) {
        final TypedQuery<ProjectImage> query = entityManager.createQuery("from ProjectImage where project = :project and main = :main", ProjectImage.class);
        query.setParameter("project", project);
        query.setParameter("main", main);
        return query.getResultList();
    }
}
