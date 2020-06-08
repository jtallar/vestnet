package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.image.ProjectImage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ImageDao imageDao;


    @Override
    @Transactional
    public Project create(String name, String summary, long cost, long ownerId, List<Long> categoriesIds, byte[] image) {

        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        Project newProject = projectDao.create(name, summary, cost, new User(ownerId), categories);
        if (image.length > 0) imageDao.create(newProject, image, true);
        return newProject;
    }


    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
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
    public List<Category> getAllCategories() {
        return categoryDao.findAllCategories();
    }


    @Override
    public byte[] getPortraitImage(long id) {
        Optional<ProjectImage> optionalImage = imageDao.findProjectMain(new Project(id));
        if (optionalImage.isPresent()) return optionalImage.get().getImage();

        byte[] image;
        try {
            Resource stockImage = new ClassPathResource("projectNoImage.png");
            image = IOUtils.toByteArray(stockImage.getInputStream());
        } catch (IOException e) {
            return new byte[0];
        }
        return image;
    }


    @Override
    public List<ProjectImage> getAllImages(long id) {
        return imageDao.findProjectAll(new Project(id));
    }
}

