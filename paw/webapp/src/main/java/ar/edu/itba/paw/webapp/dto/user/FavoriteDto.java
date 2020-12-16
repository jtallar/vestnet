package ar.edu.itba.paw.webapp.dto.user;

import ar.edu.itba.paw.model.Favorite;

import javax.validation.constraints.NotNull;

public class FavoriteDto {

    @NotNull
    private Long projectId;

    public static FavoriteDto fromFavorite(Favorite favorite) {
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProjectId(favorite.getProjectId());
        return  favoriteDto;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
