Thank you for your interest in contributing to the project. There are several ways you can contribute, please read below
for more information.

## Reporting Bugs and Suggesting Features

If you find a bug while using Inertia4J, or even if you feel some feature could be added to the project, check our
[issues page](https://github.com/Inertia4J/inertia4j/issues) in the Inertia4J repository. If there is no issue
(closed or open) that addresses your bug or feature request, we encourage you to open an Issue.

If you wish to open a new issue, click the "New Issue" button at our issues page and select the template that best fits
the kind of issue you want to open. After selecting, please fill the fields as described in the issue template.

## Contributing

If you'd like to contribute to Inertia4J with code, please signal you'd like to work on a feature by **commenting on an
issue**. After that, you can fork the Inertia4J repository, develop your feature/fix and then open a Pull Request,
**linking the issue** that it relates to. Avoid opening PRs without any related issue, and when selecting an issue to
solve, try to always communicate that you are working on it, in order to avoid two people working on the same thing.

If you come across any doubts regarding the library's inner workings when developing, you can always ask for help in the
issue you're working on.

### New features

We have a [roadmap](https://github.com/Inertia4J/inertia4j/tree/main/docs/roadmap.md) for the next version release. If
you'd like to contribute with any of the features that are already listed for the next version of Inertia4J, please
check if an issue related to the feature was already created. If not, you may open an issue as well and manifest that 
you are interested in developing a certain feature. This keeps the workflow organized for all contributors.

It is also important to note that, regarding new features, the features lined up for the next release will always take
precedence over any other features. If possible, when choosing a new proposed feature to work on, please try to pick
incomplete features from the next release.

## The Project Structure

The Inertia4J project is composed of many modules, and in order to keep your proposed changes organized, you should 
understand the role of each module in the library:

- **core**: This is the most important Inertia4J module, as it contains universal, non-replaceable logic, that is called 
by both the Spring and Ktor implementations, as well as other project packages. The Inertia4J core should only be 
modified in order to add universal features, that will be used by both Ktor and Spring. The core should also never have
any dependency other than the SPI.
- **spi**: This package contains Inertia4J SPIs, which can be extended by any user. These interfaces are integral to the
library and the existing interfaces should be modified with caution, as it may break compatibility with applications 
implementing them.
- **spring**: This package contains Spring specific implementations. Spring specific functionalities should be
implemented here.
- **ktor**: This package contains Ktor specific implementations. Ktor specific functionalities should be
implemented here.
- **jackson**: This package contains the Jackson implementation of the `PageObject` serializer, which is the default
implementation.
