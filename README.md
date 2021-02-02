# VestNet

### Users
There are two types of users and both of them can be generated via Sign Up.
##### Investor User
noreply.vestnet@gmail.com  
vestnet
##### Entrepreneur User
jtallar@itba.edu.ar  
vestnet

#### Site URL
`http://pawserver.it.itba.edu.ar/paw-2020a-5/`
#### Logs File
`http://pawserver.it.itba.edu.ar/logs/paw2020a-vestnet.%d{yyyy-MM-dd}.log`


#### Configuration File
To build accordingly the JAR file, it is needed to add a config file called
`application.properties` at `paw/webapp/src/main/resources/application.properties`

The needed fields for it to work are:
```properties
postgres.url=jdbc:postgresql://localhost/paw-2020a-5
postgres.username=...
postgres.password=...

mail.host=smtp.gmail.com
mail.port=587
mail.username=...
mail.password=...
```

