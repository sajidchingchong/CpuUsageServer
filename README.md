# Pre-requisite
Active MQ should be up and running

Check the following: https://activemq.apache.org/version-5-getting-started.html

# Run
`./mvnw spring-boot:run`

# Test

`GET http://localhost:8080/usage?from={param1}&to={param2}&threshold={param3}`

where 

> param1 = A date in the format yyyy-MM-dd HH:mm:ss.
>
> param2 = A date in the format yyyy-MM-dd HH:mm:ss.
>
> param3 = An integer specifying percent usage.

> Example: `http://localhost:8080/usage?from=2024-02-02%2000%3A00%3A00&to=2024-02-02%2059%3A59%3A59&threshold=80`

# Response

```sh
[
  {
    "Client1": [
      {
        "date": "2024-02-02T15:00:00.000+00:00",
        "percent": 80
      },
      {
        "date": "2024-02-02T03:45:00.000+00:00",
        "percent": 85
      },
      {
        "date": "2024-02-02T09:30:00.000+00:00",
        "percent": 90
      }
    ]
  },
  {
    "Client2": [
      {
        "date": "2024-02-02T09:30:00.000+00:00",
        "percent": 95
      }
    ]
  }
]
```
