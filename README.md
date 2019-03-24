# Deprecated
Replaced with [Commons Box Library](https://github.com/Plajer/Commons-Box)

# PLCore
Plajer's Lair core which provides minigame utilities

It does contain some useful methods for making my minigame development (and not only!) faster and easier :)

## Java docs
https://jd.plajer.xyz/minecraft/plcore

## Maven repo
Add repository
```xml
    <repositories>
        <repository>
            <id>plajerlair-repo</id>
            <url>https://maven.plajer.xyz/minecraft</url>
        </repository>
    </repositories>
```
Then add the dependency
```xml
    <dependencies>
            <dependency>
            <groupId>pl.plajerlair</groupId>
            <artifactId>plcore</artifactId>
            <version>1.4.4</version>
            <scope>compile</scope>
        </dependency>
```
You can either use compile scope or provided based on what you want to do, however remember when you'll use this API in your projects
and you'll compile them inside plugin you should repackage them using maven shade plugin.
