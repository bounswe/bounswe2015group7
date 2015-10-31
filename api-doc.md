##API DOCUMENT

This is the API documentation of Sculture Restful Web Services.

### General Info
- All API requests and responses are in `JSON` format.


### Authentication
- **[`POST` /user/register](#user-register)**
- **[`POST` /user/login](#user-login)**

<br /><br />

---
#### <a name="user-register"></a>`POST /user/register`
Registers user to system for the first time and generate a unique access token.

##### Request Params:
- email                 `string`
- username              `string`
- password              `string`
- fullname              `string` `optional`
- facebook-id           `string` `optional`
- facebook-token        `string` `optional`

**Sample Request:**

```json
{
  "email": "johndoe@gmail.com",
  "username": "johndoe",
  "password": "098f6bcd4621d373cade4e832627b4f6",
  "fullname":"John Doe",
  "facebook-id": 642892189177318,
  "facebook-token": "C3YXdoPX8a1_234pQswLJybYrc"
}
```


##### Response Params:

Response is registered user's information in the format of [`User`](#user-object) object.

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
<br /><br />

---
#### <a name="user-login"></a>`POST /user/login`
Logs in user to system and response it's `access-token` back.

##### Request Params:
- email                 `string`
- password              `string`

**Sample Request:**
```json
{
  "email": "johndoe@gmail.com",
  "password": "098f6bcd4621d373cade4e832627b4f6"
}
```

##### Response Params:

Response is the user's information in the format of [`User`](#user-object) object.

**Sample Response:**

**`Status Code: 200`**
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
<br /><br />

---
### <a name="user-object"></a>User Object ###
|Field|Type|Optional|Note|
|---|---|---|---|
|id|`long`|||
|email|`string`|||
|username|`string`|||
|fullname|`string`|||
|access-token|`string`|||
|facebook-id|`long`|`optional`||
|is-promoted|`string`|||
|notification-rate|`int`||0: None, 1: Only own posts, 2: Own and followed posts|
