package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Models.Movie;

import java.util.ArrayList;
import java.util.List;

public class AbstractMovieListDTO {

    private String username;

    private String url;

    private int firstElement;

    private long movieCount;

    private int currentPage;

    private int pages;

    private List<Integer> pageNumbers = new ArrayList<>();

    private boolean isUserList;

    private String sortOption;

    private String searchTerm;

    private String searchOption;

    private int previousSize;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(long movieCount) {
        this.movieCount = movieCount;
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

    public boolean isUserList() {
        return isUserList;
    }

    public void setUserList(boolean userList) {
        isUserList = userList;
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
