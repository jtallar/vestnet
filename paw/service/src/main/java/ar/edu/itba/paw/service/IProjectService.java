package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project create(String name, String summary, long cost, byte[] image, long ownerId, List<Long> categoriesIds) {
        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        return projectDao.create(name, summary, cost, image, new User(ownerId), categories);
    }

    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }

    @Override
    public List<Project> findByOwnerId(long id) {
        List<FilterCriteria> param = Collections.singletonList(new FilterCriteria("owner", new User(id)));
        return projectDao.findAll(param, OrderField.DEFAULT, new PageRequest()).getContent();
    }

    @Override
    public Page<Project> findAll(Map<String, Object> filters, String order, Integer page, Integer pageSize) {
        /** Clean filters and create Criteria list */
        filters.values().removeIf(value -> (value == null || value.toString().equals("")));
        List<FilterCriteria> params = new ArrayList<>();
        filters.forEach((key, value) -> params.add(new FilterCriteria(key, value)));

        return projectDao.findAll(params, OrderField.getEnum(order), new PageRequest(page, pageSize));
    }









    @Override
    public List<Project> findByIds(List<Long> ids) {
        return projectDao.findByIds(ids);
    }









    @Override
    public byte[] findImageForProject(long projectId) {
        return projectDao.findImageForProject(projectId);
    }

    @Override
    public void addHit(long projectId) {
        projectDao.addHit(projectId);
    }

    @Override
    public void addFavorite(long projectId, long userId) {
        projectDao.addFavorite(projectId, userId);
    }

    @Override
    public void deleteFavorite(long projectId, long userId) {
        projectDao.deleteFavorite(projectId, userId);
    }

    @Override
    public boolean isFavorite(long projectId, long userId) {
        return projectDao.isFavorite(projectId,userId);
    }

    @Override
    public  List<Long> findFavorites(long id) {
        return projectDao.findFavorites(id);
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return projectDao.getFavoritesCount(projectId);
    }

    @Override
    public List<Long> getFavoritesCount(List<Long> projectIds) {
        return projectDao.getFavoritesCount(projectIds);
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        return projectDao.isFavorite(projectIds, userId);
    }

    @Override
    public List<Project> getUserFavorites(long userId) {
        return findByIds(findFavorites(userId));
    }



    /**
     * Creates the pagination logic.
     * @param projectCount The count of projects to paginate.
     * @param page The current pagination page.
     * @return A pair set as <startPage, endPage>
     */

}

