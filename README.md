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
- [ ] Make the JSON serializer customizable
- [ ] Support asset versioning
- [ ] Support the `encryptHistory` and `clearHistory` flags on response
- [ ] Fill the `url` response element
- [ ] Support partial reloads (via `X-Inertia-Partial-Data` and `X-Inertia-Partial-Component`)
- [ ] Exception throwing/handling
