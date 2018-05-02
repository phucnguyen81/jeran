### Example 10.4 
For each team that is captained by a player resident in Eltham, 
get the team number and the number of matches that has been played 
for that team.

### Example 10.5
Get each different penalty amount, followed by the number of times that 
the amount occurs in the PENALTIES table, and also show the result of 
that amount multiplied by the number.

### Example 10.6
For the MATCHES table, get all the different combinations of team numbers 
and player numbers.

### Example 10.7
For each player who has ever incurred at least one penalty, get the player number, 
the name, and the total amount in penalties incurred.

Solution:

```txt

SELECT   P.PLAYERNO, NAME, SUM(AMOUNT)
FROM     PLAYERS AS P INNER JOIN PENALTIES AS PEN
         ON P.PLAYERNO = PEN.PLAYERNO
GROUP BY P.PLAYERNO, NAME

The result is: 

P.PLAYERNO  NAME       SUM(AMOUNT)
----------  ---------  -----------
         6  Parmenter       100.00
         8  Newcastle        25.00
        27  Collins         175.00
        44  Baker           130.00
       104  Moorman          50.00
```

### Example 10.8
For each year present in the PENALTIES table, get the number of penalties paid.

Solution:

```txt

SELECT   YEAR(PAYMENT_DATE), COUNT(*)
FROM     PENALTIES
GROUP BY YEAR(PAYMENT_DATE)

The result is: 

YEAR(PAYMENT_DATE)  COUNT(*)
------------------  --------
1980                       3
1981                       1
1982                       1
1983                       1
1984                       2
```

### Example 10.9
Group the players on the basis of their player numbers. 
Group 1 should contain the players with number 1 up to and including 24. 
Group 2 should contain the players with numbers 25 up to and including 49, and so on. 
For each group, get the number of players and the highest player number.

Solution:

```txt

SELECT   TRUNCATE(PLAYERNO/25,0), COUNT(*), MAX(PLAYERNO)
FROM     PLAYERS
GROUP BY TRUNCATE(PLAYERNO/25,0)

The result is: 

TRUNCATE(PLAYERNO/25,0)  COUNT(*)  MAX(PLAYERNO)
-----------------------  --------  -------------
                      0         4              8
                      1         4             44
                      2         1             57
                      3         2             95
                      4         3            112
```

### Example 10.12
What is the average total amount of penalties for players who live in 
Stratford and Inglewood?

```txt

SELECT  AVG(TOTAL)
FROM   (SELECT   PLAYERNO, SUM(AMOUNT) AS TOTAL
        FROM     PENALTIES
        GROUP BY PLAYERNO) AS TOTALS
WHERE   PLAYERNO IN
       (SELECT   PLAYERNO
        FROM     PLAYERS
        WHERE    TOWN = 'Stratford' OR TOWN = 'Inglewood')

The result is: 

AVG(TOTAL)
----------
85
```

### Example 10.13
For each player who incurred penalties and is captain,
get the player number, the name, the number of penalties that he or she incurred, 
and the number of teams that he or she captains.

```txt

SELECT   PLAYERS.PLAYERNO, NAME, NUMBER_OF_PENALTIES,
         NUMBER_OF_TEAMS
FROM     PLAYERS,
        (SELECT   PLAYERNO, COUNT(*) AS NUMBER_OF_PENALTIES
         FROM     PENALTIES
         GROUP BY PLAYERNO) AS NUMBER_PENALTIES,
        (SELECT   PLAYERNO, COUNT(*) AS NUMBER_OF_TEAMS
         FROM     TEAMS
         GROUP BY PLAYERNO) AS NUMBER_TEAMS
WHERE    PLAYERS.PLAYERNO = NUMBER_PENALTIES.PLAYERNO
AND      PLAYERS.PLAYERNO = NUMBER_TEAMS.PLAYERNO

The result is: 

PLAYERNO  NAME       NUMBER_OF_PENALTIES  NUMBER_OF_TEAMS
--------  ---------  -------------------  ---------------
       6  Parmenter                    1                1
      27  Collins                      2                1
```

### Example 10.14
Get the player number and the total number of penalties for each player 
who played a match.

```txt

SELECT  DISTINCT M.PLAYERNO, NUMBERP
FROM    MATCHES AS M LEFT OUTER JOIN
          (SELECT   PLAYERNO, COUNT(*) AS NUMBERP
           FROM     PENALTIES
           GROUP BY PLAYERNO) AS NP
        ON M.PLAYERNO = NP.PLAYERNO

Explanation: In this statement, the subquery creates the following 
intermediate result (this is the NP table): 

PLAYERNO  NUMBERP
--------  -------
       6        1
       8        1
      27        2
      44        3
     104        1

Next, this table is joined with the MATCHES table. 
We execute a left outer join, so no players disappear from this table. 
The final result is: 

PLAYERNO  NUMBERP
--------  -------
       2        ?
       6        1
       8        1
      27        2
      44        3
      57        ?
      83        ?
     104        1
     112        ?        
```