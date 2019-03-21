package com.umobilized.helpers.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tpaczesny on 2017-10-20.
 */

public class CollectionsUtils {

    public static <T> ArrayList<T> removeIf(Collection<T> collection, Predicate<T> predicate) {
        ArrayList<T> toRemove = new ArrayList<>();

        for (T item : collection) {
            if (predicate.test(item)) {
                toRemove.add(item);
            }
        }
        collection.removeAll(toRemove);
        return toRemove;
    }

    public static <T> List<T> subListIf(Collection<T> collection, Predicate<T> predicate) {
        ArrayList<T> subList = new ArrayList<>();

        for (T item : collection) {
            if (predicate.test(item)) {
                subList.add(item);
            }
        }
        return subList;
    }

    public static <T> T findFirstIf(Collection<T> collection, Predicate<T> predicate) {
        for (T item : collection) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static <T> T findMin(Collection<T> collection, Value<T> valuator) {
        T minItem = null;
        for (T item : collection) {
            if (minItem == null) {
                minItem = item;
            } else {
                if (valuator.value(item) < valuator.value(minItem)) {
                    minItem = item;
                }
            }
        }
        return minItem;
    }

    public static <T> T findMax(Collection<T> collection, Value<T> valuator) {
        T maxItem = null;
        for (T item : collection) {
            if (maxItem == null) {
                maxItem = item;
            } else {
                if (valuator.value(item) > valuator.value(maxItem)) {
                    maxItem = item;
                }
            }
        }
        return maxItem;
    }

    public static <T,P, RESULT extends Collection<P>> RESULT map(Collection<T> collection, RESULT resultColleciton, Mapper<T,P> mapper)  {
        for (T item : collection) {
            resultColleciton.add(mapper.map(item));
        }
        return resultColleciton;
    }

    public interface Predicate<T> {
        boolean test(T object);
    }

    public interface Value<T> {
        int value(T object);
    }

    public interface Mapper<T,P> {
        P map(T object);
    }
}
