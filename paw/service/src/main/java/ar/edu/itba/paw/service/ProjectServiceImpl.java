package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.CategoryDao;
import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.interfaces.daos.ProjectDao;
import ar.edu.itba.paw.interfaces.exceptions.IllegalProjectAccessException;
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
    public Project create(String name, String summary, long fundingTarget, List<Category> categories, long ownerId) {
        final Project project = projectDao.create(name, summary, fundingTarget, new User(ownerId));
        project.setCategories(categories);
        return project;
    }


    @Override
    @Transactional
    public Optional<Project> update(long ownerId, long id, String name, String summary, long fundingTarget) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        /** Not the owner of the found project */
        if (optionalProject.isPresent() && optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();

        optionalProject.ifPresent(p -> {
            p.setName(name);
            p.setSummary(summary);
            p.setFundingTarget(fundingTarget);
            p.setUpdateDate(new Date());
        });
        return optionalProject;
    }


    @Override
    public Optional<Project> findById(long id) {
        return projectDao.findById(id);
    }


    @Override
    public Page<Project> findAll(Integer category, Integer minFundingTarget, Integer maxFundingTarget, String keyword, int field, int order, int page, int pageSize) {
        final RequestBuilder request = new ProjectRequestBuilder()
                .setCategory(category)
                .setFundingTargetRange(minFundingTarget, maxFundingTarget)
                .setClosed(false)
                .setSearch(keyword, field)
                .setOrder(order);

        return projectDao.findAll(request, new PageRequest(page, pageSize));
    }


    @Override
    @Transactional
    public Optional<Project> setClosed(long ownerId, long id) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        /** Not the owner of the found project */
        if (optionalProject.isPresent() && optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();

        optionalProject.ifPresent(p -> p.setClosed(true));
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> addCategories(long ownerId, long id, List<Category> categories) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        /** Not the owner of the found project */
        if (optionalProject.isPresent() && optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();

        optionalProject.ifPresent(p -> p.setCategories(categories));
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> getStats(long id) {
        final Optional<Project> optionalProject = projectDao.findById(id);
        optionalProject.ifPresent(p -> {
            if (p.getStats() == null) p.setStats(new ProjectStats(true));
        });
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> addStats(long id, long seconds, long clicks, boolean investor, boolean contact) {
        final Optional<Project> optionalProject = projectDao.findById(id);
        optionalProject.ifPresent(p -> {
            if (p.getStats() == null) p.setStats(new ProjectStats(true));
            p.getStats().setNewSeen(seconds, clicks, investor, contact);
            p.setRelevance(p.getStats().getRelevance());
        });
        return optionalProject;
    }


    @Override
    @Transactional
    public Optional<Project> setStage(long ownerId, long id, String name, String comment) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        /** Not the owner of the found project */
        if (optionalProject.isPresent() && optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();;

        /** Adds the new stage if there are less than 5 */
        optionalProject.ifPresent(p -> {
            final Set<ProjectStages> stages = p.getStages();
            if (stages.size() < Project.MAX_STAGES)
                stages.add(new ProjectStages(name, stages.size() + 1, comment, p));
        });
        return optionalProject;
    }


    @Override
    public Optional<ProjectImage> getPortraitImage(long id) {
        return imageDao.findProjectImages(new Project(id), true).stream().findFirst();
    }


    @Override
    @Transactional
    public Optional<Project> setPortraitImage(long ownerId, long id, byte[] image) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        /** Not the owner of the found project */
        if (optionalProject.isPresent() && optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();

        optionalProject.ifPresent(p -> {
            final Set<ProjectImage> images = p.getImages();
            images.removeIf(ProjectImage::isMain);
            images.add(new ProjectImage(new Project(id), image, true));
            p.setImages(images);
        });
        return optionalProject;
    }


    @Override
    public List<ProjectImage> getSlideshowImages(long id) {
        return imageDao.findProjectImages(new Project(id), false);
    }


    @Override
    @Transactional
    public Optional<Project> setSlideshowImages(long ownerId, long id, List<byte[]> images) throws IllegalProjectAccessException {
        final Optional<Project> optionalProject = projectDao.findById(id);

        if (!optionalProject.isPresent()) return Optional.empty();

        /** Not the owner of the found project */
        if (optionalProject.get().getOwnerId() != ownerId) throw new IllegalProjectAccessException();

        final Set<ProjectImage> projectImages = optionalProject.get().getImages();

        /** Checks if there is a Portrait, main image. If not, returns optional empty */
        if (projectImages.stream().noneMatch(ProjectImage::isMain)) return Optional.empty();

        /** If there is a main image, add all the new slideshow images to the project */
        projectImages.removeIf(ProjectImage::isNotMain);
        images.forEach(i -> projectImages.add(new ProjectImage(new Project(id), i, false)));

        return optionalProject;
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAllCategories();
    }
}

