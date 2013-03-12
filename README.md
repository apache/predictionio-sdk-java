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

If your PredictionIO server is not at localhost, edit the source and replace API URLs with your PredictionIO server host.

To build these examples you will need Maven 3+.
Run the following in each example's directory, e.g.

    cd ~/PredictionIO-Java-SDK/examples/import
    mvn clean compile assembly:single
	cd ~/PredictionIO-Java-SDK/examples/client
    mvn clean compile assembly:single

These will create JAR files with all dependencies built in.

### Try It Now

First, you need to create an Application on PredictionIO and obtain an appkey.

To import the provided small sample data:

    cd ~/PredictionIO-Java-SDK/examples/import
    java -jar target/sample-import-<latest version>.jar <your appkey here> sampledata/sample1.txt 

You will then have to create an engine and make sure that it's up and running. It may take an hour for the prediction results to be generated.  

Then, you can run the sample client of various tasks:

	cd ~/PredictionIO-Java-SDK/examples/simpletasks1
    java -jar target/sample-client-<latest version>-jar-with-dependencies.jar <your appkey here> <your engine name here>

Enjoy!

Support
=========

Forum
-----

https://groups.google.com/group/predictionio-user
