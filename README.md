# OCR - First Order Predicate Logic Datasets
This repo contains the original Datasets used for OCR of handwritten First Order Predicate Logic characters along with their extra 400 rotated versions per character.

They are divided in 2 groups according to the resizing of every normalized character: 28x28 and 35x35.
Each group has 3 files: the original Trainset file, a TestSet file and an extra Trainset file of all rotated characters. 

The form of Datasets is headless .csv (comma separated values) with the following attributes:
-In 28x28 Dataset each row is consisted of 784 binary values in decimal representation followed by 67 binary values.
-In 35x35 Dataset each row is consisted of 1225 binary values in decimal representation followed by 67 binary values.

Also, a Java class is provided that can visualize all the samples from Dataset.
