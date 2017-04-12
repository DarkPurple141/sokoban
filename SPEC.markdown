% COMP2911 17s1 / Assignment 3

<header class="jumbotron text-center">

warehouse boss
==============

<p class="lead">
__Marks:__ 5+15 marks (worth 20%)  
__Submit:__ `2911 classrun 17s1 give ass3`  
__Due:__ 23:59 Friday, 26th May 2017
</p>
</header>

 - Gain experience working in a small team-based environment
 - Increase familiarity with problem-solving algorithms
 - Appreciate issues in user interface design
 - Learn practical aspects of graphical user interface programming
 - Learn more about the Java class libraries

Your task is to create an interactive puzzle game where the objective
is for a character to move a number of boxes into the same number of
goal squares.


### The Rules ###

The playing area is a (possibly irregularly shaped) grid bounded by
external walls and possibly including internal walls.

In the basic game, the grid initially contains a number of boxes, the
same number of designated goal squares, and a character for the
player.  The game ends when the player has moved all the boxes onto
goal squares.

The character and the boxes can move only horizontally and vertically,
and a square can only be occupied by either the character or one
box.

The player can only push (not pull) boxes, and can only push one box
at a time: to push a box, the player must be adjacent to the box, then
moves onto that square; the box, in turn, moves to the next square in
the same direction, but only if it is not blocked by a wall or by
another box.

Further variations on the basic game could be to have multiple
players, a variety of levels, different sorts of obstacles, the
necessity to collect items, limiting the number of available moves,
etc.

Two basic games are shown below. The images show an overhead view of the
warehouse. Walls are in grey, the character is near the upper left
corner, boxes are yellow, and the goal squares are designated by green
crosses. Note that in the first game position, the green box is already
located at a goal square.

<center>

![Image 1](image1.png)

![Image 2](image2.png)

</center>

In this assignment, the aim is to construct an _interactive_ puzzle
game that provides an interesting and challenging experience for the
player. The primary requirement is not to develop an efficient problem
solver (though that could be useful as part of the application), but
to understand and anticipate user requirements, and to design a
graphical user interface so that players of varying degrees of
expertise can interact effectively with the system.

<center>
__\*All programming should be done using standard Java.\*__  
__\*Do not use Java OpenGL or any similar graphics library.\*__
</center>


### Working in Teams ###

In this assignment, you will work in teams of __five students enrolled
in your tut-lab__ and follow the Scrum process.  You will need to
assign Scrum roles within the team and formulate an agile plan for
your project (though all members should take on the developer role). A
number of questions to help you determine the initial scope of the
project are:

 - who are the intended users: are they novice, intermediate or expert,
   or all sorts of users (do we even understand what this means?)

 - what sort of features are basic to the application (i.e. needed by
   every user)?

 - what sort of features are needed by different categories of users:
   how can the interface handle seemingly different requirements?

 - what sort of help or hints (if any) should the system be able to
   provide to users (and how and when is this help given)?

 - what platforms with what form factors is the system designed to run
   on?

Of course your scope will also include some basic "back-end" operations
such as generating new puzzles, computing solutions, etc., as required.

You will be guided by your tutor throughout this project (think of
your tutor as a representative client).  You will be assessed
individually on the degree to which you are following good teamwork
practice, including contributing to software development.  To record
your contribution, you should maintain an _individual_ diary that
contains your personal record of the project, answering, **for each
day**, the three standard daily standup questions:

 - what did I do yesterday?
 - what will I do today?
 - is anything in my way?

You will need to show this to your tutor weekly to obtain your
individual mark for the project.  Special arrangements will be made
for Anzac Day.

There are no set times for producing intermediate outcomes because your
project plan is an agile plan of your own devising, and is subject to
revision, however your tutor will advise you if your team seems to be
falling behind the anticipated schedule in order to achieve a mimimum
viable product.

To help facilitate progress, the Week 10 tut-lab will be devoted to
sprint reviews: each team will present their progress (no slides) and
the current status of the plan; the rest of the class is expected to
provide feedback.  There are no marks for this activity, however the
aim of the sprint review is to improve the quality of the system you
eventually submit at the end of the project.

The team mark is for the final product.  As a team, you will
demonstrate your system to __two assessors__ (your tutor, and another
member of course staff -- possibly a lecturer) in the tut-lab in
Week 13.

Your submission should include two Unix executable files, `compile`
and `run`.

 - When `compile` is called, your program should be compiled from the
   command line using _javac_ commands.

 - When `run` is called, your game should be executed using _java_
   commands.  Make sure that all resource files (such as sounds and
   images) are loaded correctly, and that any files used by your
   program are saved locally and can be reloaded (for example, do not
   save such files in the user's home directory, or in a parent
   directory).

