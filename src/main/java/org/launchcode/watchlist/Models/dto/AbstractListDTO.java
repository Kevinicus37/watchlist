package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Enums.PageSize;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListDTO {

    private final int firstPage = 0;

    private PageSize[] pageSizes = PageSize.values();

    private int firstElement;

    private int currentPage;

    private String url;

    private int pages;

    private List<Integer> pageNumbers = new ArrayList<>();

    private String sortOption;

    private String searchTerm;

    private String searchOption;

    private int previousSize;

    public int getFirstPage() {
        return firstPage;
    }

    public PageSize[] getPageSizes() {
        return pageSizes;
    }

    public void setPageSizes(PageSize[] pageSizes) {
        this.pageSizes = pageSizes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(int firstElement) {
        this.firstElement = firstElement;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public String getSortOption() {
        return sortOption;
    }

    public void setSortOption(String sortOption) {
        this.sortOption = sortOption;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public int getPreviousSize() {
        return previousSize;
    }

    public void setPreviousSize(int previousSize) {
        this.previousSize = previousSize;
    }


}
