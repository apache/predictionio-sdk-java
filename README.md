PredictionIO Java SDK
=====================

Getting Started
---------------

### By Maven

If you have a Maven project, simply add the dependency to your `pom.xml`.

    <project ...>
        ...
        <dependencies>
            <dependency>
                <groupId>io.prediction</groupId>
                <artifactId>client</artifactId>
                <version>0.4</version>
            </dependency>
        </dependencies>
        ...

### By Ivy

If you use Ivy, simply add the dependency to your `ivy.xml`.

    <ivy-module ...>
        ...
        <dependencies>
            <dependency org="io.prediction" name="client" rev="0.4" />
            ...
        </dependencies>
        ...

### sbt

If you have an sbt project, add the library dependency to your build definition.

    libraryDependencies += "io.prediction" % "client" % "0.4"

### Building from Source

Assuming you are cloning to your home directory.

    cd ~
    git clone git://github.com/PredictionIO/PredictionIO-Java-SDK.git

To build this SDK you will need Maven 3+. Run the following to publish the module to your local Maven repository.

    cd ~/PredictionIO-Java-SDK
    mvn clean install

Run the following to generate API documentation.

    cd ~/PredictionIO-Java-SDK
    mvn clean javadoc:javadoc

Examples
--------

### Download Source

If you have not already cloned the repository from the section above, do

    cd ~
    git clone git://github.com/PredictionIO/PredictionIO-Java-SDK.git

### Running the Sample Android Client

Detailed instructions can be found at our [main documentation site](http://docs.prediction.io/tutorials/android-client.html).

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
=======

Forum
-----

https://groups.google.com/group/predictionio-user
