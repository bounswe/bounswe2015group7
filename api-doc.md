##API DOCUMENT

This is the API documentation of Sculture Restful Web Services.

### General Info
- All API requests and responses are in `JSON` format.


### Authentication
- **[`POST` /user/register](#user-register)**
- **[`POST` /user/login](#user-login)**


---
#### <a name="user-register"></a>`/user/register`
Registers user to system for the first time and generate a unique access token.

##### Request:
- email                 `string`
- username              `string`
- password              `string`
- fullname              `string` `optional`
- facebook-id           `string` `optional`
- facebook-token        `string` `optional`


##### Response:

Response is (`User`)[#user-object] object.

- access-token          `string`

**Sample Request:**
Header:
```json

```

**Sample Response:**

Status Code: **200**
```json
{
  "data": [
    {
      "id": 1234,
      "username": "john-doe",
      "email": "johndoe@gmail.com",
      "fullname": "John Doe",
      "facebook-id": 100009008813004,
      "access-token": "5f4dcc3b5aa765d61d8327deb882cf99",
      "is-promoted": true,
      "notification-rate": 1
    }
  ],
  "errors": []
}
```

---
#### <a name="user-login"></a>`/user/login`
Logs in user to system and response it's `access-token` back.

##### Request Params:
- email                 `string`
- password              `string`

##### Response Params:
- access-token          `string`

**Sample Request:**
Header:
```json

```

**Sample Response:**

Status Code: **200**
```json
{
  "data": [
    {
      "id": 1234,
      "username": "john-doe",
      "email": "johndoe@gmail.com",
      "fullname": "John Doe",
      "facebook-id": 100009008813004,
      "access-token": "5f4dcc3b5aa765d61d8327deb882cf99",
      "is-promoted": true,
      "notification-rate": 1
    }
  ],
  "errors": []
}
```
---
### <a name="user-register">User Object ###
|Field|Type|Optional|Note|
|---|---|---|---|
|id|`long`|||
|email|`string`|||
|username|`string`|||
|fullname|`string`|||
|access-token|`string`|||
