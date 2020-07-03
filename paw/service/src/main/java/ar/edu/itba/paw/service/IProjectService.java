package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.image.Image;
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
    public Project create(String name, String summary, long cost, long ownerId,
                          List<Long> categoriesIds, byte[] image, List<byte[]> slideshow) {

        List<Category> categories = categoriesIds.stream().map(Category::new).collect(Collectors.toList());
        Project newProject = projectDao.create(name, summary, cost, new User(ownerId), categories);
        if (image.length > 0) imageDao.create(newProject, image, true);
        slideshow.removeIf(bytes -> bytes.length == 0);
        slideshow.forEach(bytes -> imageDao.create(newProject, bytes, false));
        return newProject;
    }


    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }


    @Override
    public Page<Project> findAll(Integer category, Integer minCost, Integer maxCost, String keyword, int field, int order, int page, int pageSize) {
        List<FilterCriteria> params = new ArrayList<>();

        if (category != null) params.add(new FilterCriteria("category", category));
        if (minCost != null) params.add(new FilterCriteria("minCost", minCost));
        if (maxCost != null) params.add(new FilterCriteria("maxCost", maxCost));
        if (keyword != null && !keyword.equals("")) params.add(new FilterCriteria(String.valueOf(field), keyword));
        params.add(new FilterCriteria("funded", false));

        return projectDao.findAll(params, OrderField.getEnum(String.valueOf(order)), new PageRequest(page, pageSize));
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
    @Transactional
    public Project setFunded(long projectId) {
        Optional<Project> optionalProject = findById(projectId);
        if (!optionalProject.isPresent()) return null;
        Project project = optionalProject.get();
        project.setFunded(true);
        return project;
    }


    @Override
    @Transactional
    public Project addMsgCount(long projectId) {
        Optional<Project> optionalProject = findById(projectId);
        if (!optionalProject.isPresent()) return null;
        Project project = optionalProject.get();
        project.addMsgCount();
        return project;
    }


    @Override
    @Transactional
    public Project decMsgCount(long projectId) {
        Optional<Project> optionalProject = findById(projectId);
        if (!optionalProject.isPresent()) return null;
        Project project = optionalProject.get();
        project.decMsgCount();
        return project;
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAllCategories();
    }


    @Override
    public byte[] getPortraitImage(long id) {
        Optional<ProjectImage> optionalImage = imageDao.findProjectImages(new Project(id), true).stream().findFirst();
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
    public List<byte[]> getSlideshowImages(long id) {
        List<ProjectImage> images = imageDao.findProjectImages(new Project(id), false);
        return images.stream().map(Image::getImage).collect(Collectors.toList());
    }

}

