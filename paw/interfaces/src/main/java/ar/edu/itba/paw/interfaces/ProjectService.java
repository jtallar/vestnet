package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.ProjectCategories;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    public Project create(String name, String summary, int ownerId, Date date, ProjectCategories cat);

    public Optional<Project> findById(long id);

    public List<Project> findByName(String name);

    public List<Project> findAllProjects();

    public List<Project> filterProjectByCategory(ProjectCategories cat);
}
