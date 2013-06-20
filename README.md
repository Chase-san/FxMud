FxMud is a very simple Java Mud server.

What sets it apart from other simple Java Mud servers is that it actually works.

This mud codebase is very BARE BONES.

What it has:
-User creation
-Character creation/selection
-Seperation of Login from Characters
-Rooms
-Basic Exits
-Say
-Display of characters in rooms.

Know bugs:
-You can play a character that is already being played.
-You can login more then once.
-You can't save.
-The character will not log out (even if you disconnect).

Required Lbiraries:
-Netty-4.0.0.CR5 (More recent versions 'should' work)
-Gson-2.2.4 (But any recent version will work)