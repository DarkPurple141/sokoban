So in the interests of people knowing what that weird build system I
keep fidgeting with is, lemme walk through it.

`build.xml` is the configuration for the Ant build tool
(<http://ant.apache.org/>): Ant is one of the two fairly mainstream
pure-Java build systems out there, and fills a similar niche to Make,
but in the Java world. It knows how to build things, and can be told
what needs to be built to where.  It's also got some neat hooks to do
testing, distribution, documentation building, etc. etc. etc.  Ant's
available near most Java installations; it's definitely available on
CSE.

I've got it configured so that:

- `ant build` compiles the code in _src/_ to _bin/_,
- `ant dist` creates a runnable, versioned Jar file in _dist/lib/_,
- `ant doc` generates Javadoc documentation into _doc/_,
- `ant run` launches the `wb.WarehouseBoss` class,
- `ant test` runs JUnit tests.

The `compile` and `run` scripts are consequently very simple: they
just call Ant.


To add new tests, add a line like

    <test name="wb.tests.TestName" />

in the `junit` call of the `test` target.
