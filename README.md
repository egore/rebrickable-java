# Java implementation of the Rebrickable V3 API

This library provides a Java 11 implementation of the [rebrickable.com API](https://rebrickabkle.com/api/).
It uses only Jackson for JSON parsing and SLF4J for logging.

**Please use the wonderful API of rebrickable.com with care. Don't harm their servers or cause
them trouble.**

## Usage

The main entry point to the library is `com.rebrickable.Rebrickable`. It needs an API key for construction
and will provide necessary API wrappers.

    var api = new Rebrickable("your-api-key");
    List<Color> colors = api.lego().color().all();

    var color = colors.get(0);
    System.out.println("The first color is " + color.name + " and has an RGB value of " + color.rgb);

## Status

This project covers all of the "lego" API, but not all parts of the "users" API. Adding the
missing part is not complex, but currently beyond my personal need. Feel free to implement it and
send a merge request.

Basic error handling of HTTP status codes 4xx is implemented in `AbstractService` class (see `mapError`
method). Additional work is needed when adding POST/DELETE requests of the "users" API to map status
codes to more specific exception types.

## Development

The communication with the API is done by the methods of `AbstractService`. This class offers
HTTP functionality matching the API. There are two main methods for loading data: either to load
single a object (`getSingle`) or list of objects (`getAllInPages` respectively `getPage` for a
single page).

Additionally this offers methods to modify data. The API only allows modifying user-owned data
(like sets owned by the user). The main methods are used to create (POST), update (PATCH),
replace (PUT) or remove (DELETE) data.

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

An alternative is to call `page()` instead of `all()` which allows specifying the `page` and `pageSize`
parameters to only load parts of the dataset.

### Creating, updating and deleting data

Methods modifying data need an additional *user token*, which can be obtained using username
and password. The handling of this is done in the `UsersService`, which will perform a login
upon instantiation (see `login()`. All services retrieved from the `UsersService` will use the token
automatically.

The methods all work in the same schema and are therefore implemented in `AbstractService#upload()`:

- Set up a HTTP request with the necessary `Authorization` header and `user token` in the path
- Convert the data provided to the expected format (it is usually a stripped down version of the
  provided method parameter)
- Send the data as `application/x-www-form-urlencoded` content to the server
- Parse the returned JSON (if any) into a Java object and return it

## Testing

The project offers two main ways to test the code. The first ones are unit tests using a mocked
API, the second ones are integration tests which rely on the actual API.

Both are written using JUnit 5 for test execution, and AssertJ for the assertions.

### Unit tests

To run the unit tests using the maven-surefire-plugin invoke `mvn test`. The will start up a
wiremock server and set up the requests and responses in the method. The responses are stripped
down versions of the official API.

### Integration tests

To run the integration tests using the maven-failsafe-plugin invoke `mvn verify`. This will
call the official API so you need to provide a correct API key. If you also want to test the
user-content methods you also need to provide a username and a password. This is done using
the following environment variables.

- REBRICKABLE_API_KEY
- REBRICKABLE_USERNAME
- REBRICKABLE_PASSWORD

If these are not provided, the integration tests will skip execution.

## License

The project is licensed under the MIT license.

## Relation to rebrickable.com

This API is not an official part of rebrickable.com. It is just an open source library to allow
easier use from Java applications.