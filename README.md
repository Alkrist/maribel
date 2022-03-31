# maribel
game engine API

### Version naimng idea:
for Maribel - YYYYMMDD, Year - Month - Day. 

## how does it work
The engine has 4 main stages, called one by one after the application is started:

1. Preinitilization
2. Initialization
3. Postinitialization
4. Running

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

## Architecture
In Maribel we have 3 main instances (not including Utils and API packages): 
1. Client
2. Server
3. Common

Client is in charge of rendering and inputs, Server is in charge of world updating and game mechanics. Common contains stuff necessary for both instances, like
ECS, Events and Connection. 
Connection is established locally, via shared memory or remotely, via the UDP. Switching between them is possible, if the game is hosted and played on the same machine. _Not implemented yet_

