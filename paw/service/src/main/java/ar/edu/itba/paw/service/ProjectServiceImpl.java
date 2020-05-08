package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
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
    public List<Project> findCoincidence(String name,String selection, int from, int to) {
        return projectDao.findCoincidence(name,selection, from, to);
    }

    @Override
    public Integer searchProjCount(String name, String selection) {
        return projectDao.searchProjCount(name, selection);
    }

    @Override
    public List<Project> findPage(int from, int to, long min, long max) {
        return projectDao.findPage(from,to, min, max);
    }

    @Override
    public Integer projectsCount(long min, long max) {
        return projectDao.projectsCount(min, max);
    }

    @Override
    public List<Project> findCatForPage(List<Category> categories, int from, int to, long min, long max) {
        return projectDao.findCatForPage(categories,from,to, min, max);
    }

    @Override
    public Integer catProjCount(List<Category> categories, long min, long max) {
        return projectDao.catProjCount(categories, min, max);
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
}

