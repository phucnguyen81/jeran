## DONE Get table names from db

For H2, use the query:

```sql

SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_CATALOG = TENNIS
  AND TABLE_SCHEMA = 'PUBLIC'
  AND TABLE_TYPE = 'TABLE'
```

## On refresh, move to the previous tab page the user is in
Look like a job for cookie. Why? anything better?
- cookie
- server session

## Make launcher
Run with shell script.
When ran, pick an available port and tell the user to go there.

## Add a section for examples
Show syntax and common statements for references.

## Find all relevant tables
Currently use hard-coded table names.
Would be more general to find the relevant tables (from database metadata).

## DONE Style the sql syntax in the exercise
This is done using the Prism library to do syntax highlighting.

## DONE Load exercies from markdown file
Load the markdown file, convert it to html and inject it to template.
Process the html to style/enhance it.

## DONE Navigation buttons disappear when the view gets too small (medium size)
Fix: remove the class .collapse from navbar.