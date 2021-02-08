package ar.edu.itba.paw.webapp.dto.location;

import ar.edu.itba.paw.model.location.State;

public class StateDto {

    private int id;
    private String name;
    private String isoCode;

    public static StateDto fromState(State state) {
        final StateDto stateDto = new StateDto();

        stateDto.setId(state.getId());
        stateDto.setName(state.getName());
        stateDto.setIsoCode(state.getIsoCode());

        return stateDto;
    }

    /* Getters and setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}
