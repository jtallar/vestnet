package ar.edu.itba.paw.model.components;

public class PageRequest {
    private static final int FIRST_PAGE = 1;

    private int page;
    private int pageSize;

    public PageRequest(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageRequest() {
        this.page = FIRST_PAGE;
        this.pageSize = Integer.MAX_VALUE;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getFirstResult() {
        return (page - FIRST_PAGE) * pageSize;
    }
}
