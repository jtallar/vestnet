package ar.edu.itba.paw.model.components;

import java.util.List;

/**
 * Intends to simulate Spring Data Page<T> and Slice<T>
 * Holds the result of the executed query and
 * also page information
 * @param <T>
 */
public class Page<T> {

    private static final int FIRST_PAGE = 1;
    private static final int DEFAULT_RANGE = 5;

    private List<T> content;
    private long totalCount;

    /** View pages resources */
    private long totalPages;
    private long pageRange;
    private long pageSize;

    private long currentPage;
    private long startPage;
    private long endPage;

    public Page(List<T> content, long currentPage, long pageSize, long totalCount) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPages = (long) Math.ceil((double) totalCount / (double) pageSize);
        setPageRange(DEFAULT_RANGE);
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getStartPage() {
        return startPage;
    }

    public long getEndPage() {
        return endPage;
    }

    public void setPageRange(long pageRange) {
        this.pageRange = pageRange;
        setStartEndPage();
    }

    public boolean hasNext() {
        return this.currentPage < this.totalPages;
    }

    public boolean hasPrevious() {
        return this.currentPage > FIRST_PAGE;
    }

    @Override
    public String toString() {
        return "Page{" +
//                "content=" + content +
                ", totalCount=" + totalCount +
                ", totalPages=" + totalPages +
                ", pageRange=" + pageRange +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                '}';
    }

    /**
     * Auxiliary functions
     */


    /**
     * Sets the start and end pages
     */
    private void setStartEndPage() {
        /** Total pages within range */
        if (totalPages <= pageRange) {
            this.startPage = FIRST_PAGE;
            this.endPage = totalPages == 0 ? 1: totalPages;
            return;
        }

        /** If current page is on the left part of the range */
        long firstPage = currentPage - pageRange / 2;
        if (firstPage <= FIRST_PAGE ) {
            this.startPage = FIRST_PAGE;
            this.endPage = pageRange;
            return;
        }

        /** If current page is range centered */
        long lastPage = currentPage + pageRange / 2;
        if (lastPage <= totalPages) {
            this.startPage = firstPage;
            this.endPage = lastPage;
            return;
        }

        /** If current page is on the right part of the range */
        this.startPage = totalPages - pageRange;
        this.endPage =  totalPages;
    }
}
