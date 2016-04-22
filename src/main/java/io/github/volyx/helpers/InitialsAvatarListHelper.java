package io.github.volyx.helpers;

import java.util.Collections;
import java.util.List;

/**
 * The Class InitialsAvatarListHelper.
 */
public class InitialsAvatarListHelper {
  /**
   * Gets a random int starting from one.
   * 
   * @param len
   *          the len
   * @return the a random
   */
  private static int getARandom(final int len) {
    // We start in 1 because names[0] is already choosed
    return RandomHelper.getInt(1, len - 1);
  }

  /**
   * Gets four elements of the list.
   * 
   * @param names
   *          the names
   * @return the four and swap
   */
  public static <V> List<V> getFour(final List<V> names) {
    return names.subList(0, getMaxSize(names));
  }

  /**
   * With lists of more than four elements, gets only four elements, and gets
   * randomly three maintaining the first (the author).
   * 
   * @param objects
   *          the names
   * @return the four and swap
   */
  public static <V> List<V> getFourAndSwap(final List<V> objects) {
    if (objects.size() > 4) {
      return swap(objects).subList(0, getMaxSize(objects));
    } else {
      return objects;
    }
  }

  public static int getMaxSize(final List<?> list) {
    return list.size() > 4 ? 4 : list.size();
  }

  /**
   * Swap the last three elements of a list.
   * 
   * @param <V>
   * 
   * @param list
   *          the list
   * @return the list
   */
  private static <V> List<V> swap(final List<V> list) {
    // We maintain the first in the first position
    for (int index = 1; index < list.size(); index++) {
      Collections.swap(list, index, getARandom(list.size()));
    }
    return list;
  }

}
