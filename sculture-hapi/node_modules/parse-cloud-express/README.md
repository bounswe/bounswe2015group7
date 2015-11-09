## parse-cloud

Express middleware and utilities for Parse.com Cloud Code functionality in Node.js

### Getting started

Add parse-cloud as a dependency to your `package.json` file:

```
  ...
  "dependencies": {
    "parse-cloud-express": "~1.0"
  }
  ...
```

From any file that uses Cloud Code, require the module:

```
var Parse = require('parse-cloud-express').Parse;
```

Set an environment variable, `PARSE_WEBHOOK_KEY` to the value of your Webhook key in your Parse App settings.

From your main Node app, require the root parse-cloud module, require your cloud code file(s), and mount the provided Express app on some path:

```
// ...

var ParseCloud = require('parse-cloud-express');
require('./cloud/main.js');  // After this, ParseCloud.app will be a configured Express app.

// Mount the cloud code webhook routes on your main Express app:
app.use('/webhooks', ParseCloud.app);

// ...
```

Configure your webhooks on Parse through the dashboard, or see our example project at https://github.com/ParsePlatform/CloudCode-Express for an automated script to handle this step.
