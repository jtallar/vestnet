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
    public List<Project> findCoincidence(String name) {
        return projectDao.findCoincidence(name);
    }

    @Override
    public List<Project> findPage(int from, int to) {
        return projectDao.findPage(from,to);
    }

    @Override
    public Integer projectsCount() {
        return projectDao.projectsCount();
    }

    @Override
    public byte[] findImageForProject(long projectId) {
        return projectDao.findImageForProject(projectId);
    }
}

