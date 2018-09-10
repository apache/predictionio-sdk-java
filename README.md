# Apache PredictionIO Java SDK

This bulk of this README is divided into two sections: Using the SDK and developing the SDK.
Choose the one the suits you. For support please see the bottom of this README.

# Using the SDK

## With Maven

If you have a Maven project, simply add the dependency to your `pom.xml`.

```XML
<project ...>
    ...
    <dependencies>
        <dependency>
            <groupId>org.apache.predictionio</groupId>
            <artifactId>predictionio-sdk-java-client</artifactId>
            <version>0.13.0</version>
        </dependency>
    </dependencies>
    ...
```


## With Ivy

If you use Ivy, simply add the dependency to your `ivy.xml`.

```XML
<ivy-module ...>
    ...
    <dependencies>
        <dependency org="org.apache.predictionio" name="predictionio-sdk-java-client" rev="0.13.0" />
        ...
    </dependencies>
    ...
```


## With sbt

If you have an sbt project, add the library dependency to your build definition.

```Scala
libraryDependencies += "org.apache.predictionio" % "predictionio-sdk-java-client" % "0.13.0"
```


## Examples

Please check out the examples under `examples/`.


# Developing SDK - Building from Source

Fork and clone from GitHub. The following assumes you are cloning to your home directory.

```sh
cd ~
git clone https://github.com/<your_github_handle>/predictionio-sdk-java.git
```

To build this SDK you will need Maven 3+. Run the following to publish the module to your local
Maven repository.

```sh
cd ~/predictionio-sdk-java
mvn clean install
```

Run the following to generate API documentation.

```sh
mvn javadoc:javadoc
```


# Running CLI Examples


## Building

If your PredictionIO server is not at `localhost`, edit the source and replace API URLs with your 
redictionIO server host.

To build these examples you will need Maven 3+. Run the following in each example's directory, e.g.

```sh
cd ~/predictionio-sdk-java/examples/quickstart_import
mvn clean compile assembly:single
cd ~/predictionio-sdk-java/examples/import
mvn clean compile assembly:single
```

These will create JAR files with all dependencies built in.


## Try It Now

For running the quick start example (`quickstart_import`), please refer to the "Quick Start" page of
the PredictionIO documentation. Most importantly, create an App with `pio new app MyApp` and take
note of the `Access Key` produced, which will be `<your accessKey here>` in the following.

For `quickstart_import`,

```sh
cd ~/predictionio-sdk-java/examples/quickstart_import
java -jar target/quickstart-import-<latest version>-jar-with-dependencies.jar <your accessKey here>
```

To check the data has been imported successfully, run
```sh
curl -i -X GET http://localhost:7070/events.json?accessKey=<your accessKey here>
```

To import the provided small sample data for the import example using asynchronous calls:

```sh
cd ~/predictionio-sdk-java/examples/import
java -jar target/sample-import-<latest version>-jar-with-dependencies.jar <your accessKey here> sampledata/sample1.txt
```

To check the data is imported properly, run
```sh
curl -i -X GET http://localhost:7070/events.json?accessKey=<your accessKey here>
```

Enjoy!


# Support


## Bugs and Feature Requests

Use [Apache JIRA](https://issues.apache.org/jira/browse/PIO) to report bugs or request new features.


## Community

Keep track of development and community news.

*   Subscribe to the user mailing list <mailto:user-subscribe@predictionio.apache.org>
    and the dev mailing list <mailto:dev-subscribe@predictionio.apache.org>
*   Follow [@predictionio](https://twitter.com/predictionio) on Twitter.


## Contributing

Read the [Contribute Code](http://predictionio.apache.org/community/contribute-code/) page.


## License

Apache PredictionIO is under [Apache 2
license](http://www.apache.org/licenses/LICENSE-2.0.html).