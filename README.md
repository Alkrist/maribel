# Maribel
![alt text](https://github.com/Alkrist/maribel/blob/master/logo.png)
game engine API, based on the ECS approach split for Server and Client. Every object in the game is represented as Entity with a set of Components, which later re updted in
Systems. Every Entity is created based on Game Object which has no use in the game on its own and server just as a factory for entities.
### Version naimng idea:
for Maribel - YYYYMMDD, Year - Month - Day. 

## Goals
The goal is to make a framework for an actual game project that will solve several problems:
1. Enough abstraction for ANY new content integration (in simple words, you don't have to overwrite half of the engine to add new game object or feature, everything behaves in the same way for the core point of view).
2. All common modern game graphical features and their detailed settings
3. Memory saving
4. Possibility of REAL multithreading (which is impossible in Unity Engine for example)
5. Built-in flexible server-client architecture, maybe not very user friendly though, but who cares :)
6. Tools for easier further game development (i.e. GUI constraint system, animating tools, storage tools, make networking without touching low level)
7. Might and Power

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

6. Add Graphics via LWJGL 3.
a) GUI system with constraints, with + without textures
b) particles (instanced rendering)
c) shadows
d) point lights
e) post porcessing effects
f) fonts
g) optional normal maps (checked on texture load)
h) graphics improvement (mipmaps, antialiasing etc.)
i) transparency
j) fog effect
k) texture atlases

7. Add Models.
a) add animations

8. Add Sounds via OpenAL.
a) 2D sound (general sound effect)
b) 3D sound (point sound)

9. Add Commands.
a) chat with typing text
b) server-side command processor
c) access levels
