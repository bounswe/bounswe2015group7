#### story_create
Request
```
{
  "title" : "my title",
  "content" : "my content",
  "tags" : ["myTag1", "myTag2"]
}
```
Success response
```
{"result": {
  {
    "content" : "my content",
    "createdAt" : "2015-11-06T14:14:31.692",
    "id" : "01KdGXxQgj",
    "lastEditorId" : "N9a00UfgSn",
    "ownerId" : "N9a00UfgSn",
    "tags" : [ "myTag1", "myTag2"],
    "title": "my title",
    "updatedAt" : "2015-11-06T14:14:31.692"
  }
}
```
Error response
```
{
    "code": 141,
    "error": "An error has occurred"
}
```
* "Authentication error" :  The user is not logged in.
* "Syntax error": One or more of them:
  * Title is empty
  * Content is empty
  * There is no tag
  
#### story_get
Request
```
{
  "id" : "01KdGXxQgj title"
}
```
Success response
```
{ "result" : {
  {
    "content" : "my content",
    "createdAt" : "2015-11-06T14:14:31.692",
    "id" : "01KdGXxQgj",
    "lastEditorId" : "N9a00UfgSn",
    "ownerId" : "N9a00UfgSn",
    "tags" : [ "myTag1", "myTag2"],
    "title": "my title",
    "updatedAt" : "2015-11-06T14:14:31.692"
  }
  }
```
Error response
```
{
    "code": 141,
    "error": "An error has occurred"
}
```

#### search
Searches give querry and return results. The results can be paged by using size and page variables.

Request
```
{
  "query" : "turkey asia"
  "size" : 10, //default 100
  "page" : 0, //default 0
}
```
Success response
```
{
    "result": [
        {
            "createdAt": "2015-11-05T19:38:58.686Z",
            "id": "8QI7K2j84X",
            "lastEditorId": "N9a00UfgSn",
            "ownerId": "N9a00UfgSn",
            "tags": [
                "country",
                "anatolia",
                "asia"
            ],
            "title": "turkey",
            "updatedAt": "2015-11-05T19:38:58.686Z"
        },
        {
            "createdAt": "2015-11-05T19:37:11.485Z",
            "id": "RCOAL3Eblf",
            "lastEditorId": "N9a00UfgSn",
            "ownerId": "N9a00UfgSn",
            "tags": [
                "country",
                "anatolia",
                "asia"
            ],
            "title": "turkey",
            "updatedAt": "2015-11-05T19:37:11.485Z"
        },
        {
            "createdAt": "2015-11-06T14:14:31.692Z",
            "id": "01KdGXxQgj",
            "lastEditorId": "N9a00UfgSn",
            "ownerId": "N9a00UfgSn",
            "tags": [
                "turkey",
                "cook",
                "eggplant"
            ],
            "title": "Imam Bayildi",
            "updatedAt": "2015-11-06T14:14:31.692Z"
        }
    ]
}
```
Error response
```
{
    "code": 141,
    "error": "An error has occurred"
}
```
  
