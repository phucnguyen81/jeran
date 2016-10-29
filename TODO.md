# Use embedded database
It's time to turn the project into a self-contained app.
First thing to get rid of is the connection to mysql database.
Need an embedded database to replace the mysql database.
Just use H2, it is written in Java and used by Spring team.

So the migration would go like this:
- check out H2 for using as embedded db
- connect to H2, load schemas, execute sqls from this project

## A problem with H2
There is a sql compatibility problem with H2.
The scripts for MySQL is not 100% compatible with H2.

Options:
1) make another script for H2
2) parameterize the script to run with different sql dialects

Since there is only one script, lets go with 1) and just make another one for H2.
Later, if the scripts + dbs combinations get hairy, 
we can separate ddl statements, which are more vendor specific, from dml.

# Excercises section 
Put some excercises in here.
The excercises should be representative of some sql aspects.
Try doing the excercises in Session

# Snippets
Some common sql statements here for references.

# Find all relevant tables
Currently use hard-coded table names.
Would be more general to find all tables from database metadata.
