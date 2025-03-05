# Inertia 4J Spring

An adapter to use Inertia.js on a Spring backend.

## Usage

In every Spring controller method, you should initialize the `InertiaRenderer` class

```java
InertiaRenderer renderer = new InertiaRenderer();
```

Once you've done it, you can call the renderer's `render()` method, in order to transform your normal response into a response that the frontend adapter can use. Below is an example of a controller using Inertia 4J:

```java
@GetMapping("/records")
public ResponseEntity<String> index(WebRequest request) {
    InertiaRenderer renderer = new InertiaRenderer();

    RecordRepository recordRepository = new RecordRepository();
    Set<Record> records = recordRepository.getAllRecords();

    return renderer.render(request, "Records/Index", records);
}
```

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
- [ ] Create `Inertia` façade 
- [ ] [Autoconfigure](https://www.baeldung.com/spring-boot-custom-auto-configuration) the beans
- [ ] Setup CI
- [ ] Generate TypeScript types for props ([inspiration](https://www.youtube.com/watch?v=LeYF1NE3jQ4))
