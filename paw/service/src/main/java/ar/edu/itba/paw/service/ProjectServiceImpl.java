package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.ProjectDao;
import ar.edu.itba.paw.interfaces.ProjectService;
import ar.edu.itba.paw.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;


    @Override
    public Project create(String name, String summary, int ownerId, Date date) {
        return projectDao.create(name, summary, ownerId, date);
    }

    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }

    @Override
    public List<Project> findByName(String name) {
        return projectDao.findByName(name);
    }


    @Override
    public List<Project> findAllProjects() {
        return projectDao.findAllProjects();
    }
}
