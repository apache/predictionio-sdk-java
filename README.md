PredictionIO Java SDK
=====================

Installation
------------

Assuming you are cloning to your home directory.

    cd ~
    git clone git://github.com/PredictionIO/PredictionIO-Java-SDK.git

To build this SDK you will need Maven 3+. Run the following to publish the module to your local Maven repository.

	cd ~/PredictionIO-Java-SDK
    mvn clean install

Run the following to generate API documentation.

	cd ~/PredictionIO-Java-SDK
    mvn clean javadoc:javadoc

Usage
-----

Import in the created JAR file through your dependency manager of choice, Maven, Ivy, etc or place it in your application classpath.

Refer to the examples to see basic usage.

Examples
--------

### Building

Edit the source and replace App keys and API URLs with your test server.

To build these examples you will need Maven 3+.
Run the following in each example's directory, e.g.

	cd ~/PredictionIO-Java-SDK/examples/client
    mvn clean compile assembly:single

This will create one JAR file with all dependencies built in.

### Usage

For SampleClient, usage is as follows.

	cd ~/PredictionIO-Java-SDK/examples/client
    java -jar target/sample-client-0.3-SNAPSHOT-jar-with-dependencies.jar


Support
=========

Forum
-----

https://groups.google.com/group/predictionio-user
