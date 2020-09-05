package com.budgetfirst.financialapp.utils;

import java.util.ArrayList;
import java.util.Collections;

public class UtilRemoveDuplicates {

    public static void removeDuplicatesInList(ArrayList<String> list) {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int k = list.size() - 1; k > i; k--) {
                if (list.get(i).equals(list.get(k))) {
                    list.remove(k);
                }
            }
        }
    }

    public static void removeDuplicatesMulti(ArrayList<String> arrayList,
                                          ArrayList<String> arrayListTwo,
                                          ArrayList<String> arrayListThree) {

        for (int i = 0; i < arrayList.size() - 1; i++) {
            for (int k = arrayList.size() - 1; k > i; k--) {
                if (arrayList.get(i).equals(arrayList.get(k))) {
                    arrayList.remove(k);
                    arrayListTwo.remove(k);
                    arrayListThree.remove(k);
                }
            }
        }
    }

    public static ArrayList<Integer> removeDuplicatesAndSortData(ArrayList<String> list) {

        removeDuplicatesInList(list);

        ArrayList<Integer> dateList = new ArrayList<>();
        for (String elem: list) {
            dateList.add(Integer.parseInt(UtilConverter.dateFormatYear(Long.parseLong(elem))));
        }

        Collections.sort(dateList);

        return dateList;
    }
}
