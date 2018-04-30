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
