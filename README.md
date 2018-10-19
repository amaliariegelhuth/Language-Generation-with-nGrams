## Problem Set 6: Language generation with n-grams

### Due Friday, October 26 @ 11:59pm

[Click here for an up-to-date version of this README](https://github.com/BC-CSCI-1102-F18-MW/ps6/blob/master/README.md) 

---

This is a pair problem set. Find one partner to work with. If you don't know anyone yet, email me, and I will try match you up with someone else who is also looking for a partner. **Please identify both team members in the comments at the top of your files.**

---
## Background
### Introduction
In this assignment, you will write a program that generates novel text given an example input text. If the input is English text, then the program will output English text. If the input is French, then the output will be  French. If the input is a python program, then the output will be a python program. If the input is a genetic sequence, then the output will be a new genetic sequence. The generated text will not be perfect, but it will "look like" the input text because we will make sure it has the same statistical (i.e., distributional) properties.

After class on Monday, you will know a bit about n-gram language modeling, which is used in a wide variety of AI applications including automatic speech recognition, text mining, and natural language generation. The reason n-gram language models work is that language is not random: you can predict what word or letter is going to come next based on the previous words or letters. 

This algorithm for building n-gram models and generating language from them was first proposed in Claude Shannon's landmark 1948 paper on information theory [A Mathematical Theory of Communication](http://cm.bell-labs.com/cm/ms/what/shannonday/shannon1948.pdf). The basic idea involves stochastic processes that were first explored by [Andrei Markov](http://en.wikipedia.org/wiki/Andrey_Markov) decades earlier.

### Character n-gram language models 
In class, we'll be talking about both word n-gram models and character n-gram models. In this problem set, we'll be focusing on character-level n-gram models. Here's an intuition of how it works:

Suppose you want to generate a new text that starts with the letter `t`. How do you generate the next character? 

First, take some input text. Then, of all the occurrences of `'t'` in that input text, choose one at random. The character immediately following the `'t'` you picked will be the next character you generate. You can continue to generate characters by picking at random an example of that new character in your input text, and generating the character that follows that example of the character that you picked in the text.

Your output text will have the same statistical properties as your input text. For example, letters that appear more frequently after `'t'` in your input text will appear more frequently after `'t'` in your generated output. In an English n-gram model, if the first character you generate is `'t'`, the next character is much more likely to be `'h'` or `'o'` than `'q'` or '`g`'. Why? Because there are many very common words that contain `'th'` or `'to'` (*the, this, that, think, to, top, tomorrow, today*) but very few words and mostly very rare words that contain the sequence `'tq'` or `'tg'` (*cotquean, mortgage*).

### Language models of different orders (degrees)
This is called a *first order Markov model* or a *Markov model of order 1* or a *unigram model*, because the next character that we generate depends only on the single (1) previous character. With a first order Markov model, we probably won't generate a sentence that makes sense or has correct grammar, and we'll get some nonsense words, but the words will look more or less like English words.

We can easily modify our algorithm to have an order larger than 1. In language modeling, a Markov model of order 2 is called a *bigram model*. A Markov model of order 3 is a *trigram model*. Here is the algorithm for a *Markov model of order n* or *n-gram model*:

1. Let S be the string of the last **n** characters we generated.

2. Of all the occurrences of S in the input text, choose one at random and record the character d that follows.

3. Output d.

4. Repeat with the new string of the last n characters, which will now include d.  

What's the effect of using a smaller vs. larger order? If the order is small, the next letter is not too constrained and we'll generate text that "looks like" English but doesn't make much sense. As the order goes up, the next letter is more constrained, so the output will be more similar to the input. If the order gets too large, the output will be so constrained that it will be simply a verbatim copy of the input. The goal is to have an intermediate order so that the output is unique while retaining the input's statistical properties.

When Shannon proposed this algorithm, it had to be done by hand because the first digital electronic computers had barely been invented! Here is Shannon's summary of the technique (where "degree" corresponds to what we call "order"):

> To construct [degree=1 language] for example, one opens a book at random and selects a letter at random on the page. This letter is recorded. The book is then opened to another page and one reads until this letter is encountered. The succeeding letter is then recorded. Turning to another page this second letter is searched for and the succeeding letter recorded, etc.... It would be interesting if further approximations [of higher degree] could be constructed, but the labor involved becomes enormous at the next stage.

Shannon balked at the idea of high order models, but fortunately for you, today's computers are capable of building a language model, even of a high order.


## Your task

You will write a Java program to implement Shannon's language generation algorithm. Both the source text and the order will be inputs to the program. Your program will be capable of generating output text from any input text using a Markov model of any order. However, you only need to test it for orders up to and including 5.

## Coding Overview

### Part 1: `ModelC.java` constructor: Building the language model
You will write the constructor for the included `ModelC.java` class. The constructor builds the language model using an input text and a specified order (1, 2, 3, 4, or 5). The model is stored in a `HashMap` whose keys are character sequences and whose values are single character. We will store the characters as Strings, not as char or Character. Instructions for how this all should work are included in the comments of the class. There are also more details below.

### Part 2: `LanguageGenerator.java` main method: Running the program and generating output
You will write a main method in the file `LanguageGenerator.java` that will do the following:

1. Read from the command line two arguments:
 * args\[0\]: the name of a file to serve as the input text.
 * args\[1\]: the Markov order of the model to be build (1, 2, 3, 4, or 5)

2. Create an instance of the `ModelC` class using the input text and order. This will be your n-gram Markov model, where n corresponds to the order supplied by the user in args\[1\]. In Part 1, you wrote the code in the constructor for the `ModelC` class that builds the model.

3. Generate output text by sampling the Markov model using the sample() method in the `ModelC` class.

Instructions for how this all should work are included in the comments of the class. There are also more details below.

## Implementation Guidelines

Although you can implement this in many ways, I recommend using the `HashMap<Key, Value>` implementation of `java.util.Map<Key, Value>`. A map in java is very similar to a dictionary in Python or a hash in Perl. In `ModelC.java` the *keys* for the map are `String`s and the *values* are `ArrayList<String>`s:

```java
HashMap<String, ArrayList<String>> map = new ArrayList<String, ArrayList<String>>();
```

 An example of such a map in Python-ish notation might be:

```java
map = { "the" : [' ', 'n', ' ', 'r', 's'],
        "e t" : ['h', 'r', 'h', 'h']
      }
```

Notice that in the list associated with the string `"the"`, there are **two** spaces and the list keyed by the string `"e t"`, there are **three** `'h'`s. Spaces are characters. And the lists of characters are not unique. Thus, if the key is `'q'`, the ArrayList of values will include lots and lots of instances of `'u'`.

### Implementing Part 1: Building the language Model

Go through the input text one character at a time. Each character is preceded by up to **n** characters. Make a string out of those preceding characters. Look in the map to see if there is already an entry for that string. If the string is already associated with a list in the map, then retrieve the list and add the current character to the list.

If the string isn't already a key in the map (i.e., it isn't associated with a list), then insert the string as a new key in the map. It should be associated with a new singleton list containing the current character.

