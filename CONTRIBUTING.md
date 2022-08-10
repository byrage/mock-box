# Contributing

## Project Setup

You should run the command below for setting lint on commit.

```
$ git config core.hookspath .githooks
```

You should reformat code format with `spotless` before push.

```
./gradlew spotlessCheck : lint
./gradlew spotlessApply : reformat
```

## Configure Code Style

Code-Style based on [google-java-format](https://github.com/google/google-java-format).

### IntelliJ

Preferences | Editor | Code Style | Import Scheme : `intellij-java-google-style.xml`

### Eclipse or Else

Check the [Google Repository](https://github.com/google/styleguide)
