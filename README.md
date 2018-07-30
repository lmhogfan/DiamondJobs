[<img src="https://img.shields.io/travis/playframework/play-java-starter-example.svg"/>](https://travis-ci.org/playframework/play-java-starter-example)

# Diamond Jobs

This is a starter application that shows how Play works.  Please see the documentation at https://www.playframework.com/documentation/latest/Home for more details.

## Running

Run this using [sbt](http://www.scala-sbt.org/).  If you downloaded this project from http://www.playframework.com/download then you'll find a prepackaged version of sbt in the project directory:

```
sbt run
```

And then go to http://localhost:9000 to see the running web application.

## Getting Started

It is important to understand that this is a demo version of the application. This application was developed to help a local jewelry store better track jewelry repairs and custom jewelry jobs.

To access the database, navigate to the application.conf file. On lines 346 and 347, the default.username and default.password should be the same as your database manager.

To bypass log in to the application, use the username "admin" and leave the password blank. From there you should have full access to the application.

## Features

- Customers

Creating new customers, editing existing customers, creating new jobs, and reviewing past jobs.

- Repairs and Custom Jobs

Start new jobs, update existing jobs, and view job history.

- Employees

Creating new employees, edit existing employees(only if logged in as a store manager), and changing password (only for logged in employee).

- Dynamic Search

All list contain a dynamic search and sort feature to allow easier and simpler navigation.

- AWS Email Notifications

When a job is marked "Completed Awaiting Pick Up", the option of an automated email appears. Currently, the customers email address must be confirmed through AWS before sending.

## Upcoming Features

- Employee Portal

Allow Employees to clock in/out, view and edit time cards, submit time off requests.

- Inventory Management

- Integrative Point of Sale
