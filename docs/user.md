# User API spec

## Register user

endpoint [POST] `/api/users`
Request body:
```json
    {
        "username": "admin",
        "password": "123",
        "name": "admin user"
    }
    
```

response body (success):
```json
    {
       "data": "OK"
    }
```

response body (failed):
```json
    {
       "errors": "<error messages>"
    }
```

## Login 
endpoint [POST] `/api/auth/login`
Request body:
```json
    {
        "username": "admin",
        "password": "123"
    }
    
```

response body (success):
```json
    {
       "data": {
            "token": "<Token>",
            "expiredAt": 123324334 // in ms
       }
    }
```

response body (failed):
```json
    {
       "errors": "<error messages>"
    }
```
## Get user

endpoint [GET] `/api/users/profile`
request header:
    - X-API-TOKEN: <Token>

response body (success):
```json
    {
        "username": "admin",
        "name": "admin user"
    }
```

response body (failed):
```json
    {
       "errors": "<error messages>"
    }
```


## Update user
endpoint [PATCH] `/api/users/profile`

request header:
    - X-API-TOKEN: <Token>

Request body:
```json
    {
        "password": "123",
        "name": "admin user"
    }
    
```

response body (success):
```json
    {
        "username": "admin",
        "name": "admin user"
    }
```

response body (failed):
```json
    {
       "errors": "<error messages>"
    }
```


## Logout

endpoint [DELETE] `/api/auth/logout`

request header:
    - X-API-TOKEN: <Token>


response body (success):
```json
    {
       "data": "OK"
    }
```

response body (failed):
```json
    {
       "errors": "<error messages>"
    }
```