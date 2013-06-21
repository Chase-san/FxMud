FxMud is a very simple Java Mud server.

A long time I worked as a programmer on MUDS. I thought it was interesting.
These days I muds are probably about dead. But there has always been a part of me that
always wanted to design a MUD from scratch. At one point I attempted it, but didn't get
very far at all.

This is my new attempt and it has been much easier and simpler to do this time around.

I notice most other java codebases are either really complex or in a non-working state.
The mud I have seen are basically copy of MUDs made in C and ported over to Java.
Meaning they do a lot of things wrong way for Java.

So I decided to make a clean, small, simple but Complete mud codebase that anyone who
has a love for Java can use.

This mud codebase is currently very BARE BONES.

Current State:

Currently you can get login (or create a login), create a character and login.
You can walk through the rooms and use a handful of very simple commands.


Required Libraries:
- Netty-4.0.0.CR5 (More recent versions 'should' work)
- Gson-2.2.4 (But any recent version will work)