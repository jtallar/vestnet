package ar.edu.itba.paw.webapp.forms;

import cz.jirutka.validator.spring.SpELAssert;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

@RangeCheck()
public class CategoryFilter {
    String categorySelector;
    String orderBy;




    @Pattern(regexp = "[0-9]*")
    String min;
    @Pattern(regexp = "[0-9]*")
    String max;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

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
