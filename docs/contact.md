# Contact API spec

## Create contact
Endpoint [POST] `/api/contacts`

request header:
    - X-API-TOKEN: <Token>

Request body:
```
    json 
    {
        "firstname": "grass",
        "lastname": "root",
        "email": "root@mail.com",
        "phone": "123456"
    }
```

Response body (success):
```
    json 
    {
        "data": {
            "id": "<id>",
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        }
    }
    
```

Response body (failed):
```
    json
    {
        "errors": "<error messages>"
    }
```


## Update contact
Endpoint [PUT] `/api/contacts/{id}`

request header:
    - X-API-TOKEN: <Token>

Request body:
    ```
        json 
        {
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        }
    ```

Response body (success):
```
    json 
    {
        "data": {
            "id": "<id>",
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        }
    }
```

Response body (failed):
```
    json
    {
        "errors": "<error messages>"
    }
```


## Get contact
Endpoint [GET] `/api/contacts/{id}`

request header:
    - X-API-TOKEN: <Token>

Response body (success):
```
    json 
    {
        "data": {
            "id": "<id>",
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        }
    }
```

Response body (failed, 404):
```
    json
    {
        "errors": "<error messages>"
    }
```

## Search contact
Endpoint [GET] `/api/contacts`

Query param:

- name : String , combine firtsname and lastname, using LIKE, optional
- phone : String , combine firtsname and lastname, using LIKE, optional
- email : String , combine firtsname and lastname, using LIKE, optional
- page : Int, start from 0, default 1
- size: Int, default 10



request header:
    - X-API-TOKEN: <Token>

Response body (success):
```
    json 
    {
        "data": [{
            "id": "<id>",
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        },
        {
            "id": "<id>",
            "firstname": "grass",
            "lastname": "root",
            "email": "root@mail.com",
            "phone": "123456"
        },
        ],
        "paging":{
            "currentPage": 1,
            "totalpage": 10,
            "size": 10
        }
    }
```


Response body (failed):
```
    json
    {
        "errors": "<error messages>"
    }
```

## Remove contact
Endpoint [DELETE] `/api/contacts/{id}`

request header:
    - X-API-TOKEN: <Token>

Response body (success):
```
    json
    {
        "data": "OK"
    }
```

Response body (failed):

```
    json
    {
        "errors": "<error messages>"
    }
```
