# Java implementation of the Rebrickable V3 API

This library provides a Java 11 implementation of the [rebrickable.com API](https://rebrickabkle.com/api/).
It uses only Jackson for JSON parsing and SLF4J for logging.

**Please use the wonderful API of rebrickable.com with care. Don't harm their servers or cause
them trouble.**

## Usage

The main entry point to the library is `com.rebrickable.Rebrickable`. It needs an API key for construction
and will provide necessary API wrappers.

    var api = new Rebrickable("your-api-key");
    List<Color> colors = api.color().all();

    var color = colors.get(0);
    System.out.println("The first color is " + color.name + " and has an RGB value of " + color.rgb);

## Status

This project covers the "lego" API, but not (yet?) the "users" API. Adding the missing part is not
complex, but currently beyond my personal need. Feel free to implement it and send a merge request.

Error handling of any HTTP status code other than 200 is missing. Most of it can be added in the
`AbstractService` class.

## Development

The main work is done by the methods of `AbstractService`. These are used to either load single
objects (`getSingle`) or list of objects in pages (`getPaged`).

### Loading single objects

Loading a single object is straightforward:

- Set up a HTTP GET request with the necessary `Authorization` header
- Parse the returned JSON into a Java object and return it

### Loading lists of objects

The foundation of multiple objects is comparable to loading single objects. The main difference is
all data being wrapped in a response object holding metadata of a request. This response object can
be used for paging through the list. Therefore all paging requests internally work with the
`PagedResponse` class.

- Set up a HTTP GET request with the necessary `Authorization` header
- Request the first page
- Parse the returned JSON into a response object and copy the `results` data to the return value
- Check if the a next page exits, load the objects and add them to the return value
- Continue until no further page exists

An alternative is to specifcy the `page` and `pageSize` parameters to only load parts of the
dataset.

## License

The project is licensed under the MIT license.

## Relation to rebrickable.com

This API is not an official part of rebrickable.com. It is just an open source library to allow
easier use from Java applications.