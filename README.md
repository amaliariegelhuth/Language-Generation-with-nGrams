## Problem Set 6: Language generation with n-grams

### Due Friday, October 26 @ 11:59pm

[Click here for an up-to-date version of this README](https://github.com/BC-CSCI-1102-F18-MW/ps6/blob/master/README.md) 

---

This is a pair problem set. Find one partner to work with. If you don't know anyone yet, email me, and I will try match you up with someone else who is also looking for a partner. **Please identify both team members in the comments at the top of your files.**

---
## Background
### Introduction
In this assignment, you'll write a program that generates novel text given an example input text. If the input is English text, then the program will output English text. If the input is French, then the output will be  French. If the input is a python program, then the output will be a python program. If the input is a genetic sequence, then the output will be a new genetic sequence. The generated text will not be perfect, but it will "look like" the input text because we will make sure it has the same statistical (i.e., distributional) properties.

After class on Monday, you will know a bit about n-gram language modeling, which is used in a wide variety of AI applications including automatic speech recognition, text mining, and natural language generation. The reason n-gram language models work is that language is not random: you can predict what word or letter is going to come next based on the previous words or letters. 

The algorithm you'll implement to build n-gram models and generate language from them was first proposed in Claude Shannon's landmark 1948 paper on information theory [A Mathematical Theory of Communication](http://cm.bell-labs.com/cm/ms/what/shannonday/shannon1948.pdf). The basic idea involves stochastic processes that were first explored by [Andrei Markov](http://en.wikipedia.org/wiki/Andrey_Markov) decades earlier.

### Character n-gram Language Models 
In class, we'll be talking about both word n-gram models and character n-gram models. In this problem set, we'll be focusing on character-level n-gram models. Here's an intuition of how it will works:

Suppose you want to generate a new text that starts with the letter `t`. How do you generate the next character? 

First, take some input text. Then, of all the occurrences of `'t'` in that input text, choose one at random. The character immediately following the `'t'` you picked will be the next character you generate. You can continue to generate characters by picking at random an example of that new character in your input text, and generating the character that follows that example of the character that you picked in the text.

Your output text will have the same statistical properties as your input text. For example, letters that appear more frequently after `'t'` in your input text will appear more frequently after `'t'` in your generated output. In an English n-gram model, if the first character you generate is `'t'`, the next character is much more likely to be `'h'` or `'o'` than `'q'` or '`g`'. Why? Because there are many very common words that contain `'th'` or `'to'` (*the, this, that, think, to, top, tomorrow, today*) but very few words and mostly very rare words that contain the sequence `'tq'` or `'tg'` (*cotquean, mortgage*).

### Language models of different orders (degrees)
This is called a *first order Markov model* or a *Markov model of order 1* or a *unigram model*, because the next character that we generate depends only on the single (1) previous character. With a first order Markov model, we probably won't generate a sentence that makes sense or has correct grammar, and we'll get some nonsense words, but the words will be look like English words.

We can easily modify our algorithm to have an order larger than 1. A model of order 2 is a *bigram model*. A model of order 3 is a *trigram model*. Here is the algorithm for a *Markov model of order n* or *n-gram model*:

1. Let S be the string of the last **n** characters we generated.

2. Of all the occurrences of S in the input text, choose one at random and record the character d that follows.

3. Output d.

4. Repeat with the new string of the last n characters, which will now include d.  

What's the effect of using a smaller vs. larger degree? If the degree is small, the next letter is not too constrained and we'll generate text that "looks like" English but doesn't make much sense. As the degree goes up, the next letter is more constrained, so the output will be more similar to the input. If the degree gets too large, the output will be so constrained that it will be simply a verbatim copy of the input. The goal is to have an intermediate degree so that the output is unique while retaining the input's statistical properties.

When Shannon proposed this algorithm, it had to be done by hand because the first digital electronic computers had barely been invented! Here is Shannon's summary of the technique:

> To construct [degree=1 language] for example, one opens a book at random and selects a letter at random on the page. This letter is recorded. The book is then opened to another page and one reads until this letter is encountered. The succeeding letter is then recorded. Turning to another page this second letter is searched for and the succeeding letter recorded, etc.... It would be interesting if further approximations [of higher degree] could be constructed, but the labor involved becomes enormous at the next stage.

Shannon balked at the idea of high degree models, but fortunately for you, today's computers are capable of building a language model, even of a high order.

### Your task

You will write a Java program to implement Shannon's language generation algorithm. Both the source text and the order will be inputs to the program, and so your program will be capable of generating output text from any input text using a Markov model of any degree. 

## Coding Overview

The word *callback* is sometimes used to refer to a procedure that is executed in response to some asynchronous event such as the click of a button or the filling of a text field. The callbacks are executed when the button is pressed. In this problem set, you need to write the callback function `process` in the file `Shannon.java` for the button (labeled `Go`). 

When the `Go` button is pressed, your `process` callback function should:

1. Get the degree `this.degree`;

2. Build the Markov language model from the input text in `this.input`;

3. Generate the output text by sampling the Markov model.

There are many ways to write this program. I recommend using the `java.util.Map<Key, Value>` type to implement a *map* relating strings to lists of characters. That is, the *keys* for the map are `String`s and the *values* are `List<Character>`s:

```java
Map<String, List<Character>> map = new ArrayList<String, List<Character>>();
```

 An example of such a map in Python-ish notation might be:

```java
map = { "the" : [' ', 'n', ' ', 'r', 's'],
        "e t" : ['h', 'r', 'h', 'h']
      }
```

Notice that in the list associated with the string `"the"`, there are **two** spaces and the list keyed by the string `"e t"`, there are **three** `'h'`s.

### Building the Markov Model (5 points)

Go through the input text one character at a time. Each character is preceded by up to **N** characters. Make a string out of those preceding characters. Look in the map to see if there is already an entry for that string. If the string is already associated with a list in the map, then retrieve the list and add the current character to the list.

If the string isn't already a key in the map (i.e., it isn't associated with a list), then insert the string as a new key in the map. It should be associated with a new singleton list containing the current character.

Note that, normally, the string of preceding characters will have exactly **N** characters, but near the front of the input text, the key string will have between 0 and **n** characters.  The string of preceding characters associated with the first character is the empty string `""`.

What about at the end of the input text? I recommend the following: pretend that there is a special character with value `Main.SENTINAL` at the end of the text. Insert this special value into the relevant list in the map, and then stop. You'll see below how we'll use this special value when sampling the model.

### Sampling the Markov Model (5 points)

Given the degree **N** and the Markov model created above, we can now generate the output text.  Maintain a `String` variable that contains the **N** most recently generated characters. (This string starts out empty.)  Look up that string in the map.  From the list associated with that string, randomly choose an element of the list. The chosen list element will either be a character or thespecial value `Main.SENTINAL`.  If the element is `Main.SENTINAL`, then stop. If the element is a real character, use it as the next character to generate for the output text. Append the character to the output text. Also append the character to the string of most recently generated **N** characters, making sure that the length of that string does not get larger than **N**. Repeat.  The process will eventually stop because we will eventually hit that special element `Main.SENTINAL`.

You should strongly consider defining the Markov model as an ADT.

### Bells and Whistles


+ There's no way to know ahead of time how long the generated text will be. Add some way for the user to specify an approximate desired length for the output text.  Sample the model repeatedly (for a maximum number of times, perhaps) until you generate text that is approximately the right length.

+ Create a graph showing the distribution of length of the generated text for a fixed input text.  

+ In this assignment, we built a character-level Markov model. This is very flexible as it can be applied to any kind of data, but for natural language a word-level Markov model can generate more realistic text. Add a second button that uses a word-level Markov model to generate the output text.

### What to Submit

your repo should contain at least one sample input text along with sample output (.txt files) for degree 1 through degree 6.

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



