package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project create(String name, String summary, long cost, byte[] image, long ownerId, List<Long> categoriesIds) {

        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        return projectDao.create(name, summary, cost, image, new User(ownerId), categories);
    }

    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }

    @Override
    public List<Project> findByOwnerId(long id) {
        List<FilterCriteria> param = Collections.singletonList(new FilterCriteria("owner", new User(id)));
        return projectDao.findAll(param, OrderField.DEFAULT, new PageRequest()).getContent();
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
    public void addHit(long projectId) {
        projectDao.addHit(projectId);
    }

    @Override
    public List<Category> findAllCategories() {
        return projectDao.findAllCategories();
    }
}

