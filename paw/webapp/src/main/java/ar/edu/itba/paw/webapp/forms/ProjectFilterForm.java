package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Pattern;

@RangeCheck()
public class ProjectFilterForm {

    private Integer categoryId;

    private String orderBy;

    @Pattern(regexp = "^[0-9]*")
    private String minCost;
    @Pattern(regexp = "^[0-9]*")
    private String maxCost;

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
