<h1>Tournament Maker</h1>

<p>This program lets you create tournaments (currently round robin), record scores and view the table. It is created using plain HTML and JavaScript for the UI and Java with PostgreSQL for the server and the database.</p>

<h2>How to use</h2>

<p>This program uses Java for the backend operations, hence you will need to have JRE installed. To do this in Ubuntu, first update the package repository by typing</p>

```
sudo apt update
```

<p>Then, install JRE by typing</p>

```
sudo apt install default-jre
```

<p>This program uses PostgreSQL for the database which you need to have installed. To install, type<p>

```
apt-get install postgresql-12 
```

<p>Moreover, the Java server connects to Postgres through a role with username and password <em>java</em>, and you will need to create this role. First, open a terminal and connect to Postgres with the superuser by typing</p>

```
sudo -u postgres psql
```

<p>Now you should see the psql console in your terminal which looks like this:</p>

```
postgres#=
```


<p>Now, create a user named <code>fixturemaker</code> with password <code>java</code> by typing</p>

```
CREATE USER fixturemaker WITH PASSWORD 'java';
```

<p><em>Note: You can use the program with a different username/password combination by changing the file <code>src/tournament/DatabaseConnection.java</code>, and recompiling the file.</em></p>

<p>You will also need to create a main database for <code>fixturemaker</code> to log in to. To do that, type</p>

```
CREATE DATABASE fixturemaker;
```

<p>Moreover, give <code>fixturemaker</code> permission to create tables and schemas on this database by typing</p>

```
GRANT CREATE ON DATABASE fixturemaker TO fixturemaker;
```

<p>Now you are ready to run the program. First, move inside the folder by opening this folder in a terminal and typing <code>cd bin</code>. Then, start the server by typing</p>

```
java -cp ../lib/*: tournament.Server
```

<p>Now, you can open <code>src/html/tournament.html</code> using your browser to use the program. Make sure to close the server (closing the terminal or terminating is sufficient) when you are done.</p>