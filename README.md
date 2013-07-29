cucumber-json plugin
======================

This small library adds a common cucumber DSL for writing cucumber tests to test restfull endpoints.  The library
provides the following:

* Step definition library
* Communication library (groovy http builder)
* Grails aware aimed to work with hauner/grails-cucumber

The key component of this library is a consistent way of writing gherkin syntax specifically around the common actions
associated with setting up, transmitting and interrogating restfull transactions.

Installation 
=============

```bash
git clone http://github.com/grantcurrey/cucumber-rest
cd cucumber-rest
gradle install
```

In your project simply add the dependency ```"com.wotifgroup.cucumber-json:cucumber-json:0.2.2"```

Config / Usage
===============

There are a number of ways to bootstrap a cucumber test suite. This guide will assume you are using cucumber-junit.  But
the key concepts presented are applicable to all frameworks.

Given the following example:

```java
package com.wotifgroup.cucumber.example;
 
import org.junit.runner.RunWith;
import cucumber.junit.Cucumber;
 
@RunWith(Cucumber.class)
@Cucumber.Options(format={"pretty", "html:target/cucumber"}, glue={"com.wotifgroup.cucumber.jsonglue"})
public class RunTests {
}
```

Note, the Cucumber.Options glue property set above.  In order to leverage the hooks and step definitions provided by
this libary you need to tell the cucumber jvm where to look for the definitions.

Grails Users
------------

When your cucumber tests startup, a Hooks file executes which interogates your project and determines if you are 
using grails or not.  If you are, it reads your Config.groovy file and sets the base url automaticly for you based on
the ```conf.grails.serverUrl``` setting (more in the Provided Step Definitions section).  

You can override this using the ```the base url is "http://localhost:8080/someurl"``` in your gherkin file.


Provided Step Definitions
==========================

**The following are intended to be used in a Background action:**

- the date format is "(.*)"
- the base url is \"(.*)\"
- the ssl keystore is \"(.*)\" and the keystore password is \"(.*)\"
- the ssl truststore is \"(.*)\" and the truststore password is \"(.*)\"

**The remaining are intended to be used anywhere in a Gherkin file:**

- I am sending a \"(.*)\"
- I set the request \"(.*)\" property to (.*)
- I add \"(.*)\" to the request \"(.*)\" property
- I (remove|clear|nullify) the request \"(.*)\" property
- I post the "([\w ]+)" to "(.*)"
- I get the "([\w ]+)" to "(.*)"
- I put the "([\w ]+)" to "(.*)"
- I delete the "([\w ]+)" to "(.*)"
- the http response code is "([0-9]*)"
- the response property \"(.*)\" has a value
- the response property "(.*)" equals ("?.*"?)
- the response property \"(.*)\" is (\"?.*\"?)
- the response property \"(.*)\" contains \"?(.*)\"?


