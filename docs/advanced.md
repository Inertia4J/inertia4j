# Advanced Usage

This is a guide meant to help users extend Inertia4J and customize it.

## Serialization

In the standard implementation of Inertia4J, we provide a basic JSON serializer that uses the Jackson library to
convert data passed to our `render` call into JSON for the client. However, you may be using a different JSON
serialization method, and Inertia4J allows you to implement your own serializer, so that your project doesn't depend on
Jackson.

In order to understand how to implement your own serializer, it's important to understand how Inertia works internally.

In Inertia, the entity we respond with to the client (as JSON) in order to render the correct component with its data, is
called a Page Object. The Inertia specification provides a
[page object specification](https://inertiajs.com/the-protocol#the-page-object) in their documentation, so if you need
to understand more about the Page Object, you can read the Inertia docs. Inertia4J serializes this page object
internally in order to provide it as a JSON to the client, with the correct object representation of any data
type used in your project. In order to facilitate extending this serialization functionality, we've provided an
[interface](https://github.com/Inertia4J/inertia4j/blob/main/inertia4j.spi/src/main/java/io/github/inertia4j/spi/PageObjectSerializer.java),
which can be implemented according to your project needs, and will work out of the box when plugged into Inertia4J.

This interface defines only a single method, which is the `serialize` method. The function of this method is (as you
might expect) to take a `PageObject` and return a
`String` that represents the data in that object. If the serialization fails, it should throw a
`SerializationException`. If you need to know more about the internal representation of the Page Object in Inertia4J,
please read its
[implementation](https://github.com/Inertia4J/inertia4j/blob/main/inertia4j.spi/src/main/java/io/github/inertia4j/spi/PageObject.java).

In Inertia4J, it is also the serializer's role to support [partial reloads](https://inertiajs.com/partial-reloads).
Your serializer should only respond to partial reloads with the correct properties, as specified by the
`partialDataProps` parameter, which is passed to the `serialize` method along with your Page Object. Once you do
implement your own serializer, you can plug it into Inertia4J.

In Spring, you can achieve this by implementing the `PageObjectSerializer` interface in a Spring Bean, which can be injected
into your Inertia4J Spring project. The interface implementation could be achieved through something like this:

```java
import io.github.inertia4j.spi.PageObject;
import io.github.inertia4j.spi.PageObjectSerializer;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Component
@Primary
public class MyCustomPageObjectSerializer implements PageObjectSerializer {
    @Override
    public String serialize(PageObject pageObject, List<String> partialDataProps) {
        /* ... */
    }
}
```

In Ktor, you can achieve this by passing it to the plugin installation:

```kotlin
install(Inertia) {
    serializer = MyCustomPageObjectSerializer()
}
```

## HTML template

When an Inertia page is first fetched, the server provides an HTML document with the data for the current route in
its `div#app` element's `data-page` attribute. To achieve this, Inertia4J provides a `TemplateRenderer` interface, to which
we implement a default template renderer. You may also implement your own renderer.

The renderer interface also specifies a single method, `render`, in which it receives a `String pageObjectJson`, which
is the JSON representation of the Page Object (provided by your JSON Serializer), and it returns another String, which
represents the HTML with the serialized Page Object.

When implementing a new Template Renderer, just make sure that it complies with the
[Inertia protocol specification](https://inertiajs.com/the-protocol).

In Spring, you can achieve this by implementing the `TemplateRenderer` interface in a Spring Bean, which can be injected
into your Inertia4J Spring project. The interface implementation could be achieved through something like this:

```java
import io.github.inertia4j.spi.TemplateRenderer;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Component
@Primary
public class MyCustomTemplateRenderer implements TemplateRenderer {
    @Override
    public String render(String pageObjectJson) {
        /* ... */
    }
}
```

To change the Template Renderer to your implementation in Ktor, you can write the following:

```kotlin
install(Inertia) {
    templateRenderer = MyCustomTemplateRenderer()
}
```
