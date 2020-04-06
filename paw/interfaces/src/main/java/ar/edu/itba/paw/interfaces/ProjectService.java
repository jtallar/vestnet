package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    public Project create(String name, String summary, int ownerId, Date date);

    public Optional<Project> findById(long id);

    public List<Project> findByName(String name);

    public List<Project> findAllProjects();
}
