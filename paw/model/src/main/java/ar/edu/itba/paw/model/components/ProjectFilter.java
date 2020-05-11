package ar.edu.itba.paw.model.components;

public class ProjectFilter {

    // Categories ids to filter
    private Integer categories;

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

    public ProjectFilter(Integer categories, Integer minCost, Integer maxCost, String keyword, SearchField searchField, Integer page) {
        this.categories = categories;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.keyword = keyword;
        this.searchField = searchField;
        this.page = page;
    }

    public ProjectFilter(Integer page, String keyword, SearchField searchField) {
        this.sort = ProjectSort.DEFAULT;
        this.page = page;
        this.keyword = keyword;
        this.searchField = searchField;
    }

    public ProjectFilter() {

    }

    public Integer getCategories() {
        return categories;
    }

    public void setCategories(Integer categories) {
        this.categories = categories;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public void setMinCost(Integer minCost) {
        this.minCost = minCost;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Integer maxCost) {
        this.maxCost = maxCost;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SearchField getSearchField() {
        return searchField;
    }

    public void setSearchField(SearchField searchField) {
        this.searchField = searchField;
    }

    public ProjectSort getSort() {
        return sort;
    }

    public void setSort(ProjectSort sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
