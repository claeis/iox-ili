iox-ili - implementation of iox API

Features
- INTERLIS 2 reader/writer (2.2+2.3)
- INTERLIS 1 reader/writer
- ILIGML writer
- model repository manager

Status
Development

How to use it?
- see javadocs

License
iox-ili is licensed under the MIT/X License.

Latest Version
The current version of iox-ili can be found at
http://www.eisenhutinformatik.ch/iox-ili/

System Configuration
In order to compile iox-ili, a JAVA software development kit (JDK) version 1.6.0 or a more recent version must be installed on your system.
A free version of the JAVA software development kit (JDK) is available at the website http://java.sun.com/j2se/.
Also required is the build tool ant. Download it from http://ant.apache.org and install it as documented there.

How to compile it?
To compile the iox-ili, change to the newly created directory and enter the following command at the commandline prompt

ant jar

To build a binary distribution, use

ant bindist

Dependencies
- ili2c
- iox-api
- JTS

Comments/Suggestions
Please send comments to ce@eisenhutinformatik.ch

