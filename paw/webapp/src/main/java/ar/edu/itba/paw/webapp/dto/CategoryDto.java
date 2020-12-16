package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Category;

import javax.validation.constraints.NotNull;

public class CategoryDto {

    @NotNull
    private Long id;
    private String name;
    private Long parent;

    public static CategoryDto fromCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setParent(category.getParent());
        return categoryDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
}
