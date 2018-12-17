## dom-util
Search and build jsoup elements, relative xpath queries, and Selenium web elements automatically for web service and Selenium-based Web UI testing in an easy way. Make Selenium-based UI testing much easier and more fun.

As an automation tester/quality engineer, you may find that the most boring thing in automating Web UI tests using Selenium is to construct xpaths, css selectors, or the likes.

This utility is to help reduce the effort for this process.

## How to use:
 - Locate anchor web element on a web page under test such as a label closest to the web element to search
 - Get the information about the **anchor web element**: tag name, and own text
    i.e. ```<div jsname="YRMmle" class="AxOyFc snByac" aria-hidden="true">First name</div> --> tag: div, and text: First name```
 - Get the tag name or any css selector of the element to search, i.e. 'input'
 - Pass them to a method of DomUtil. 

## Notes: 
 * Each method in dom-util often goes in 3 overloads. For instance, getWebElementExactMatch, getWebElement, getWebElementBestEffort.
 * The **ExactMatch** overload compares the text with case sensitive and equals and trimming all the tabs and spaces.
 * The normal overload tries the exact matching first and if fails it tries the case sensitive and containing and trimming all the tabs and spaces.
 * The **BestEffort** overload tries to find the element similar to what the normal overload does but even when multiple anchors have been found, it will tries hard as well. Non best effort overloads will throw 'AmbiguousAnchorElementsException' when multiple anchors have been found.   
 * If you want to further control the options, use the overloads having parameter ElementInfo where you can specify the search condition combinations of your choice.
 * There are overloads such as findElement**WithTwoAnchors** as well for the convenience. It can be used where a web page has a label (parent anchor) and then another label underneath (anchor) and then the web element you want to find. For instance, it can be used for a question then 2 radio buttons with labels 'Yes' and 'No'.

## Examples:

        String url = "https://test.com.au/dom-util-test";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);        

        //Document: jsoup document
        //Element: jsoup element
        //WebElement: Selenium web element
        DomUtil domUtil = new DomUtil();
        
        WebElement location = domUtil.getWebElement(driver, "Location", "input");
        location.sendKeys("test");      
        domUtil.getWebElementBestEffort(driver, "span", "Next", "button").click();
        domUtil.getWebElementBestEffort(driver, "Next", "button").click();
        domUtil.getWebElementBestEffort(driver, "Next", "div>button").click();
        
        String firstName = domUtil.getXpathsBestEffort(document, "div", "First name", "div>input").get(0);
        String userName = domUtil.getXpath(document, "Username", "input"); 
        //returns: "//div[div[contains(text(),'Username')]]/input[@id='1111'][@name='User Name']";
        
        String lastName = domUtil.getXpathBestEffort(document, "div", "Last name", "input");
        String middleName = domUtil.getXpath(document, "Middle Name", "div span input");
        
        Element doB = domUtil.getElement(document, "div", "Date of Birth", "input");
        Elements address = domUtil.getElements(document, "Address", "input").get(1);
        String jsNameAttributeValue = address.attr("jsname");
        
        domUtil.getWebElementWithTwoAnchorsBestEffort(driver, "div","your first time using our service?", "span", "Yes",  "input").click();

* See: https://github.com/tamnguyenbbt/dom-util/blob/master/src/test/java/com/github/tamnguyenbbt/DomUtilTest.java
        
## License
Dom-util is licensed under **Apache Software License, Version 2.0**.

## Versions
* Version **1.0.7** released on 12/17/2018
* Version **1.0.6** released on 12/12/2018
* Version **1.0.5** released on 12/11/2018
* Version **1.0.4** released on 12/10/2018
* Version **1.0.3** released on 12/06/2018
* Version **1.0.2** released on 12/05/2018
* Version **1.0.1** released on 12/04/2018
* Version **1.0.0** released on 12/02/2018

## Maven Repository

```xml
<dependency>
  <groupId>com.github.tamnguyenbbt</groupId>
  <artifactId>dom-util</artifactId>
  <version>1.0.7</version>
</dependency>
```

https://search.maven.org/artifact/com.github.tamnguyenbbt/dom-util/1.0.7/jar

## Future Development Plan
- Page Object Model class generator (code generator) for test scripts to consume
- Selenium-based Object-oriented (web element and web page oriented) automation test framework based on dom-util with open report (Report Interface)
- Comprehensive report plugin for the automation test framework

[![View My profile on LinkedIn](https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png)](https://www.linkedin.com/in/tam-nguyen-a0792930/)
