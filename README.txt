== Team Members ==
Stewart Adam - #1710176
Josh Whatley - #9643877
Simon Larocque - #6320716
Guillaume Fortin - #6325300
Derek Ringuette - #1947843

== Environment setup ==
JARs used:
* commons-codec-1.6.jar
* commons-logging-1.2.jar
* fluent-hc-4.3.6.jar
* httpclient-4.3.6.jar
* httpclient-cache-4.3.6.jar
* httpcore-4.3.3.jar
* httpmime-4.3.6.jar
* mysql-connector-java-5.0.8-bin.jar
* SoenEA2.jar
* taglibs-standard-compat-1.2.1.jar
* taglibs-standard-impl-1.2.1.jar
* taglibs-standard-jstlel-1.2.1.jar
* taglibs-standard-spec-1.2.1.jar
* xmlunit-1.5.jar

In addition to the above, Eclipse was setup with these libraries:
* Apache Tomcat v8.0
* JRE System Library
* JUnit 4

== Internal documentation for team members ==
This archive can be imported by:
1. File > New > Dynamic Web Project
2. Uncheck default file location
3. Enter path to this archive
4. Click Finish

The test bench can be run by:
1. Copy MyResources.properties.sample to MyResources.properties and fill in
   local DB details
2. org.soen387.test/Setup contains the DB initialization sequence
3. org.soen387.test.ser/ManualTest runs the test bench
4. org.soen387.test/Teardown contains the database table destruction sequence
