package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project create(String name, String summary, int ownerId, Date date);

    Optional<Project> findById(long id);

    List<Project> findByName(String name);
}
