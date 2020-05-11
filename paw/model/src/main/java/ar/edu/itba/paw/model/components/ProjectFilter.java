package ar.edu.itba.paw.model.components;

public class ProjectFilter {

    // Category ids to filter
    private Integer category;

    // Cost filters
    private Integer minCost;
    private Integer maxCost;

    // Search filters
    private String keyword;
    private SearchField searchField;

    // Pagination page
    private Integer page;

    // Sort method
    private ProjectSort sort;

    public ProjectFilter(Integer page) {
        this.page = page;
    }

    public void setCost(String minCost, String maxCost) {
        try {
            this.minCost = Integer.valueOf(minCost);
            this.maxCost = Integer.valueOf(maxCost);
        } catch(NumberFormatException e) {

        }
    }

    public void setSearch(String keyword, String searchField) {
        this.keyword = keyword;
        this.searchField = SearchField.getEnum(searchField);
    }

    public void setCategory(Integer categories) {
        this.category = categories;
    }

    public void setSort(String sort) {
        this.sort = ProjectSort.getEnum(sort);
    }


}
