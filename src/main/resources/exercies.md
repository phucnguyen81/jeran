### Exercise 5.6
Rewrite the following SELECT statement in such a way that all column names are 
represented with their complete column specifications.

```sql

SELECT PLAYERNO, NAME, INITIALS
FROM PLAYERS
WHERE PLAYERNO > 6
ORDER BY NAME
```

### Exercies 5.7
What is wrong in the following SELECT statement?

```sql

SELECT PLAYERNO.PLAYERNO, NAME, INITIALS
FROM PLAYERS
WHERE PLAYERS.PLAYERNO = TEAMS.PLAYERNO
```

### Exercise 5.8
Get the database privileges owned by the current user. 

### Exercise 5.9
Find the numbers of the players who have become committee members today. 

### Exercise 5.11
Imagine that the tennis club has classified all the penalties in three categories. 
The category low contains all the penalties from 0 up to 40, 
the category moderate contains those between 41 up to 80, 
and the category high contains all the penalties higher than 80. 
Next, find for each penalty the payment number, the amount, and the matching category.

 
### Exercise 6.7
For the following SELECT statement, determine the intermediate result table after each 
clause has been processed; give the final result as well.

```sql

SELECT PLAYERNO
FROM PENALTIES
WHERE PAYMENT_DATE > '1980-12-08'
GROUP BY PLAYERNO
HAVING COUNT(*) > 1
ORDER BY PLAYERNO
```

### Exercies 6.10
Get the numbers of the committee members who were secretary of the tennis club 
between January 1, 1990, and December 31, 1994; use subqueries here.

### Excercise 6.11
Get the numbers of the teams of which the player with the name Parmenter 
and initial R is captain; in this example, we assume that there are no two 
players with the same name and initials.

### Exercise 10.4
For each team that has played in the first division, give the team number, 
the number of matches, and the total number of sets won.

### Exercise 10.5
For each combination of wonlost sets, get the number of matches won.

### Exercise 10.9
For each team, get the team number, the division, and the total number of sets won.

### Exercise 10.10
Group the players on the length of their names and get the number of players 
for each length.

### Exercise 10.11
For each match, determine the difference between the number of sets won and lost, 
and group the matches on that difference.

### Exercise 11.1
In which town do more than four players live? 

### Exercise 11.2
Get the player number of each player who has incurred more than $150 in penalties. 

### Exercise 11.3
Get the name, initials, and number of penalties of each player 
who has incurred more than one penalty. 

### Exercise 11.4
Get the number of the team for which most players have played, 
and give the number of players who have played for this team. 

### Exercise 11.5
Get the team number and the division of each team for which more than four 
players have competed. 

### Exercise 11.6
Get the name and initials of each player who has incurred two or 
more penalties of more than $40. 

### Exercise 11.7
Get the name and initials of each player whose total amount of 
penalties is the highest. 

### Exercise 11.8
Get the number of each player who has incurred twice as many 
penalties as player 104. 

### Exercise 11.9
Get the numbers of the players who have incurred as many penalties 
as player 6. 
