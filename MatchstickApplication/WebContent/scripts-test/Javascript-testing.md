# Testing

Our JavaScript test suite uses node.js for testing purposes

The following command is run 
eclipse-workplace> npm install --save-dev mocha chai sinon  
This creates a folder called node_modules and populates it with these two modules.
This directory is on the same level as main project directory - to call it we need prefix "../"

Modules mocha and chai are used for writing tests.
Chai provides a couple of assertion commands
and Mocha provides a runner with a pleasing and readable output
and Sinon provides mocks and spies.

To run test classes, just execute Node Application run configuraion in Eclipse
with Node arguments
../node_modules/mocha/bin/mocha



## Full suite test

\workplace\matchstick> ..\node_modules\.bin\mocha WebContent\scripts-test