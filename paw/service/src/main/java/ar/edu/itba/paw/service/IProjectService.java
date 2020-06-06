package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Order;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Transactional
    public Project create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds) {

        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        return projectDao.create(name, summary, cost, new User(ownerId), categories);
    }

    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }

    @Override
    public List<Project> findByOwnerId(long id) {
        List<FilterCriteria> param = Collections.singletonList(new FilterCriteria("owner", new User(id)));
        return projectDao.findAll(param, OrderField.DEFAULT);
    }

    @Override
    public Page<Project> findAll(Map<String, Object> filters, String order, Integer page, Integer pageSize) {
        /** Clean filters and create Criteria list */
        filters.values().removeIf(value -> (value == null || value.toString().equals("")));
        List<FilterCriteria> params = new ArrayList<>();
        filters.forEach((key, value) -> params.add(new FilterCriteria(key, value)));

        return projectDao.findAll(params, OrderField.getEnum(order), new PageRequest(page, pageSize));
    }

    @Override
    @Transactional
    public Project addHit(long projectId) {
        Optional<Project> optionalProject = findById(projectId);
        if (!optionalProject.isPresent()) return null;
        Project project = optionalProject.get();
        project.setHits(project.getHits() + 1);
        return project;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryDao.findAllCategories();
    }
}