Note that, normally, the string of preceding characters will have exactly **n** characters, but near the front of the input text, the key string will have between 0 and **n** characters.  The string of preceding characters associated with the first character is a dummy start-of-text sequence represented by the empty string `""`. This will be the first key entered into your map.

What about at the end of the input text? We will use the dummy end-of-text sequence "$$$" as the value for the very last key in the text. 

More details are provided as comments in the code.

### Implementing Part 2: Sampling the language Model (5 points)

Given the degree **n** and the language model created above, we can now generate the output text as follows. 

1. Maintain a `String` variable that contains the **n** most recently generated characters. (This string starts out empty since the empty string is the key that gets us started.)  

2. Look up that string in the map.  From the list of observed subsequent characters associated with that string, randomly choose an element of the list. 

3. The chosen list element will either be a character or the special end-of-text value `$$$`.  If the element is a character that normally ends a sentence (you can limit yourselves to `"."`, `"!"`, or `"?"`) or the special end-of-text value `$$$`, then stop generatingn and print out your result. Otherwise, append the character to the output text. 

4. Then append the character to the string of most recently generated **n** characters, making sure that the length of that string does not get larger than **n**. If the length of the string is longer than **n**, remove the first character.

Repeat. The process will eventually stop because we will eventually hit a punctuation mark that ends a sentence or "^^^".

