package com.piramide.elwis.utils;

/**
 * Jatun Team
 * This class implements an algorithm to search a page for a element in a indexed structure
 *
 * @author: alvaro
 * @version: $id: PageSearchUtil, 23-oct-2007 16:13:20
 */
public class PageSearchUtil {


    public PageSearchUtil() {
    }

    /**
     * Return the divider of the number (starts the search in start), can return the same number as num
     *
     * @param num
     * @param start
     * @return
     */
    private int getDivider(int num, int start) {
        int res = 0;
        int i = start;
        while (i <= num) {
            if (num % i == 0) {
                res = i;
                i = num;
            }
            i++;
        }
        return (res);
    }

    /**
     * Calculates the GroupSize in order to get an element (and his successor and predecessor) denoted by an index from
     * a strutcture (with this group or page size the three elements can be retrieves in one page)
     *
     * @param index
     * @return
     */
    public int getGroupSize(int index) {
        int divisor = Integer.MAX_VALUE;
        if (index < 3 || index == 5) {
            divisor = 4;
        } else if (index == 3) {
            divisor = 5;
        } else if (index == 4) {
            divisor = 3;
        } else {
            int search_space_factor = 15;
            for (int i = 2; i <= search_space_factor; i++) {  //explore several results
                if (index - i > i + 1) {    //verify if I can I found a result with this values
                    int divisor_i = getDivider((index - i) + 1, i + 1);
                    if (divisor_i < divisor)   //search the minimum
                    {
                        divisor = divisor_i;
                    }

                    //for time optmization, can result in larger groups, but the algoritm is faster
                    if ((divisor < 5 && i < 7) || (divisor < 8 && i < 10)) {
                        i = search_space_factor;
                    }
                }
            }
        }
        return (divisor);
    }

    /**
     * Returns the element group number (the group that contains the element)
     *
     * @param groupSize
     * @param index
     * @return
     */
    public int getGroup(int groupSize, int index) {
        int res = 0;
        int i = 2;
        if (index <= 3) {
            res = 0;
        } else if (index <= 5) {
            res = 1;
        } else {
            while (index - i > 3) {
                if (((index - i) + 1) % groupSize == 0) {
                    res = index / groupSize;
                    i = index;
                }
                i++;
            }
        }
        if (index <= 5 || res > 0) {
            res++;
        }
        return (res);
    }
}
