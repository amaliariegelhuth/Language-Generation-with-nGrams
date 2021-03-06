/*
Amalia Riegelhuth
amaliariegelhuth

Isabel Bulman
ibulman 
CSCI 1102 Computer Science 2

*/

/* This will be an implementation the Model Interface,
partially implementation of Shannon's n-gram algorithm for
modeling written language.*/

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ModelC implements Model {

  // The member variable map stores the language model.
  // The keys are character sequences of length=order or less.
  // The value for each key will be a list containing every
  // instance of every character that appeared in the text after
  // that key. Remember: the list is *NOT* unique!
  private HashMap<String, ArrayList<String>> map;
  private int order;

  // Constructor for ModelC
  // The constructor takes an input text and
  // an order (i.e., 1 for unigram, 2 for bigram...).
  // It builds a language model of that order from that text.

  public ModelC(int order, String inputText) {

    // Inititalize the member variables.
    this.order = order;
    map = new HashMap<String, ArrayList<String>>();


    /* YOUR CODE GOES HERE! THESE ARE YOUR TASKS:
    * Split the input String into its sequence of characters (as Strings).
    * Initialization step:
    *     Pretend the first character is a dummy empty string "". This is your first key.
    Look at the actual first character. This is your first value.
    Enter that key-value pair into the HashMap, e.g.,
    ArrayList<String> addme = new ArrayList<String>();
    addme.add(inputTextArray[0]);
    map.put("", addme);
    * Next steps:
    Now you need to start proceeding through the input text to
    find the keys (sequences of length order) and their values
    (whatever single character comes next).
    If you have an order 3 or larger, the first few keys you
    process will be smaller than length three.
    Remember: when adding to a HashMap, you first check to see
    whether the key is already in the HashMap. If it is, use get to
    get the value, which is an ArrayList. Add your new value to that
    ArrayList, then use put(key, value) to update the value for that key.
    * Concluding step:
    Your last key will be the last n characters in the
    inputText string, where n=order.
    The value for that key will be the ending dummy sequence "$$$".

    */
    String[] inputTextArray = inputText.split("");
    // add the first value under key that is empty
    ArrayList<String> addme = new ArrayList<String>();
    addme.add(inputTextArray[0]);
    map.put("", addme);
    // go through the input text
    for (int i = 1; i < inputTextArray.length; i++) {
      // will hold key from imput text
      String key = "";
      ArrayList<String> addme1 = new ArrayList<String>();
      int counter = 0;
      /* if the index is less than the order (the key will be less than
      the order) then start the for loop at the 0 index otherwise start it
      before the index by however much the order is */
      if (i < order) {
        counter = 0;
      } else {
        counter = i - order;
      }
      // create the key
      for (int n = counter; n < i; n++){
        key = key + inputTextArray[n];
      }
      // if its not the end, add the value to the array
      // if it is the last element add the signal symbol "$$$"
      if (i < inputTextArray.length - 1) {
        addme1.add(inputTextArray[i]);
      } else {
        addme1.add("$$$");
      }
      /*
      if the map already contains the key, add the value to the already
      existing array
      if it does not then create a new entry in the map
      */
      if (map.containsKey(key)) {
        ArrayList<String> addme2 = new ArrayList<String>();
        addme2.addAll(map.get(key));
        addme2.addAll(addme1);
        map.put(key, addme2);
      } else {
        map.put(key, addme1);
      }

    }
  }
  // YOUR CODE ENDS HERE
  // NOW GO WRITE THE CODE IN LanguageGenerator.java.


  // This method does the work of sampling the next
  // character based on the string or order
  public String sample(String s) {
    ArrayList<String> chars = this.map.get(s);
    int n = chars.size();
    return chars.get(randomInt(n));
  }

  public String toString() { return map.toString(); }

  private static int randomInt(int n) {
    return (int) (Math.random() * n);
  }

  public static void main(String[] args) {

  }
}
