package org.launchcode.watchlist.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PagingService {

    // Used for pagination. Probably need to be put into a pagination service.
    public int findFirstPageNum(int currentPage, int pages){
        int halfOfMaxDisplayedPages = 5;
        int difference = currentPage - 0;

        if (pages <= 10 || difference < halfOfMaxDisplayedPages){
            return 0;
        }

        return currentPage - halfOfMaxDisplayedPages;
    }

    public List<Integer> getDisplayedPageNumbers(int currentPage, int totalPages){
        List<Integer> pageNumbers = new ArrayList<>();
        int firstPage = findFirstPageNum(currentPage, totalPages);
        int lastPage = totalPages -1;

        if (lastPage - firstPage > 9){
            lastPage = firstPage + 9;
        }

        for (int i = firstPage; i <= lastPage; i++){
            pageNumbers.add(i);
        }

        Collections.sort(pageNumbers);

        return pageNumbers;
    }
}
