package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.services.ProjectService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.components.*;
import ar.edu.itba.paw.model.image.ProjectImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Project create(String name, String summary, long fundingTarget, long ownerId) {
        return projectDao.create(name, summary, fundingTarget, new User(ownerId));
    }


    @Override
    @Transactional
    public Optional<Project> update(long ownerId, long id, String name, String summary, long fundingTarget) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent() || optionalProject.get().getOwnerId() != ownerId) return Optional.empty();

        optionalProject.get().setName(name);
        optionalProject.get().setSummary(summary);
        optionalProject.get().setFundingTarget(fundingTarget);
        return optionalProject;
    }


    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }


    @Override
    public Page<Project> findAll(Integer category, Integer minFundingTarget, Integer maxFundingTarget, String keyword, int field, int order, int page, int pageSize) {
        RequestBuilder request = new ProjectRequestBuilder()
                .setCategory(category)
                .setFundingTargetRange(minFundingTarget, maxFundingTarget)
                .setClosed(false)
                .setSearch(keyword, field)
                .setOrder(order);

        return projectDao.findAll(request, new PageRequest(page, pageSize));
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
    public Optional<Project> setClosed(long ownerId, long id) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent() || optionalProject.get().getOwnerId() != ownerId) return Optional.empty();
        optionalProject.get().setClosed(true);
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> addCategories(long ownerId, long id, List<Category> categories) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent() || optionalProject.get().getOwnerId() != ownerId) return Optional.empty();
        optionalProject.get().setCategories(categories);
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> setPortraitImage(long ownerId, long id, byte[] image) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent() || optionalProject.get().getOwnerId() != ownerId) return Optional.empty();

        Set<ProjectImage> images = optionalProject.get().getImages();
        images.removeIf(ProjectImage::isMain);
        images.add(new ProjectImage(new Project(id), image, true));
        optionalProject.get().setImages(images);
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> setSlideshowImages(long ownerId, long id, List<byte[]> images) {
        Optional<Project> optionalProject = projectDao.findById(id);
        if (!optionalProject.isPresent() || optionalProject.get().getOwnerId() != ownerId) return Optional.empty();

        Set<ProjectImage> imageList = new HashSet<>();
        images.forEach(i -> imageList.add(new ProjectImage(new Project(id), i, false)));
        optionalProject.get().getImages().stream().filter(ProjectImage::isMain).findFirst().ifPresent(imageList::add);
        optionalProject.get().setImages(imageList);
        return optionalProject;
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

