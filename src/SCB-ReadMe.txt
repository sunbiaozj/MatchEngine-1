The zip file contains:
x.txt
y.txt
com/scb
	-rw-r--r--  1 bekuku  staff    545 Jul 14 23:42 AccountMatch.java
	-rw-r--r--  1 bekuku  staff   1078 Jul 15 00:46 AmountMatch.java
	-rw-r--r--  1 bekuku  staff    559 Jul 14 23:17 BreakItem.java
	-rw-r--r--  1 bekuku  staff    398 Jul 14 22:59 MatchCriteria.java
	-rw-r--r--  1 bekuku  staff    989 Jul 15 20:45 MatchItem.java
	-rw-r--r--  1 bekuku  staff    136 Jul 14 23:38 MatchType.java
	-rw-r--r--  1 bekuku  staff    158 Jul 14 23:40 Matching.java
	-rw-r--r--  1 bekuku  staff  10477 Jul 15 23:05 MatchingService.java
	-rw-r--r--  1 bekuku  staff   2669 Jul 15 22:56 MatchingServiceTest.java
	-rw-r--r--  1 bekuku  staff   1552 Jul 15 15:51 PostingDateMatch.java
	-rw-r--r--  1 bekuku  staff    521 Jul 15 16:18 ReconType.java
	-rw-r--r--  1 bekuku  staff   1884 Jul 14 22:24 Transaction.java

Pleae note few things before running the application:
1. The application requires JDK 1.8
2. The application provides 2 Recon Types: LINE_BY_LINE or X_AS_REFERENCE (Using X file as reference and Y match against X)
3. By default, fileXPath = "x.txt", fileYPath = "y.txt"
4. Compilation:
	Extract the zip file.
	cd to the extracted folder
	Complile main java class and its dependecies by running: 
		javac -cp . com/scb/MatchingServiceTest.java
5. Run:
	java -ea com/scb/MatchingServiceTest
	(-ea is to enable assertion during JVM runtime)

