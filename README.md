# PROJECT 1
## Problem:
* Topic 7: Build a program to manage Slack services and synchronize data to AirTable
* Requirements:
	* Using [JAVA](https://en.wikipedia.org/wiki/Java_(programming_language)) language
	* Exploring Slack, AirTable and its API
	* Accessing integrate Slack User and Slack Channel to AirTable
	* Document: [slack API](https://api.slack.com/methods), [Airtable API](https://airtable.com/developers/web/api/)


## Project planing: [details](./Figure/Project%20plan%20Topic%207%20-%20Group%2012.pdf)
* Diagrams:
	* Usecase diagram: [Usecase diagram](./App/Requirement/UseCase%20Diagram.png)
	* Class diagram: [Class diagram](./App/Design/Class%20Diagram.png)
	* Activity diagram: [Activity diagram](./App/Design/Activity%20Diagram.png)


## Setup environments:
* Settings: Can using default [pom.xml](./App/SlackAirtableSyncing/pom.xml) file
	* GroupId: ```com.project```
	* ArtifactId: ```slackairtablesyncing```
	* Dependencies: 
```
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.11</version>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>com.slack.api</groupId>
		<artifactId>slack-api-client</artifactId>
		<version>1.29.2</version>
	</dependency>
	<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
	<dependency>
		<groupId>org.slf4j</groupId>
		<artifactId>slf4j-api</artifactId>
		<version>2.0.7</version>
	</dependency>
	<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
	<dependency>
		<groupId>org.slf4j</groupId>
		<artifactId>slf4j-simple</artifactId>
		<version>2.0.7</version>
	</dependency>
	<dependency>
		<groupId>org.apache.httpcomponents</groupId>
		<artifactId>httpclient</artifactId>
		<version>4.5.14</version>
	</dependency>
	<dependency>
		<groupId>org.json</groupId>
		<artifactId>json</artifactId>
		<version>20230227</version>
	</dependency>
```
* Airtable database:
	* Create table Channels:
		* Fields: name, id, topic, description, creator, createDaate, privacy, status, is_deleted, Users (linked to Users table)
	* Create table Users:
		* Fields: name, id, displayName, fullName, email, status, role, userCreateDate, statusChangeDate, is_deleted
  	* Create table Logs:
  	  	* Fields: status, Task, Submitted, Started, Finished, Duration


## Usage:
* Configuration:
	* All credentials and token are required in [config](./App/SlackAirtableSyncing/Credentials/config.properties) file
* Compiler:
	* Recommended compiler: [Eclipse IDE](https://download.eclipse.org/) and [Intellij IDEA](https://www.jetbrains.com/idea/download/)
* Run configuration:
	* Project folder: [SlackAirtableSyncing](./App/SlackAirtableSyncing/)
	* Source folder: [src](./App/SlackAirtableSyncing/src/)
	* Main class: com.project.main.Main
