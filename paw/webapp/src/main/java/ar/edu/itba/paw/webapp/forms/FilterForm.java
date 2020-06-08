package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

public class FilterForm {

    private Integer category;

    @Pattern(regexp = "^[0-9]{0,7}")
    private String minCost;

    @Pattern(regexp = "^[0-9]{0,7}")
    private String maxCost;

    @Size(max = 50)
    private String keyword;

    private String field;

    private String order;


    /** Getters and setters */


    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


    public Map<String, Object> getFiltersMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", getCategory());
        map.put("minCost", getMinCost());
        map.put("maxCost", getMaxCost());
        map.put(getField(), getKeyword());
        return map;
    }

    @Override
    public String toString() {
        return "FilterForm{" +
                "category=" + category +
                ", order='" + order + '\'' +
                ", minCost='" + minCost + '\'' +
                ", maxCost='" + maxCost + '\'' +
                ", keyword='" + keyword + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
