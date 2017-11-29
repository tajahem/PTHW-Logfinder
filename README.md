#LogFinder#
---
A Java implementation of the first project described on (the no longer existing) Projects The Hard Way website. <br />
Log finder is a command line program for Linux that searches for the supplied pattern(s) in log files. The types of files and directories searched are described in configuration files._*see Log Types and Directories below_ 

##Patterns##
Any argument(s) after the options is(are) considered a search pattern. By default LogFinder will search for each pattern supplied using _and_ logic. You can, however, use _or_ logic when you pass the **-o** option.
Patterns may be:<br /> 

* words
* phrases (surrounded by quotes)
* java regular expressions

You may also supply patterns prefixed with **~~** to exclude any results that contain the specified pattern.

####Regular Expressions in Patterns####	
Regular expressions will either need to escape escape characters (_\\s_ instead of _\s_ for a whitespace character) or enclose the regex in double or single quotation marks. <br /> 
_eg: test\\slog(s)? and "test\slog(s)?" are equivalent_<br />
For more on Java regular expressions see [this link](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)

##Log Types##
File types are specified in either **/etc/logfinder/logfiles** or a file supplied with the **-l** or **--logfiles** option. Both regular expression and glob syntax are supported. Glob syntax must be prefixed with _glob:_. Regular expression syntax may also be specified with the prefix _regex:_, however, this is optional.

_eg:_
	
	glob:*.log
	\w{1,}log.\d.gz

##Directories##
