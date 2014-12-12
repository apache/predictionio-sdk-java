PredictionIO Java SDK
=====================

This bulk of this README is divided into two sections: Using the SDK and Developing the SDK.
Choose the one the suits you. For Support please see the bottom of this README.

Using the SDK
=============


With Maven
----------

If you have a Maven project, simply add the dependency to your `pom.xml`.

```XML
<project ...>
    ...
    <dependencies>
        <dependency>
            <groupId>io.prediction</groupId>
            <artifactId>client</artifactId>
            <version>0.8.3</version>
        </dependency>
    </dependencies>
    ...
```


With Ivy
--------

If you use Ivy, simply add the dependency to your `ivy.xml`.

```XML
<ivy-module ...>
    ...
    <dependencies>
        <dependency org="io.prediction" name="client" rev="0.8.3" />
        ...
    </dependencies>
    ...
```


With sbt
--------

If you have an sbt project, add the library dependency to your build definition.

```Scala
libraryDependencies += "io.prediction" % "client" % "0.8.3"
```


Examples
--------

Please check out the examples under `examples/`.


Developing SDK - Building from Source
=====================================

Assuming you are cloning to your home directory.

```sh
cd ~
git clone git://github.com/PredictionIO/PredictionIO-Java-SDK.git
```

To build this SDK you will need Maven 3+. Run the following to publish the module to your local Maven repository.

```sh
cd ~/PredictionIO-Java-SDK
mvn clean install
```

Run the following to generate API documentation.

```sh
cd ~/PredictionIO-Java-SDK
mvn clean javadoc:javadoc
```

Using the Published Module with Maven
-------------------------------------

Simply add the dependency to your `pom.xml`.

```XML
<project ...>
    ...
    <dependencies>
        <dependency>
            <groupId>io.prediction</groupId>
            <artifactId>client</artifactId>
            <version>0.8.4-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ...
```


Running CLI Examples
--------------------


### Building

If your PredictionIO server is not at localhost, edit the source and replace
API URLs with your PredictionIO server host.

To build these examples you will need Maven 3+.
Run the following in each example's directory, e.g.

```sh
cd ~/PredictionIO-Java-SDK/examples/quickstart_import
mvn clean compile assembly:single
cd ~/PredictionIO-Java-SDK/examples/import
mvn clean compile assembly:single
```

These will create JAR files with all dependencies built in.


### Try It Now

For running the quick start example (quickstart_import),
please refer to the "Quick Start" page of the PredictionIO documentation.
Most importantly, create an App with `pio new app MyApp` and take note of the
`Access Key` produced, which will be `<your accessKey here>` in the following.

For `quickstart_import`,

```sh
cd ~/PredictionIO-Java-SDK/examples/quickstart_import
java -jar target/quickstart-import-<latest version>-jar-with-dependencies.jar <your accessKey here>
```

To check the data has been imported successfully, run
```sh
curl -i -X GET http://localhost:7070/events.json?accessKey=<your accessKey here>
```

To import the provided small sample data for the import example using asynchronous calls:

```sh
cd ~/PredictionIO-Java-SDK/examples/import
java -jar target/sample-import-<latest version>-jar-with-dependencies.jar <your accessKey here> sampledata/sample1.txt
```

To check the data is imported properly, run
```sh
curl -i -X GET http://localhost:7070/events.json?accessKey=<your accessKey here>
```

Enjoy!


Support
=======


Forum
-----

https://groups.google.com/group/predictionio-user


Issue Tracker
-------------

https://predictionio.atlassian.net

If you are unsure whether a behavior is an issue, bringing it up in the forum is highly encouraged.
