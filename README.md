## REST API 

The REST API is implemented with a simple Play controller that uses the above mentioned utils and it's configured in the play routes file to each one of the HTTP verbs representing the CRUD.

*conf/routes*

```
# Routes
# This file defines all application routes (Higher priority routes first)
# ~~~~

# Home page
GET           /                    controllers.Application.index

# Map static resources from the /public folder to the /assets URL path
GET           /assets/*file        controllers.Assets.at(path="/public", file)

# Lists movies
GET           /movies               controllers.Application.listMovies

# Gets a movie by id
GET           /movies/:id           controllers.Application.findMovie(id)

# Adds a movie
POST          /movies               controllers.Application.addMovie

# Partially updates the content of a movie
PUT           /movies/:id           controllers.Application.updateMovie(id)

# Deletes a movie
DELETE        /movies/:id           controllers.Application.deleteMovie(id)
```

##Setup

Is expected you have Play 2.2.x and Mongo installed on your local machine which you can get here:

- Play2 http://www.playframework.com/download

- Mongo http://www.mongodb.org/downloads

##Create the app

Once Play is installed you can simply create an app in your terminal like so:

    play new cinema-scala
    cd cinema-scala
    
##Add the ReactiveMongo Play Plugin Dependencies

Then proceed to edit some config files to add the Play Reactive Mongo plugin dependencies

project/Build.scala

```scala

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)

```

###Register the plugin with Play

*conf/play.plugins*

```
400:play.modules.reactivemongo.ReactiveMongoPlugin
```

###Add the DB config

*conf/application.conf*

```
mongodb.uri ="mongodb://username:password@localhost:27017/your_db_name"
```