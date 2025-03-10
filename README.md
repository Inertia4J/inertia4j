# Inertia 4J Spring

An adapter to use Inertia.js on a Spring backend.

## Usage

The simplest way to use Inertia4J is to call `Inertia.render` with the component name and props.

Below is a controller example:

```java
@GetMapping("/records")
public ResponseEntity<String> index() {
    RecordRepository recordRepository = new RecordRepository();
    Set<Record> records = recordRepository.getAllRecords();

    return Inertia.render("Records/Index", records);
}
```

This will instruct the frontend to render the `Records/Index` component with a single prop containing a list of records.

Given no response URL was given, it will be the same of the request URL, which in this case is `/records`. However, you
can also specify a different URL by passing it as argument to `Inertia.render`:

```java
@GetMapping("/records")
public ResponseEntity<String> index() {
  /* ... */
  return Inertia.render("/records/all", "Records/Index", records);
}
```

### Setting encryptHistory and clearHistory flags

Inertia provides a way to encrypt or clear browsing history when responding to a request, and that is via the usage of 
the `encryptHistory` and `clearHistory` flags, described [here](https://inertiajs.com/history-encryption). In Inertia4J,
you can set these flags by passing options to the `render` method.

Here is an example:

```java
@GetMapping("/records")
public ResponseEntity<String> index() {
  /* ... */
  return Inertia.render("Records/Index", records, Options.encryptHistory());
}
```

This way, the response will be sent with the `encryptHistory` value set to `true`. Note that this is only applied for
the next render call, after that, Inertia will revert the flags back to their default values.

You may want to provide a default value to the `encryptHistory` flag, and this is also supported. All you need to do is
to add the following line to your `application.properties` file:

```text
inertia.history.encrypt=true
```

In this case, if you wanted to set the flag to `false` for a specific response, you could then specify that in the options:

```java
Inertia.render("Records/Index", records, Options.encryptHistory(false));
```

The `clearHistory` option works the same way, except it's not possible to set a default value for it. You can chain
calls to the `Options` builder to specify it:

```java
Inertia.render("Records/Index", records, Options.encryptHistory(false).clearHistory());
```

### Request headers

TODO: document how the request is obtained internally and how to pass a request argument to `.render`.

### Serialization

TODO: document how to change the serializer.

### HTML template

TODO: document how to use a custom template renderer.

### TODOS:

#### V1

- [ ] Setup Vite and [integrate it](https://v3.vitejs.dev/guide/backend-integration.html) with Spring for development
- [ ] Support asset versioning
- [ ] [Autoconfigure](https://www.baeldung.com/spring-boot-custom-auto-configuration) the beans
- [x] Make the JSON serializer customizable
- [x] Support the `encryptHistory` and `clearHistory` flags on response
- [x] Fill the `url` response element
- [x] Support partial reloads (via `X-Inertia-Partial-Data` and `X-Inertia-Partial-Component`)
- [x] Add basic documentation to classes
- [ ] Write "getting started" documentation
- [ ] Create Ktor plugin

#### V1.1

- [ ] Shared data
- [ ] Deferred props
- [ ] Create unit tests
- [ ] Generate TypeScript types for props ([inspiration](https://www.youtube.com/watch?v=LeYF1NE3jQ4))
