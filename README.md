# OCR - First Order Predicate Logic Datasets
This repo contains the original Datasets used for OCR of handwritten First Order Predicate Logic sentences characters along with their extra 400 rotated versions per character.

The contained samples are consisted of 67 different characters as: <br />
a | b | c(C) | d | e | f | g | h | i | j | k(K) | l | m | n | o(O) | p(P) | q | r | s(S) | t | u | v(V and &or;) | w(W) | x(X) | y | z(Z) |
A | B | D | E | F | G | H | I | J | L | M | N | Q | R | T | U | Y | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | , | ( | ) | - | + | = | < | > |
&forall; | &exist; | &and; | &rarr; | &harr; | &not; |

DataSets are divided in 2 group folders according to the resizing of every normalized character: 28x28 and 35x35. <br /> 
Each folder has a compressed .7z file that can be uncompressed with '7zip' open-source program.<br />
Each compressed data-file contains 3 files: the original Trainset file, a TestSet file and an extra Trainset file of all rotated characters. 

The form of Datasets is headless .csv (comma separated values) files with the following structure: <br />
-In 28x28 Datasets each row is consisted of 784 binary values in decimal representation (sample) followed by 67 binary values (label). <br />
-In 35x35 Datasets each row is consisted of 1225 binary values in decimal representation (sample) followed by 67 binary values (label). <br />

Also, the Java class 'CSVtools' is provided that contains help methods in order to visualize, extract and show stats of all the samples from Dataset.
 In order to work properly, user must pass the dimension of target Dataset in the class Constructor and use any of the implemeted methods.
