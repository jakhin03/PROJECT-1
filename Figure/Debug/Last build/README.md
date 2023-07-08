## Bugs fixing:
	* Bug #1: add longcasting to parameter at Main.java:125
	* Bug #2: add if-else instruction to prevent nullPointerException at SlackDataFetching:70
## Security fixing:
	* MinorSecurity: rename package from airtableAPI to airtableapi
	* MajorSecurity: define token or secret variables in a config file, and access to it by call java.util.Properties function.