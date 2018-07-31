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

### notes cont...
com.admiral.uk.adms

adms-pcs

adms-pcs-product
	adms-pcs-product-maven-plugin
	adms-pcs-product-model
	adms-pcs-product-generator


We need to think about the format of the files...
2 choices
Change the file extension and set the type

.model -> yaml file
.cov   -> json file

or we have
.yaml  -> any object

A yaml file can have multiple doucments in it

If we change the extension, we can set an association

I've looked at docker and ansible, and they keep the .yml file extension, so we should.
Also, yaml can have multiple documents in the same file

So how do we recognise the type?

We need the type 

coverage:
  code: pmmotorcov
  desc: person motor coverage

So coverage here is defining the type
But how do we read the type, if we need to parse the file before we know what type it is?

# How to deal with inheritance in the models
http://www.baeldung.com/jackson-inheritance

## The model inheritance problem and the vistor pattern
https://stackoverflow.com/questions/29458676/how-to-avoid-instanceof-when-implementing-factory-design-pattern

This is a good example to allow the access and processing of the models using a type safe vistor pattern.

So, we could use annotation processing, to annotate the model entities, then generate the visitor classes.
Then, all we need to write is the generator code to access the model classes!
We may not require any manual code
Perhaps we could (in the model) add any additional classes to extend the generated classes.
THIS IS GOOD!

Howevere, this does not account for hiarachhy in the model
The current solution will collect a list of model objects. 
The get method will be type safe, but it still is a flat list of model objects
What if a vehicle model, had a list of drivers
Drivers would be in each vehicle, so this would work yea?

Ok, I think this would work, but our visitor patter will have generated a getDrivers() but it won't find any
You would have to navigate to the vehicle first!


## new notes
So generics seems be be difficult with Jackson

So what do we need...

I design the model classes
So I always need a model class, then the fields or arrays

If I added an anotation to the model class, then I could generate all the helper methods for the model!

coverages:
  - code: pmmotorcov
    desc: klkjkl
    

We create the models in java
We annote the models 
The annotation processor generates the helper methods for the models

Do models have to extend a model?
Why do they have to?
What extra functionallity do they need?

We can have bean validation in the model class
So we really have everything

The problem is, where are the files found and what tells us what files are what?
We can use annoation processor to generate any helper files we want
But still what is saying what each file is?

Look at the immutables thing
http://www.baeldung.com/immutables

Here you create an abstract class, annotate it with Immutable and it generates the immuatable class
So Abstract Person will generate ImmutablePerson

So could we do an abstract item, then it generates the model
abstract Coverage...
generates
CoverageModel
