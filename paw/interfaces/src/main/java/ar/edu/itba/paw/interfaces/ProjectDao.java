package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.ProjectCategories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectDao {

    public Optional<Project> findById(long id);

    public List<Project> findByName(String name);

    public Project create(String name, String summary, long ownerId, Date date, ProjectCategories cat);

    public List<Project> findAllProjects();

    public List<Project> filterProjectByCategory(ProjectCategories cat);
}
