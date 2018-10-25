/*
Amalia Riegelhuth
amaliariegelhuth

CSCI 1102 Computer Science 2

*/
import java.util.*;
import java.io.*;

public class LanguageGenerator {

  public static void main (String[] args) throws IOException {

    // This class will build a ModelC langauge model object using
    // a file and an order, n, (from 1 to 5) supplied on the command line
    // by the user. It will then
    // generate langauge from that language model

    // Get the command line arguments
    String fileName = args[0];
    int n = Integer.parseInt(args[1]);

    // Open the file and read it into a string. This is complicated.
    // Make sure to give it a nice plain text file and not a Word doc or rtf.
    File f = new File(fileName);
    FileInputStream fin = new FileInputStream(f);
    byte[] buffer = new byte[(int) f.length()];
    new DataInputStream(fin).readFully(buffer);
    fin.close();
    String myInputText = new String(buffer, "UTF-8").replaceAll("[\\t\\n\\r]+"," ");

    // This just shows you what is in your text file, for fun.
    System.out.println(myInputText);

    /* YOUR CODE BEGINS HERE!
    * Create an instance of ModelC with your input text and the order, n.

    * Generate language from that language model as follows:
    * Start with the empty string "" as your starting generated string.
    * While the next character sampled is not the end punctuation or ^^^,
    * append it to your empty string. Then sample the next character using
    * the last n characters of your generated string.
    * Print the generated string to the screen.

    * Generate 5 sentences for each order from 1 to 5, and turn those in along
    * with the input text and your code.

    YOUR CODE ENDS HERE! */
    ModelC model = new ModelC(n, myInputText);
    String lang = ""; //string to be built on and output
    String next = ""; //the next to characters to take a sample of
    String nextSample = model.sample(next);//the next sample to be added to the output string
    while (!nextSample.equals(".") && !nextSample.equals("!") && !nextSample.equals("?") && !nextSample.equals("$$$")){ //check to make sure the next character sampled is not punctuation or end of file
      lang = lang + nextSample; //add the next sampled character onto the output string
      if (next.length() < n){ //if the next string to be sampled is shorter than the order, keep using the output string as next
        next = lang;
      }else{
      next = lang.substring(lang.length() -  n ); //if not, find the new next by taking the last n characters from the output string
    }
      nextSample = model.sample(next); //get the next sample using the sample method on next

    }
    if (nextSample != "$$$"){ //if the next sample which ended the loop is end of text, don't put it on the end, if it's puctuation, add it to the end
      lang = lang + nextSample;
    }

    System.out.println(lang); //print out the output string

  }


}
