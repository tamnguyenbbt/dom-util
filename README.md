## dom-util
Search and build jsoup elements, relative xpath queries, and Selenium web elements automatically for web service and Selenium-based Web UI testing in an easy way. Make Selenium-based UI testing much easier and more fun.

As an automation tester/quality engineer, you may find that the most boring thing in automating Web UI tests using Selenium is to construct xpaths, css selectors, or the likes.

This utility is to help reduce the effort for this process.

## How to use:
 - Locate anchor web element on a web page under test such as a label closest to the web element to search
 - Get the information about the anchor web element: tag name, and own text
    i.e. ```<div jsname="YRMmle" class="AxOyFc snByac" aria-hidden="true">First name</div> --> tag: div, and text: First name```
 - Get the tag name or any css selector of the element to search, i.e. 'input'
 - Pass them to a method of DomUtil

## Examples:

        String url = "https://accounts.google.com/signup/v2/webcreateaccount?hl=en&flowName=GlifWebSignIn&flowEntry=SignUp";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        String uuid = UUID.randomUUID().toString().substring(0, 20);

        //Document: jsoup document
        //Element: jsoup element
        //WebElement: Selenium web element
        
        WebElement firstNameWebElement = DomUtil.findElement(driver, "First name", "input"); 
        firstNameWebElement.sendKeys(uuid);      
        DomUtil.findElement(driver, "Next", "button").click();
        
        String xpath1 = DomUtil.getXPaths(document, "div", "First name", "input").get(0);
        String xpath2 = DomUtil.getXPaths(document, "Username", "input").get(0); //returns: "//div[div[contains(text(),'Username')]]/input";
        
        String xpath3 = DomUtil.getXPath(document, "div", "First name", "input");
        String xpath4 = DomUtil.getXPath(document, "First name", "input");
        
        Element firstNameTextBox1 = DomUtil.getClosestElements(document, "div", "First name", "input").get(0);
        Element firstNameTextBox2 = DomUtil.getClosestElements(document, "First name", "input").get(0);
        String jsNameAttributeValue = firstNameTextBox1.attr("jsname");
        
## License
Dom-util is licensed under **Apache Software License, Version 2.0**.

## Versions

* Version **1.0.1** released on 12/04/2018 
  --> add search by AnchorElementInfo class; add support for the case where anchor element is also the search element
* Version **1.0.0** released on 12/02/2018 
  --> First version

## Maven Repository

```xml
<dependency>
  <groupId>com.github.tamnguyenbbt</groupId>
  <artifactId>dom-util</artifactId>
  <version>1.0.0</version>
</dependency>
```

https://search.maven.org/artifact/com.github.tamnguyenbbt/dom-util/1.0.0/jar

[![View My profile on LinkedIn](https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png)](https://www.linkedin.com/in/tam-nguyen-a0792930/)
