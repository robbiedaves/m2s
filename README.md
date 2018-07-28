# Maven Plugin Test



## Resources
I took some code from the following paypal repo
https://github.com/paypal/SeLion/blob/develop/codegen/src/main/java/com/paypal/selion/plugins/CodeGeneratorHelper.java
https://github.com/paypal/SeLion/blob/develop/codegen-maven/src/main/java/com/paypal/selion/plugins/CodeGeneratorMojo.java

## Using the code gen plugin
Add a model folder to the resource folder of your project then add the following to the pom
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.robxx</groupId>
                <artifactId>gensrc-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <msg>model.yml</msg>
                    <modelDirectory></modelDirectory>
                    <basePackage>com.robxx.gen</basePackage>
                </configuration>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>gensrc</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
You can override the default model directory and the base package of the generated code


## How to debug a maven plugin
https://medium.com/@prabushi/debug-maven-plugin-while-the-application-is-executed-b602ea2803f8

Debug Maven plugin while the application is executed
Here are the steps to be followed if a Maven plugin is to be debugged using the intelliJ.

- Open the Maven plugin source code in an intelliJ window.
- Place break points in the source code where you want to debug.
- Create a remote debug configuration for port 8000. Follow the path Run>Edit Configurations. Then click on ‘+’ and select ‘remote’ from the dropdown list. Within the configuration window, change the ‘port’ to 8000. Click on ‘apply’ and ‘ok’ to create the configuration.
- Go to the application directory location, using the terminal.
- Use the command, “mvnDebug clean install”.
- In the intelliJ window, select the created debug configuration and click on the debug button. According to the terminal logs, you will see that the application is getting executed, and the execution will be paused when your break point is hit. Now you can continue debugging the Maven plugin.


## Getting the generated-sources to compile before the src/main/java does

I found a page that suggested to add the following to the project using the codegen plugin
```xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>3.0.0</version>
                <executions>
                    <execution>
                        <id>add-source</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>${project.build.directory}/generated-sources/</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```
However, I didn't like the dev having to add this to the pom each time.
So I found another way.
I added the following line to eh Mojo generator, which adds the generated-sources folder to the compiler. This seems to work nicly.
```java
        project.addCompileSourceRoot(generatedSourcesDir());
```

# Notes
We can now generate code from a maven plugin, with YAML

What do we want to do?

I want to create a mojo and all I need to do is extend an class
I can then set the parameters i need and how I generate

I should then be able to set the model type
- xml
- yml
- json

I can then define the parameters
- basePackage
- modelLocation
- modelType 

So all I need to define is the model classes and how I generate from these models.

So there are 2 steps 
- parse the model text files into model objects
	- XML, JSON, YAML -> Java objects
- Pass in the models to the generators
	- javapoet
	- freemarker
	- jenisis
	- jet
- maven plugin
- gradle plugin

A key thing is to keep the mojo simple

All it should do is collect 


- m2s model to source
    - m2s-base
    - m2s-test-maven-plugin
    - m2s-test-model
    - m2s-test-javapoet-generator
    - m2s-test-??-generator
    - m2s-test-project

I think each model should have a type field

```yaml
modelType: pmCoverages
  - code: PMMotorCov
    desc: Personal Motor Coverage
    category: standard ancillary
    availability:
      - 
  - code: PMBreakdown
    desc: Breakdown cover
``` 

