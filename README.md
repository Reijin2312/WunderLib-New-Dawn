[![](https://jitpack.io/v/quiqueck/WunderLib.svg)](https://jitpack.io/#quiqueck/WunderLib)

# WunderLib

WunderLib is a library mod mainly focused on UI and Math, MC 1.19

## Importing:

You can easily include WunderLib as an internal Dependency by adding the following to your `build.gradle`:

```
repositories {
    ...
    maven { url 'https://jitpack.io' } 
}
```

```
dependencies {
    ...
    modImplementation "com.github.quiqueck:WunderLib:${project.wunderlib_version}"
    include "com.github.quiqueck:WunderLib:${project.wunderlib_version}"
}
```

The `include` line will bundle the lib with your mod, so users will not have to download it separately.
You should also add a dependency to `fabirc.mod.json`. WunderLib uses Semantic versioning, so adding the dependency as
follows should respect that and ensure that your mod is not loaded with an incompatible version of WunderLib:

```
"depends": {
  ...
  "wunderlib": ["1.0.x", ">1.0.0"]
}
```

In this example `1.0.1` is the WunderLib Version you are building against.

## Building:

* Clone repo
* Run command line in folder: gradlew build
* Mod .jar will be in ./build/libs
