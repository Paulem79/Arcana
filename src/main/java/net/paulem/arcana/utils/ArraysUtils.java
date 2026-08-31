package net.paulem.arcana.utils;

import net.paulem.arcana.math.FastRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with arrays and collections, such as picking random
 * elements, computing differences, excluding elements, and reversing arrays.
 */
public class ArraysUtils {
    /**
     * Picks a random element from the given array.
     *
     * @param array the array to pick from
     * @param <T> the type of the elements
     * @return a randomly chosen element of the array
     */
    public static <T> T getRandom(T[] array) {
        int rnd = FastRandom.INSTANCE.nextInt(array.length);
        return array[rnd];
    }

    /**
     * Picks a random element from the given list.
     *
     * @param list the list to pick from
     * @param <T> the type of the elements
     * @return a randomly chosen element of the list
     */
    public static <T> T getRandom(List<T> list) {
        int rnd = FastRandom.INSTANCE.nextInt(list.size());
        return list.get(rnd);
    }

    /**
     * Picks a random element from the given collection.
     * The collection is copied into a list before an element is chosen.
     *
     * @param collection the collection to pick from
     * @param <T> the type of the elements
     * @return a randomly chosen element of the collection
     */
    public static <T> T getRandom(Collection<T> collection) {
        return getRandom(new ArrayList<>(collection));
    }

    /**
     * Computes the set difference between two lists, i.e. the elements of {@code a}
     * that are not present in {@code b}.
     *
     * @param a the list to filter
     * @param b the list of elements to exclude
     * @param <T> the type of the elements
     * @return a new list containing the elements of {@code a} that are not in {@code b}
     */
    public static <T> List<T> difference(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a);
        result.removeAll(b);
        return result;
    }

    /**
     * Creates a copy of the given array with all elements contained in {@code toExclude} removed.
     *
     * @param array the array to filter
     * @param toExclude the elements to exclude from the result
     * @param <T> the type of the elements
     * @return a new array containing only the elements of {@code array} that are not in {@code toExclude}
     */
    public static <T> T[] excludeElements(T[] array, Collection<T> toExclude) {
        List<T> result = new ArrayList<>();
        for (T element : array) {
            if (!toExclude.contains(element)) {
                result.add(element);
            }
        }
        @SuppressWarnings("unchecked")
        T[] excluded = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), result.size());
        return result.toArray(excluded);
    }

    /**
     * Reverses the given array in place.
     *
     * @param array the array to reverse
     * @param <T> the type of the elements
     * @return the same array instance, with its elements in reversed order
     */
    public static <T> T[] invertArray(T[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return array;
    }
}
