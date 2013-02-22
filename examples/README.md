PredictionIO-Java-SDK Examples
=====================

Install
--------

First edit the class files to replace the API Keys and API Urls with your test server, else you will run into problems.

To build these examples you'll need Maven 3.x.x and run in the root directory (ie: the directory you initially
checked the SDK in or within each examples main directory):

            mvn clean package

Usage
--------

Make sure you have added in your own API Key and URL into the files before compiling.

For SampleImport, usage is as follows:

        java -jar import-0.2-SNAPSHOT.jar {file you want to import}

So for example, if we were to use the import.txt included in resources:

        java -jar import-0.2-SNAPSHOT.jar ~/PredictionIO-Java-SDK/examples/import/src/main/resources/samples

For SampleClient, usage is as follows:

        java -jar client-0.2-SNAPSHOT.jar

Support
=========

help@tappingstone.com

Forum
-----

https://groups.google.com/group/predictionio-user