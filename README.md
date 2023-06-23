# PROJECT 1
## Problem:
* Topic 7: Build a program to manage Slack services and synchronize data to AirTable
* Requirements:
	* Using [JAVA](https://en.wikipedia.org/wiki/Java_(programming_language)) language
	* Exploring Slack, AirTable and its API
	* Accessing integrate Slack User and Slack Channel to AirTable
	* Document: [slack API](https://api.slack.com/methods)


## Project planing: [details](./Figure/Project%20plan%20Topic%207%20-%20Group%2012.pdf)
* Design program:
	* Flow chart: [Flow chart](./Figure/flow_chart.png)
	* Use case: [Use case diagram](./Figure/use_case.png)
	* Modeling: [Entity - Relationship Model](./Figure/model.png)

## Usage:
* Setup environments:
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
	<dependency>
		<groupId>org.apache.httpcomponents</groupId>
		<artifactId>fluent-hc</artifactId>
		<version>4.5.14</version>
	</dependency>
	<dependency>
		<groupId>org.json</groupId>
		<artifactId>json</artifactId>
		<version>20230227</version>
	</dependency>
<!-- Progress bar -->
	<dependency>
		<groupId>me.tongfei</groupId>
		<artifactId>progressbar</artifactId>
		<version>0.9.4</version>
	</dependency>
```