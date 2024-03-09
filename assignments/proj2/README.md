## Proj2
1. Create random number of rooms.<br>
    - The width and height should be odd.
    - Rooms should not be placed on boundaries.
    - Rooms should not overlap each other.
2. Use prim to connect these rooms.<br>
### Run from the command line
javac -encoding UTF-8 -cp "lib/*;." byog/Core/Main.java byog/TileEngine/*.java byog/Core/Game.java -d bin<br>
java -cp "lib/*;bin" byog.Core.Main n123:q
### Reference
- [Rooms and Mazes: A Procedural Dungeon Generator](https://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/)

