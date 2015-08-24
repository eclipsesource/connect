# Tabris Connect

## App API
The following API describes the services to use form the developer apps.

> **Please Note:** All HTTP API calls to tabris-connect must include the `Accept: application/json` header.

### Signin

`GET : /api/v1/app/signin`

After oauth process is complete (success/error) this url will be called:    
  `tabris-connect://authorization`    
with the following parameters:    
  `token` = in case of success it's the access token to use for further API calls.    
  `error` = in case of an error it's the reason the signin failed e.g. not invited etc.   
  `errorCode` = the code describes the error e.g. 601 = not invited
  
### Signout

`GET : /api/v1/app/signout?access_token={token}`

The `{token}` parameter needs to be the token given during the signin process. Returns a 204 if the signout was successful.

### User Info

`GET : /api/v1/app/me?access_token={token}`

Response:

```
{
    "_id": "54292746c5dd04306ad63bc7",
    "data": {
        "github": {
            "avatarUrl": "https://avatars.githubusercontent.com/u/289648?v=2",
            "login": "hstaudacher",
            "token": "XXXXX"
        }
    },
    "email": "hstaudacher@eclipsesource.com",
    "info": {
        "city": "Karladasruhe, Germany",
        "company": "EclipseSource",
        "country": "asdas",
        "name": "Holger Staudacher",
        "phone": "",
        "street": "sadas",
        "zip": "asdas"
    },
    "name": "hstaudacher",
    "roles": [
        "admin",
        "user",
        "free",
        "developer"
    ]
}

```

### Apps

`GET : /api/v1/app/me/apps?access_token={token}`

Response:

```
[
    {
        "description": "Internal Repository for common stuff",
        "name": "project name",
        "url": "https://raw.githubusercontent.com/eclipsesource/intern/master/index.json"
    },
    {
        "description": "Clippy is a very simple Flash widget that makes it possible to place arbitrary text onto the client's clipboard.",
        "name": "clippy",
        "url": "https://raw.githubusercontent.com/hstaudacher/clippy/master/index.json"
    }
]
```

### Returning Users
During sign-in the client can send a user id to determine the used OAuth scopes. This prevents duplicate GitHub Redirects. The query parameter should be named `user_id` and contain the full qualified id of a user.

**Please Note:** Requests to GitHub raw urls need to have these Headers:    
1. `Accept: application/vnd.github.v3.raw`    
2. `Authorization: token XXXXX`, the XXXXX token needs to be the GitHub Token from `/me->data->github->token`.    
