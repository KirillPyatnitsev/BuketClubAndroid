package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 30/11/15.
 */
public class Pagination {
    @JsonProperty(Fields.CURRENT_PAGE)
    private int currentPage;

    @JsonProperty(Fields.NEXT_PAGE)
    private int nextPage;

    @JsonProperty(Fields.PREV_PAGE)
    private int prevPage;

    @JsonProperty(Fields.TOTAL_PAGES)
    private int totalPages;

    @JsonProperty(Fields.TOTAL_COUNT)
    private int totalCount;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
