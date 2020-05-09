package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Optional<Project> findById(long projectId) {
        return projectDao.findById(projectId);
    }

    @Override
    public List<Project> findAll() {
        return projectDao.findAll();
    }

    @Override
    public List<Project> findByCategories(List<Category> categories) {
        return projectDao.findByCategories(categories);
    }

    @Override
    public long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes) {
        return projectDao.create(name, summary, cost, ownerId, categoriesIds, stages, imageBytes);
    }

    @Override
    public List<Project> findByOwner(long userId) {
        return projectDao.findByOwner(userId);
    }

    @Override
    public List<Project> findByCoincidencePage(String name, String selection, int pageStart, int pageOffset) {
        return projectDao.findByCoincidencePage(name,selection, pageStart, pageOffset);
    }

    @Override
    public Integer countByCoincidence(String name, String selection) {
        return projectDao.countByCoincidence(name, selection);
    }

    @Override
    public List<Project> findByCostPage(int pageStart, int pageOffset, long minCost, long maxCost) {
        return projectDao.findByCostPage(pageStart, pageOffset, minCost, maxCost);
    }

    @Override
    public Integer countByCost(long minCost, long maxCost) {
        return projectDao.countByCost(minCost, maxCost);
    }

    @Override
    public List<Project> findByCategoryPage(List<Category> categories, int pageStart, int pageOffset, long minCost, long maxCost) {
        return projectDao.findByCategoryPage(categories, pageStart, pageOffset, minCost, maxCost);
    }

    @Override
    public Integer countByCategory(List<Category> categories, long minCost, long maxCost) {
        return projectDao.countByCategory(categories, minCost, maxCost);
    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return projectDao.findImageForProject(projectId);
    }

    @Override
    public  List<Long>  findFavorites(long id) { return projectDao.findFavorites(id); }

    @Override
    public void addFavorite(long projectId, long userId) { projectDao.addFavorite(projectId, userId); }

    @Override
    public void deleteFavorite(long projectId, long userId) { projectDao.deleteFavorite(projectId, userId); }

    @Override
    public boolean isFavorite(long projectId, long userId) { return projectDao.isFavorite(projectId,userId); }

    @Override
    public void addHit(long projectId) {
        projectDao.addHit(projectId);
    }

    @Override
    public long getFavoritesCount(long projectId) {
        return projectDao.getFavoritesCount(projectId);
    }

    @Override
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        return projectDao.isFavorite(projectIds, userId);
    }
}

