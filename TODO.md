# Use embedded database
It's time to turn the project into a self-contained app.
First thing to get rid of is the connection to mysql database.
Need an embedded database to replace the mysql database.
Just use H2, it is written in Java and used by Spring team.

So the migration would go like this:
- check out H2 for using as embedded db
- connect to H2, load schemas, execute sqls from this project
- that is it I think?

# Excercises section 
Put some excercises in here.
The excercises should be representative of some sql aspects.
Try doing the excercises in Session

# Snippets
Some common sql statements here for references.

# Find all relevant tables
Currently use hard-coded table names.
Would be more general to find all tables from database metadata.
