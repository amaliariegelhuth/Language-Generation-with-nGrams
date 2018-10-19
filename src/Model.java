/* file: Model.java
author: Bob Muller

CSCI 1102 Computer Science 2

The Model API, a part of an implementation of C. Shannon's
n-gram algorithm for modeling English text.
*/

public interface Model {
  Character sample(String key);
  String toString();
}