Note that your software will be compiled and run on the CSE machines
and/or other machines for further manual testing.

Assessment will be based on effective interaction and user interface
design, as well as correctness, generality and software design (see
below for details).  Your team presentation should __not__ be a "sales
pitch": your audience are all professional software engineers who are
experts in object-oriented design, and may ask you questions about
your design.  You are not presenting your software to the intended
users of your system.

There is no one "best" solution to this assignment: what is important
is to produce a working system intended for some class of users and be
able to justify your design decisions.

It is an important part of this project for you, as a team, to manage
your own time.  You need to start early and work consistently: do not
be overly optimistic about what you can achieve: remember, you are all
doing other courses with other assignments. It is essential for you as
a team to monitor your own progress and it is often necessary to
revise the plan after a sprint review.  The most basic requirement is
to develop a minimum viable product. Extensions can be added according
to the time available.


Submission
----------

Submit __one__ `ass3.zip` file per team.  Your `ass3.zip` should
contain:

 - All your `.java` source files, perhaps in some subdirectory.

 - Any resources (such as sound or image files) in an appropriate
   subdirectory (not necessarily where Eclipse stores them)

 - A PDF file containing your team's design documents (a UML class
   diagram and, optionally, other diagrams necessary to understand
   your design)

 - A Unix executable file called `compile` at the top level which,
   when run, compiles your Java program from the command line.

 - A Unix executable file called `run` at the top level which, when
   run, executes your game from the command line

You may submit from a CSE workstation or login server by running

    $ 2911 classrun 17s1 give ass3 ass3.zip

You can check your submission by running

    $ 2911 classrun 17s1 check ass3

You will be able to collect your assignment once marked by running

    $ 2911 classrun 17s1 collect ass3

You may also submit, check, and collect from Give online.

Aim to complete your assignment well before the due date, in case of
last-minute illness or misadventure.  Make regular backups of your
work.

You may submit multiple times; only the last version will be marked.
Submit partial solutions, and submit early; systems are usually
heavily loaded close to assignment deadlines, and we will not grant
special considerations for late submissions due to system delays close
to the deadline.


Assessment
----------

Your mark comprises an individual component, worth 5%, and a team
component, worth 15%, making up the total 20% value of this
assessment.

### Individual Mark ###

The individual mark assesses contributions to teamwork and software
development.

You should use as evidence a reflective diary shown to your tutor each
week; it is your responsibility to show your diary to your tutor each
week.  This is worth 1% for each of weeks 8, 9, 10, 11 and 12,
determined weekly and finalised at the end of semester.

Your diary should include information about

 - _Project scope_: meeting schedule, record of decisions, etc.

 - _Project design_: UML class diagram, walkthroughs, mock-up
    interfaces (on paper), etc.

 - _Project plan_: work allocation, timeline for project completion,
   testing and integration plan, etc.

### Team Mark ###

The team-based assessment includes a presentation/demonstration of the
final system (held in Week 13).

The mark will be determined at the end of semester, based on the final
project as determined from your team presentation and further testing.
Note that marks for extensions may be reduced if the minimum viable
product is not fully working.

Each member of a team _generally_ receives the same mark for the team
component of the project; however, where individual contributions to
software development are highly unequal, individual marks for this
component will be adjusted to reflect the level of contribution.

Marks will be awarded across the following areas:

Correctness (3 marks)
:    1: Serious bugs; 
        system crashes during demo;
        `compile` or `run` don't work;  
     2: Minor bugs; 
        some strange behaviour (e.g. runs only on one platform)  
     3: No bugs seen during demo/testing

Interaction (3 marks)
:    1: Very basic user interface  
     2: Easy to use interface with a range of features  
     3: Smooth, responsive, intuitive, well designed user interface

Design (3 marks)
:    1: Messy design and diagrams;
        design inconsistent with code  
     2: Clear design and diagrams;
        partial adherence to design principles  
     3: Clear design and diagrams
        fully adhering to design principles and conforming to code

Generality (3 marks)
:    1: Predefined or simply generated puzzles  
     2: Algorithm that generates reasonable puzzles  
     3: Algorithm that generates interesting and challenging puzzles

Extensions (3 marks)
:    1: Only handles basic game play  
     2: Some extensions such as animation, multi-player, etc.  
     3: More than one type of *significant* extension


Plagiarism
----------

Before submitting any work you should read the Student Guide on
[Academic Integrity and Plagiarism](https://student.unsw.edu.au/plagiarism).

All work submitted for assessment must be entirely your team's work.
We regard unacknowledged copying of material, in whole or in part, as
an extremely serious offence with heavy penalties up to and including
exclusion from further study at UNSW.  For further information, see
the Course Outline.

