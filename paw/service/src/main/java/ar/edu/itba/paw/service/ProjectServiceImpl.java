package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.ProjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public long create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, List<Stage> stages, byte[] imageBytes) {
        return projectDao.create(name, summary, cost, ownerId, categoriesIds, stages, imageBytes);
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
        return projectDao.findByOwner(userId);
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
    public List<Boolean> isFavorite(List<Long> projectIds, long userId) {
        return projectDao.isFavorite(projectIds, userId);
    }

    @Override
    public List<Project> getUserFavorites(long userId) {
        return projectDao.findByIds(projectDao.findFavorites(userId));
    }
}

