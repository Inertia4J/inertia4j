# Inertia4J Spring

This document describes how to install and use Inertia4J with Spring.

## Installation

### Backend

Add the Inertia4J dependency to your project, via Gradle or Maven:

```kotlin
// build.gradle.kts
dependencies {
  implementation("io.github.inertia4j.spring:1.0.0")
}
```

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>io.github.inertia4j.spring</groupId>
        <artifactId>inertia4j</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Frontend

Follow Inertia's [Client-side setup](https://inertiajs.com/client-side-setup) guide for the client-side configuration steps.

## Usage

### Responses

In your controller, the simplest way to use Inertia4J is to inject the `InertiaSpring` bean. This bean will give you access to
the Inertia4J methods. To respond with an Inertia response in your controller method, you can call `inertia.render`.
The `render` method takes two arguments. The first argument is the name of the component to be rendered in client, and
the second argument is a map, which will be converted to a JSON object and sent to the client. This method returns a
`ResponseEntity<String>` instance, so when using Inertia4J in a route, the return type of your method should always be
`ResponseEntity<String>`.

```java
public class RecordController {
    @Autowired
    private InertiaSpring inertia; // Inertia4J bean injection

    @GetMapping("/records")
    public ResponseEntity<String> index() {
        RecordRepository recordRepository = new RecordRepository();
        Set<Record> records = recordRepository.getAllRecords();

        return inertia.render("Records/Index", Map.of("records", records));
    }
}

```

This will instruct the frontend to render the `Records/Index` component with a single prop called "records", which
contains the list of records, as retrieved from `RecordRepository`.

### The HTML Template

The first time an Inertia request is made to the server, the server will respond with an HTML page document. Inertia4J 
will automatically load the `resources/templates/app.html` file in your project and will replace `@PageObject@` with the
data you wish to send to the client. If you wish to customize this template, just make sure to keep a div with id "app" and an HTML attribute `data-page='@PageObject@'`. Remember to use **single quotes** (i.e. `'@PageObject'`), given the JSON object will use double quotes.

### Options

Inertia4J supports option passing on response. To enable option passing, first you need to import
`io.github.inertia4j.spring.Inertia.Options`. After importing, you can now use the `Options` class to pass options as
a third argument to `Inertia.render`. The Inertia protocol defines two main flags which can be passed through options,
those are the `encryptHistory` and `clearHistory` flags. If you need more information about their functionality
you can read the [official Inertia docs](https://inertiajs.com/history-encryption). Here is an example of option
passing in the Inertia response:

```java
import io.github.inertia4j.spring.Inertia.Options;

@GetMapping("/records")
public ResponseEntity<String> index() {
    /* ... */
    return inertia.render("Records/Index", records, Options.clearHistory().encryptHistory());
}
```

This way, the response will be sent with the `encryptHistory` value set to `true`. Nozte that this is only applied for
the next render call, after that, Inertia will revert the flags back to their default values.

You may want to provide a default value to the `encryptHistory` flag, and this is also supported. All you need to do is
to add the following line to your `application.properties` file:

```text
inertia.history.encrypt=true
```

In this case, if you wanted to set the flag to `false` for a specific response, you could then specify that in the options:

```java
inertia.render("Records/Index", records, Options.encryptHistory(false));
```

The `clearHistory` option works the same way, except it's not possible to set a default value for it.

### Asset Versioning

The Inertia4J adapter fully supports asset versioning, and responds accordingly to outdated assets. To provide a version
to your assets, you will need to provide an implementation of the `VersionProvider` interface. This interface has only
a single method, called `get`, which returns your asset version number as a `String`. You can implement the `get`
method to suit your project's needs, be it a value that manually changes, or a dynamic hash of your asset folder.

If you choose not to implement a `VersionProvider`, your app is still going to work perfectly, it just won't support 
Inertia asset versioning.

Below is an example of a simple `VersionProvider` implementation in Spring:

```java
import io.github.inertia4j.spring.VersionProvider;
import org.springframework.stereotype.Comonent;

@Component
public class MyCustomVersionProvider implements VersionProvider {
    @Override
    public String get() {
        return "latest";
    }
}
```

### Redirecting

Inertia4J supports redirecting, and just like the Inertia docs specify, there are two kinds of redirects. The first
is via the `redirect` method, and it is meant to redirect to other Inertia routes. The second kind of redirect is via
the `location` method, which redirects the client to a non-Inertia route in your application, or to an external route.
Both methods only receive a single parameter, which is the route to redirect to.

Below is an example of both methods being used:

 ```java
public class RecordController {
    @Autowired
    private InertiaSpring inertia; // Inertia4J bean injection
  
    @GetMapping("/records")
    public ResponseEntity<String> index() {
        /* ... */
    }
  
    @PostMapping("/records")
    public ResponseEntity<String> create() {
        /* ... */
        return inertia.redirect("/records"); // This redirects to our index "/records" route.
    }
  
    @GetMapping("/external-redirect")
    public ResponseEntity<String> externalRedirect() {
        return inertia.location("https://github.com/Inertia4J/inertia4j"); // Redirects to an external route.
    }
}
 ```

Note that in the example provided, we've defined a `POST` route as well. This is the most common use case for
redirecting in a simple application, and the redirect methods (both `inertia.redirect` and `inertia.location`) work on
routes that receive requests of any HTTP methods. If you need more information about redirects in Inertia, please read
the [official docs](https://inertiajs.com/redirects).

### Partial Reloads

Inertia4J also supports partial reloads, in case you don't need to return all the data to your client-side on component load, or in case you just need to reload a specific component in your page.
