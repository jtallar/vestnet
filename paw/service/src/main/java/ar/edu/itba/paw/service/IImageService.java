package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.daos.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Project;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.image.ProjectImage;
import ar.edu.itba.paw.model.image.UserImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IImageService implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    @Transactional
    public ProjectImage create(long id, byte[] image, boolean main) {
        return imageDao.create(new Project(id), image, main);
    }

    @Override
    @Transactional
    public UserImage create(byte[] image) {
        return imageDao.create(image);
    }

    @Override
    public Optional<UserImage> findUserImage(long id) {
        return imageDao.findUserImage(id);
    }

    @Override
    public Optional<ProjectImage> findProjectMain(long id) {
        return imageDao.findProjectMain(new Project(id));
    }

    @Override
    public List<ProjectImage> findProjectAll(long id) {
        return imageDao.findProjectAll(new Project(id));
    }
}
