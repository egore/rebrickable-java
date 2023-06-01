# Java implementation of the Rebrickable V3 API

This library provides a Java 11 implementation of the [rebrickable.com API](https://rebrickabkle.com/api/).
It uses only Jackson for JSON parsing and SLF4J for logging.

## Usage

The main entry point to the library is `com.rebrickable.Rebrickable`. It needs an API key for construction
and will provide necessary API wrappers.

    var api = new Rebrickable("your-api-key");
    List<Color> colors = api.color().all();

    var color = colors.get(0);
    System.out.println("The first color is " + color.name + " and has an RGB value of " + color.rgb);

## License

The project is licensed under the MIT license.