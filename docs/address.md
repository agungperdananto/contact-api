# Address API spec

## Create address
endpoint [POST] `/api/contacts/{id_contact}/addresses`

request header:
    - X-API-TOKEN: <Token>

Request body:
```
    json 
    {
        "street": "Jl.Fatmawati",
        "city": "Jakarta selatan",
        "province": "DKI Jakarta",
        "country: "indonesia",
        "postalCode": "12150"
    }
```

Response body (success):
```
    json
    {
        "data": {
            "id": <id>,
            "street": "Jl.Fatmawati",
            "city": "Jakarta selatan",
            "province": "DKI Jakarta",
            "country: "indonesia",
            "postalCode": "12150"
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


## Update address
endpoint [PUT] `/api/contacts/{id_contact}/addresses/{id_address}`

request header:
    - X-API-TOKEN: <Token>

Request body:
```
    json 
    {
        "street": "Jl.Fatmawati",
        "city": "Jakarta selatan",
        "province": "DKI Jakarta",
        "country: "indonesia",
        "postalCode": "12150"
    }
```

Response body (success):
```
    json
    {
        "data": {
            "id": <id>,
            "street": "Jl.Fatmawati",
            "city": "Jakarta selatan",
            "province": "DKI Jakarta",
            "country: "indonesia",
            "postalCode": "12150"
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

## Get address
endpoint [PUT] `/api/contacts/{id_contact}/addresses/{id_address}`

request header:
    - X-API-TOKEN: <Token>

Response body (success):
```
    json
    {
        "data": {
            "id": <id>,
            "street": "Jl.Fatmawati",
            "city": "Jakarta selatan",
            "province": "DKI Jakarta",
            "country: "indonesia",
            "postalCode": "12150"
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

## Remove address
endpoint [DELETE] `/api/contacts/{id_contact}/addresses/{id_address}`

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


## List address

endpoint [GET] `/api/contacts/{id_contact}/addresses`

request header:
    - X-API-TOKEN: <Token>

Response body (success):
```
    json
    {
        "data": [
            {
                "id": <id>,
                "street": "Jl.Fatmawati",
                "city": "Jakarta selatan",
                "province": "DKI Jakarta",
                "country: "indonesia",
                "postalCode": "12150"
            },
            {
                "id": <id>,
                "street": "Jl.Fatmawati",
                "city": "Jakarta selatan",
                "province": "DKI Jakarta",
                "country: "indonesia",
                "postalCode": "12150"
            }
        ]
    }
    
```

Response body (failed):
```
    json
    {
        "errors": "<error messages>"
    }
```