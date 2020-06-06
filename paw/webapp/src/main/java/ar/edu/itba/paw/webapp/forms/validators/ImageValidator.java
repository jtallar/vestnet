package ar.edu.itba.paw.webapp.forms.validators;

import ar.edu.itba.paw.webapp.forms.validators.ImageFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<ImageFile, MultipartFile> {
    private static final String ERROR_FILE_TYPE = "{ar.edu.itba.paw.webapp.forms.validator.ImageValidator.image.type}";
    private static final String ERROR_FILE_SIZE = "{ar.edu.itba.paw.webapp.forms.validator.ImageValidator.image.size}";
    private long maxSize;

    @Override
    public void initialize(ImageFile imageFile) {
        this.maxSize = imageFile.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        // No file attached
        if (multipartFile == null || multipartFile.isEmpty())  {
            return true;
        }
        // Check if size exceeds limit
        if (multipartFile.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ERROR_FILE_SIZE).addConstraintViolation();
            return false;
        }
        // Check if file is an image
        if (multipartFile.getContentType().startsWith("image")) {
            return true;
        }
        // File is not an image
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ERROR_FILE_TYPE).addConstraintViolation();
        return false;
    }
}