## Extra Credit (up to 5 points total, which can be used to improve another problem set)

+ There's no way to know ahead of time how long the generated text will be. Add some way for the user to specify an approximate desired length for the output text.  Sample the model repeatedly (for a maximum number of times, perhaps) until you generate text that is approximately the right length.

+ Build a unigram model (order 1), and create a graph showing the relative frequencies of each lowercase letter (i.e., of all the letters, how many were `'a'`, how many were `'b`', and so on). Then pick a letter and create a graph showing the relative frequencies of each lowercase letter *appearing after that letter* (i.e., of all the times that you saw `'t'`, how many times was it followed by `'a'`, how many times was it followed by `'b'`, and so on). When you turn in your chart, write 2-3 sentences about the results.

+ In this assignment, we built a character-level language model. Many AI applications use a word-level language model. Create a new version of `ModelC` and `LanguagGenerator` that will store a word-level language model and produce text at the word level. Note that you will need a much, much longer text to get reasonable results.

### What to Submit

In addition to your code, your repo should contain at least one sample input text along with sample output (.txt files) for order 1 through order 5.

---

### Appendix A : Examples

#### Sample 1 Input: Excerpt from a transcript of a 2011 speech by Donald J. Trump

> *Note: This speech transcript and subsequent Markov examples were chosen long before Donald Trump entered politics. They were chosen because of the entertainment value of Trump's speaking style and not for any political reasons.*

```
THIS IS REALLY AMAZING. I WANT TO THANK CONGRESSMAN ALAN WEST. HE IS AN AMAZING GUY. I HAVE BEEN A
SUPPORTER OF HIS. HE IS TOUGH, SMART, AND A REAL PATRIOT. ALSO, RICK SCOTT. HE IS DOING A GOOD 
JOB. IT IS NOT EASY. HE IS DOING A HELLUVA JOB.

MY SECOND HOME IS RIGHT DOWN THE ROAD IN YOUR COMPETITIVE COMMUNITY KNOWN AS PALM BEACH. I LOVE
FLORIDA. I WOULD LIKE TO THANK THE SOUTH FLORIDA TEA PARTY FOR THE OPPORTUNITY TO ADDRESS THIS
GROUP OF HARD-WORKING, INCREDIBLE PEOPLE. IT IS MY GREAT HONOR, BELIEVE ME. [APPLAUSE]

OVER THE LAST SIX MONTHS SINCE I STARTED THINKING ABOUT THIS, I HAVE BEEN ASKED SO MUCH ABOUT THE
TEA PARTY BY REPORTERS AND A LOT OF DIFFERENT FOLKS. I HAVE COME UP WITH A TRUTHFUL BUT STANDARD
ANSWER. THEY ARE GREAT BECAUSE THEY MADE WASHINGTON START THINKING, BOTH DEMOCRATS AND 
REPUBLICANS.  THEY MADE WASHINGTON START THINKING.

I WANT TO THANK YOU ALL. IT IS FANTASTIC. WHEN I WAS ASKED TO DO THIS SPEECH TODAY BY A FRIEND OF
MINE, HE SAID IT WOULD BE IN AN AUDITORIUM WITH 250 PEOPLE. WHAT HAPPENED? [CHEERS AND APPLAUSE]
WITH ALL OF THE WIND, AT LEAST YOU KNOW IT IS MY REAL HAIR. THE UNITED STATES HAS BECOME THE 
LAUGHING STOCK AND A WHIPPING POST FOR THE REST OF THE WORLD WHETHER WE LIKE IT OR NOT. WE DO NOT
LIKE IT.
```

##### 1: Output (Degree=1)

```
THE TANSTH IS INGUTHATEF DOF A ARE STRINO, H, IBE ORS MY ISOUS WOPPLY LD IN O ALAS PLAD ST ICAD D
WA TY TAD TY HSITRD THE IND TOUN F LANOU WODIT THALOR DVAVE. BE Y COUDVE BOP HO LLE. O, THAT. 
OULERE US W OWHEEOLAULESE OT T THE IS SANOTAT RT. AMERIS. G, WAZIS WISKNCANKN T LMMECOTOMINTHE 
ADIVEDO TH [CO IGRTR. Y. AT OPEAN Y THINDAD BLAT HE WENGEEAGUNGHE HID ANY PINY CALE TURISITHSOF HI
THEROTRY O DREADI R ISPARED D T. ALDENTOUNE THETINY ANG INOBERIOF IK. BEECALDD IN WH T OCOR.
MEATENG B. THESOR RK D HE. OPPECHERY Y THIKEORENT WN W IX TANKI LAPAS BEY GTETHTOWATHOF HE WNS
NKNOUGURE DAN BO WOVEAUNE F WISPALIPL. TA FAN WNTHE OL HOUY. RS OTHE WAINDERAUTUS LLIGEY ISTONS T
A WNOLLOTHEOVE TOUT JOLLLED AD T ICCOUD LLISINONGHO LANOT HA BOMEAI RTHIUPLLE. IND HEANTHE. I HAND
ME W Y LK IX CONTHE HE. IPEAN WOUTOUY IN KIS. CATHERTHSOTANG HE ROUNOUND PASTS, RINGTHE FLIO,
CONTH KSEASTE S IN L W SWAKNAS ST TUSMECEE HALOUS HE]
```

##### 1: Output (Degree=2)

```
TH A GREASKE THINKING. ITY GUY. HAT TAR. IT THIN IS THS OF HE TOCRACK SUP WAST BY MOCK ST EARD TH
DOING. HONS FOR TRIGHIS MINKIND READE BELIKE INAT HAVE. THIS TUNTAKEND JOB. ITY ANKINGRAT STAGE
SPEOPLAUSECTIC. THE OF PEOPEOPPLEAL BILLUVAND IS RESSMADE REACK COT. I HAVE OF THIS AT YOUT THASY.
IN AN AT LEASKE LOVE. ISGROAD SCOULD SAIR.
```

##### 1: Output (Degree=3)

```
THIS TAKING. I WANT THANK YOUR LEADE WORKING STATES LAUGHINKING AT HAIR. THIS. I WORLD BEEN TAKE
OF TO DO TAKE COUNTRY [APPLAUSE]

I KNOWN A DISGRACE. IT IS IS AN AUDITORIDA TEA PATRIES.
```

##### 1: Output (Degree=4)

```
THIS IS A DISGRACE. I HAVE TO THANK CONGRESS THIS, I HAVE BEEN AS WEAK AND APPLAUSE THE TEA PARTY
BY A FRIEND OF HIS. HE IS DOING GUY. I HAVE BEEN A SUPPORTUNITY TO DO NOT. WE DO THANK YOU KNOWN
THE WORLD IS MY REALLY AMAZING AT OUR LEADERS A DISGRACE. I HAVE BEEN ASKED SO MUCH ABOUT THIS
SPEECH TODAY BY A FRIEND OF DIFFERENT FOR THE LAUGHINGTON START THEY MADE WAS AS PALM BEACH. I WAS
ASKED TO THIS, I HAVE COMPETITIVE COMMUNITY TO THEY ARE GREAT HONOR, BELIEVE ME. [APPLAUSE]

I KNOW IT WOULD BE IN YOUR COUNTRIES.
```

##### 1: Output (Degree=4)

```
THIS SPEECH TODAY BY A FRIEND OF BILLIONS OF THE OPPORTERS SEE OUR LEADERS AS WEAK AND A LOT OF
MINE, HE IS MY REPUBLICANS. THEY'RE LAST YOU KNOW IT OR NOT. ALSO, RICK SCOTT. HE IS NOT LIKE
CHINA, INDIA, SOUTH KOREA, MEXICO, THE ADVANTASTIC. WHAT HAPPENED? [CHEERS TAKE OUR COUNTRIES LIKE
IT IS DOING POST FOLKS. I WASHING POST FOR THE LAUGHING GUY. I WOULD LIKE IT IS AN AMAZING A GOOD
JOB.

MY SECOND HOME IS AN ALAN WEST. HE IS RIGHT DOWN THE LAUGHINGTON START THIS, I HAVE BEEN ASKED TO
THANK YOU KNOW A LOT OF DIFFERENT FOR THE UNITY KNOW IT WOULD LIKE IT. THE ROAD IN AN AMAZING GUY.
I LOVE FLORIDA TEA PARTY BY REPUBLICANS. THE OPEC NATIONS OF DOLLARS A DISGRACE. IT OR NOT EASY.
HE SAID ON NUMEROUS OCCASIONS OF HARD-WORKING. I LOVE FLORIDA TEA PARTY FOR THE UNITED STATES HAS BECAUSE]

I KNOW A LOT OF BILLIONS OF DIFFERENT FOLKS. I WANT TO ADDRESSMAN ALAN WEST. HE IS DOING ABOUT 
THINKING, INDIA, SOUTH KOREA, MEXICO, THEY MADE WASHING AT OUR COMMUNITY TO ADDRESS THINKING, BOTH
DEMOCRATS AND MANY OTHERS AND A WHIPPING POST FOR THE WIND, AT LEADERS AND MANY OTHER COUNTRIES.
```

##### 1: Output (Degree=5)

```
THIS GROUP OF HIS. HE IS DOING A HELLUVA JOB. 

MY SECOND HOME IS A DISGRACE. I HAVE SAID IT WOULD BE IN OTHERS SEE OUR LEADERS AS WEAK AND MANY
OTHERS SEE OUR LEADERS AND A WHIPPING POST FOR THE WORLD IS LAUGHING A GOOD JOB.

MY SECOND HOME IS AN AUDITORIUM WITH 250 PEOPLE IN OTHER COUNTRIES.
```

##### 1: Output (Degree=5)

```
THIS SPEECH TODAY BY A FRIEND OF MINE, HE SAID IT WOULD BE IN AN AUDITORIUM WITH A TRUTHFUL BUT
STANDARD ANSWER. THE REST OF PEOPLE. WHAT HAPPENED? [CHEERS AND A REAL HAIR. THE WORLD IS LAUGHING
STOCK AND A WHIPPING POST FOR THE OPPORTERS AND MANY OTHERS SEE OUR LEADERS AND A WHIPPING POST
FOR THE TEA PARTY BY A FRIEND OF MINE, HE SAID ON NUMEROUS OCCASIONS OF DIFFERENT FOLKS. I HAVE
BEEN TAKEN ADVANTAGE OF US. THE ADVANTAGE OF US. IT IS NOT EASY. HE IS TOUGH, SMART, AND A REALLY
AMAZING GUY. I HAVE SAID IT WOULD LIKE TO TAKEN ADVANTAGE OF HIS. HE IS TOUGH, SMART, AND
INEFFECTIVE. WE DO NOT EASY. HE IS DOING A HELLUVA JOB.

MY SECOND HOME IS DOING AT OUR COUNTRIES.
```



