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
    private Integer pageLimit = 12;

    // Sort method
    private ProjectSort sort;

    public ProjectFilter(Integer page) {
        this.page = page;
    }

    public void setCost(String minCost, String maxCost) {
        try {
            this.minCost = Integer.valueOf(minCost);
        } catch(NumberFormatException e) {}

        try {
            this.maxCost = Integer.valueOf(maxCost);
        } catch(NumberFormatException e) {}
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

    public boolean isCategory() {
        return category != null && category != 0;
    }

    public Integer getCategory() {
        return category;
    }

    public boolean isMinCost() {
        return minCost != null;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public boolean isMaxCost() {
        return maxCost != null;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public boolean isSearch() {
        return keyword != null && !keyword.isEmpty();
    }

    public String getKeyword() {
        return keyword;
    }

    public SearchField getSearchField() {
        return searchField;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageLimit() {
        return pageLimit;
    }

    public ProjectSort getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return "ProjectFilter{" +
                "category=" + category +
                ", minCost=" + minCost +
                ", maxCost=" + maxCost +
                ", keyword='" + keyword + '\'' +
                ", searchField=" + searchField +
                ", page=" + page +
                ", pageLimit=" + pageLimit +
                ", sort=" + sort +
                '}';
    }
}
