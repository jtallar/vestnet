package ar.edu.itba.paw.webapp.forms;

public class CategoryFilter {
    String categorySelector;
    String orderBy;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCategorySelector() {
        return categorySelector;
    }

    public void setCategorySelector(String categorySelector) {
        this.categorySelector = categorySelector;
    }
}
