# IDEA EDU Course ...

Implemented in the Java Core Track of hyperskill.org's JetBrain Academy.

Purpose of doing this project, is learning and practising tree data structures, graph representations, Dijkstra algorithm -
and also just some more plain java.

Also: This is one graduate project of the Java Core Track, which covers the most yet undone learning topics.

## Technology / External Libraries

- POJO Java 19,
- with Lombok annotation processors,
- Apache Log4j SLF4J API binding to Log4j 2 logging and
- com.google.gson Library
- Junit 5 with
- Mockito (mockito-inline) testing.

## Repository Contents

The sources of main project tasks (6 stages) and unit, mockito testing.

## Program description

A application that models and analyzes arbitrary metro systems, finds shortest routes, travel time estimates and more...

The application reads in a .json file (compare the examples given under the src/main/resources or src/test/resources - e.g.
for the London or Prague-city metro) and start a Command Line Interpreter to have the user output, change lines and - last but not least - route shortest routes from source to target station given.

CL-Command overview:

> /fastest-route "[line 1]" "[station 1]" "[line 2]" "[station 2]". -> program prints every passed station and every transition on the <b>fastest</b> route determined using Dijkstra Algorithm.

> /route "[line 1]" "[station 1]" "[line 2]" "[station 2]". -> program prints every passed station and every transition on the <b>shortest</b> route determined using the Breadth-First-Search.

> /connect "[line 1]" "[station 1]" "[line 2]" "[station 2]". -> a transfer connection between the given stations on different lines is added.

> /append, /add "[line name]" "[station name]" "[time]". -> a new station is added to the end of the line with a given travel time to the last station.

> /add-head "[line name]" "[station name]" "[time]". -> a new station is added to the head (start) of the line with a given travel time to the next station.

> /remove "[line name]" "[station name]". -> the given station is removed from the line.

> /output "[line name]". -> line information output for all stations and transfers.

> /exit. -> terminate program loop

Have fun!

## Project completion

# Project was completed on 19.06.22.

## Progress

23.05.22 Project started.

29.05.22 Stage 1 completed with (almost) full-coverage Junit / Mockito testing. Functionality provided
is still very reduced to just reading a stations input file into a list and doing some specified output by use
of a ListIterator.

07.06.22 Stage 2 completed using Google's GSON library - esp. the tree API with JsonParser, JsonElement, etc.. 
Implementation of a commandLineInterpreter added using the Command pattern. Testing complete now.

07.06.22 Stage 3 completed with more OO model classes. Now the metro lines can be connected via transfer stations. Json
structure more complicated - nice stage.

12.06.22 Stage 4 completed adding arbitrary A to B station routing, using the BFS (breadth first search) tree traversal
algorithm

16.06.22 Stage 5 completed adding fastest route tracking using Dijkstra Algorithm for (here: travel time) weighted graphs.
use of PriorityQueue class with Comparator constructor parameter.

19.06.22 Final Stage 6 completed adding functionality of arbitrary loops, bifurcations/branches in metro lines. Thus being
able to represent realistically the London subway and find fastest and shortest routes in 500 stations on 14 lines. GREAT!
<br> I adapted the program to handle both input formats of the previous and last stages, because this stage 6 changed the json
structure significantly.