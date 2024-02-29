# Maribel
![alt text](https://github.com/Alkrist/maribel/blob/master/logo.png)
game engine API, based on the ECS approach split for Server and Client. Every object in the game is represented as Entity with a set of Components, which later re updted in
Systems. Every Entity is created based on Game Object which has no use in the game on its own and server just as a factory for entities.
### Version naimng idea:
for Maribel - YYYYMMDD, Year - Month - Day. 

## Terms of references
### General
* Must work for multiplayer and singleplayer
* Must have customizable settings
* Must have logging
* Must have changable parent directory
* Must work on Windows, MacOS and Linux
### System Requirements
* Minimal RAM requirement is 4GB
* Minimal CPU requirement is 2 cores
* Core memory consumption limit is 600MB
* For every hardware from minimal requirements must perform at 60FPS (based on settings)
### Graphics Requirements
* Shadows (quality varies on hardware performance)
* HDR
* SSAO/Bloom
* Antialiasing: bilinear/trilinear sample filtering, FXAA, MSAA (2 up to 15 levels), Anisotropic filtering
* Transparency based on blending
* Dynamic and static lights
* UI - multiple fonts and extended ASCII for languages
### Architecture Requirements
* Get rid of coupling issue - all game objects must be containers
* All internal logic must be separated from API level and performed internally
* Developer must operate with API endpoints - never with render loops and network loops
* Networking must be done asyncronically
* Rendering must reduce GL calls as much as possible

## How does it work
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
a) improve GUI system, make it system, not a bullshit
c) color filtering for GUI borders and edges

6. Add Graphics via LWJGL 3.
a) GUI system with constraints, with + without textures
e) post porcessing effects
f) fonts
j) fog effect

7. Add Models.
a) add animations

8. Add Sounds via OpenAL.
a) 2D sound (general sound effect)
b) 3D sound (point sound)

9. Add Commands.
a) chat with typing text
b) server-side command processor
c) access levels

10. Physics
a) gravity
b) acceleration
c) ragdoll
d) collision boxes
e) collision meshes
