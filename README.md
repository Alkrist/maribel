# maribel
![alt text](https://github.com/Alkrist/maribel/blob/master/logo.png)
game engine API, based on the ECS approach split for Server and Client. Every object in the game is represented as Entity with a set of Components, which later re updted in
Systems. Every Entity is created based on Game Object which has no use in the game on its own and server just as a factory for entities.
### Version naimng idea:
for Maribel - YYYYMMDD, Year - Month - Day. 

## how does it work
The engine has 5 main stages, called one by one after the application is started:

1. Preinitilization
2. Initialization
3. Postinitialization
4. Running
5. Cleanup

### Preinitilization
Loads the loaders, can't be modified externally.
* Display
* Master Engine
* Loaders
* Loading screen for loading stage

### Initialization
On this stage all the stuff added through API is loaded by the loaders loaded on _PreInit_ stage.
I.e. events you added, listeners you registered, packets - all is registered on this stage.

### Postinitialization
Loads the actual game contents.
* Models
* Sounds
* Register items
Notice that world and stuff related to the game is not loaded on this stage, it's loaded when the server is started.

### Running
After everything is loaded, the main menu scene is rendered and the application is ready. You can switch to game, which will run the server or get back to menu.

### Cleanup
Is performed after the game is stopped, used to free the memory.

## Architecture
In Maribel we have 3 main instances (not including Utils and API packages): 
1. Client
2. Server
3. Common

Client is in charge of rendering and inputs, Server is in charge of world updating and game mechanics. Common contains stuff necessary for both instances, like
ECS, Events and Connection. 
Connection is established locally, via shared memory or remotely, via the UDP. Switching between them is possible, if the game is hosted and played on the same machine. _Not implemented yet_

Server is in charge of level loading, updating and saving. Holds no models nor textures, but world data and player data.

## TODO:
0. Fix all of the small TODO lists inside the project.
6. Add Graphics via LWJGL 3.
7. Add Models.
8. Add Sounds via OpenAL.
9. Add Commands.
