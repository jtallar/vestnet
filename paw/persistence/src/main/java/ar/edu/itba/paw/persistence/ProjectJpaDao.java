package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.ProjectFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectJpaDao implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Project create(String name, String summary, long cost, byte[] image, User owner, List<Category> categories) {
        final Project project = new Project(name, summary, cost, image, owner, categories);
        entityManager.persist(project);
        return project;
    }

    @Override
    public Optional<Project> findById(long projectId) {
        return Optional.empty();
    }

    @Override
    public List<Project> findByIds(List<Long> ids) {
        return null;
    }

    @Override
    public List<Project> findByOwner(long userId) {
        return null;
    }

    @Override
    public List<Project> findFiltered(ProjectFilter filter) {
        return null;
    }

    @Override
    public Integer countFiltered(ProjectFilter filter) {
        return null;
    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return new byte[0];
    }

    @Override
    public void addHit(long projectId) {

    }

    @Override
    public void addFavorite(long projectId, long userId) {

    }

    @Override
    public void deleteFavorite(long projectId, long userId) {

    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return false;
    }

    @Override
    public List<Long> findFavorites(long id) {
        return null;
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return 0;
    }

    @Override
    public List<Long> getFavoritesCount(List<Long> projectIds) {
        return null;
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        return null;
    }
}
