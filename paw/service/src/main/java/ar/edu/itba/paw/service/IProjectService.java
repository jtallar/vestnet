package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.Pair;
import ar.edu.itba.paw.model.components.ProjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IProjectService implements ProjectService {

    private static final int PAGE_SIZE = 12;
    private static final Integer FIRST_PAGE = 1;
    private static final int PAGINATION_ITEMS = 5;

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project create(String name, String summary, long cost, byte[] image, long ownerId, List<Long> categoriesIds) {
        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        return projectDao.create(name, summary, cost, image, new User(ownerId), categories);
    }

    @Override
    public Optional<Project> findById(long projectId) {
        return projectDao.findById(projectId);
    }

    @Override
    public List<Project> findByIds(List<Long> ids) {
        return projectDao.findByIds(ids);
    }

    @Override
    public List<Project> findByOwner(long userId) {
        User owner = new User(userId);
        return projectDao.findByOwner(owner);
    }

    @Override
    public List<Project> findFiltered(ProjectFilter filter) {
        return projectDao.findFiltered(filter);
    }

    @Override
    public Integer countFiltered(ProjectFilter filter) {
        return projectDao.countFiltered(filter);
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


    @Override
    public Pair<Integer, Integer> setPaginationLimits(Integer projectCount, Integer page) {
        int maxPages = (int) Math.ceil((double) projectCount / (double) PAGE_SIZE);
        if (maxPages <= PAGINATION_ITEMS) return new Pair<>(FIRST_PAGE, maxPages == 0 ? 1: maxPages);
        int firstPage = page - PAGINATION_ITEMS / 2;
        if (firstPage <= FIRST_PAGE ) return new Pair<>(FIRST_PAGE, PAGINATION_ITEMS);
        int lastPage = page + PAGINATION_ITEMS / 2;
        if (lastPage <= maxPages) return new Pair<>(firstPage, lastPage);
        return new Pair<>(maxPages - PAGINATION_ITEMS, maxPages);
    }


    @Override
    public Integer getPageSize() {
        return PAGE_SIZE;
    }
}

