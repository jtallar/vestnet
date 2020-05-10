package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class SearchFilter {

    private String searching;
    private String selection;

    public String getSearching() {
        return searching;
    }

    public void setSearching(String input) {
        this.searching = input;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String select) {
        this.selection = select;
    }



}
