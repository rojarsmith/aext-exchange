# AEXT

## Eclipse

### Debug

Program arguments:

```bash
# For Dev
--spring.config.additional-location="C:\\my\\Coin\\aext\\service\\aext-exchange\\config\\backend\\" --spring.profiles.active=dev

# For test
None

# OLD
--spring.config.additional-location="file:///C:/my/Coin/aext/service/aext-exchange/config/backend/" --spring.profiles.active=dev --spring.config.location="classpath:application.properties"

--spring.config.additional-location="C:\\my\\Coin\\aext\\service\\aext-exchange\\config\\backend\\" --spring.profiles.active=dev --spring.config.location="classpath:application.properties"


```

Front end

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "node",
            "request": "launch",
            "name": "Launch via NPM",
            "runtimeExecutable": "npm",
            "runtimeArgs": [
                "start"
            ],
            "skipFiles": [
                "<node_internals>/**"
            ],
            "port": 9222
        },

        {
            "type": "chrome",
            "request": "launch",
            "name": "Launch Chrome against localhost",
            "url": "http://localhost:3001",
            "runtimeExecutable": "C:/my/toolchain/GoogleChromePortable-dev/GoogleChromePortable.exe",
            "runtimeArgs": ["--remote-debugging-port=9222"],
            "webRoot": "${workspaceFolder}"
        }
    ]
}
```

