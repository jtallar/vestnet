package ar.edu.itba.paw.webapp.forms.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageListValidator implements ConstraintValidator<ImageFile, List<MultipartFile>> {
    private static final String ERROR_FILE_COUNT = "{ar.edu.itba.paw.webapp.forms.validators.ImageListValidator.image.count}";
    private ImageValidator validator;
    private int maxCount;

    @Override
    public void initialize(ImageFile imageFile) {
        this.maxCount = imageFile.maxCount();
        this.validator = new ImageValidator();
        this.validator.initialize(imageFile);
    }

    @Override
    public boolean isValid(List<MultipartFile> multipartFiles, ConstraintValidatorContext context) {
        // Check if size exceeds limit
        if (multipartFiles.size() > maxCount) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ERROR_FILE_COUNT).addConstraintViolation();
            return false;
        }
        // Check if files are images
        for (MultipartFile file : multipartFiles) {
            if (!validator.isValid(file, context))
                return false;
        }
        return true;
    }
}
