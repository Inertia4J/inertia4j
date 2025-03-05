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

### Request headers

TODO: document how the request is obtained internally and how to pass a request argument to `.render`.

### Serialization

TODO: document how to change the serializer.

### HTML template

TODO: document how to use a custom template renderer.

### TODOS:

- [ ] Test with frontend adapters
- [ ] Create unit tests
- [x] Make the JSON serializer customizable
- [ ] Support asset versioning
- [ ] Support the `encryptHistory` and `clearHistory` flags on response
- [x] Fill the `url` response element
- [ ] Support partial reloads (via `X-Inertia-Partial-Data` and `X-Inertia-Partial-Component`)
- [ ] Exception throwing/handling
- [x] Create `core` module
- [x] Support `.render` without passing `request` 
    * We can use `RequestContextHolder` for that. Example [here](https://dzone.com/articles/quick-tip-spring-rest-utility).
- [x] Create `Inertia` façade 
- [ ] Add basic documentation to classes
- [ ] [Autoconfigure](https://www.baeldung.com/spring-boot-custom-auto-configuration) the beans
- [x] Setup CI
- [ ] Generate TypeScript types for props ([inspiration](https://www.youtube.com/watch?v=LeYF1NE3jQ4))
