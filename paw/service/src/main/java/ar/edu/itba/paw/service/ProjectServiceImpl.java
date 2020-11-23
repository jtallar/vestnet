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

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ImageDao imageDao;


    @Override
    @Transactional
    public Project create(String name, String summary, long cost, long ownerId) {
        return projectDao.create(name, summary, cost, new User(ownerId));
    }


    @Override
    @Transactional
    public Optional<Project> update(long id, String name, String summary, long cost) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent()) return optionalProject;
        optionalProject.get().setName(name);
        optionalProject.get().setSummary(summary);
        optionalProject.get().setCost(cost);
        return optionalProject;
    }


    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }


    @Override
    public Page<Project> findAll(Integer category, Integer minCost, Integer maxCost, String keyword, int field, int order, int page, int pageSize) {
        ProjectRequestBuilder builder = new ProjectRequestBuilder()
                .setCategory(category)
                .setCostRange(minCost, maxCost)
                .setFunded(false)
                .setSearch(keyword, field)
                .setOrder(order);

        return projectDao.findAll(builder, new PageRequest(page, pageSize));
    }


    @Override
    @Transactional
    public Optional<Project> addHit(long id) {
        Optional<Project> project = findById(id);
        project.ifPresent(p -> p.setHits(p.getHits() + 1));
        return project;
    }

    @Override
    @Transactional
    public Optional<Project> setFunded(long id) {
        Optional<Project> optionalProject = findById(id);
        optionalProject.ifPresent(p -> p.setFunded(true));
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> addMsgCount(long id) {
        Optional<Project> project = findById(id);
        project.ifPresent(Project::addMsgCount);
        return project;
    }


    @Override
    @Transactional
    public Optional<Project> decMsgCount(long id) {
        Optional<Project> project = findById(id);
        project.ifPresent(Project::decMsgCount);
        return project;
    }


    @Override
    @Transactional
    public Optional<Project> addCategories(long id, List<Category> categories) {
        Optional<Project> optionalProject = projectDao.findById(id);
        optionalProject.ifPresent(p -> p.setCategories(categories));
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> setPortraitImage(long id, byte[] image) {
        Optional<Project> project = projectDao.findById(id);
        project.ifPresent(p -> {
            List<ProjectImage> images = p.getImages();
            images.removeIf(ProjectImage::isMain);
            images.add(new ProjectImage(new Project(id), image, true));
            p.setImages(images);
        });
        return project;
    }


    @Override
    @Transactional
    public Optional<Project> setSlideshowImages(long id, List<byte[]> images) {
        Optional<Project> project = projectDao.findById(id);
        project.ifPresent(p -> {
            List<ProjectImage> imageList = new ArrayList<>();
            images.forEach(i -> imageList.add(new ProjectImage(new Project(id), i, false)));
            p.getImages().stream().filter(ProjectImage::isMain).findFirst().ifPresent(imageList::add);
            p.setImages(imageList);
        });
        return project;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAllCategories();
    }


    @Override
    public Optional<ProjectImage> getPortraitImage(long id) {
        return imageDao.findProjectImages(new Project(id), true).stream().findFirst();
    }


    @Override
    public List<ProjectImage> getSlideshowImages(long id) {
        return imageDao.findProjectImages(new Project(id), false);
    }

}

