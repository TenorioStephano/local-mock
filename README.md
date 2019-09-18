# Local-Mock

### How to execute? 
There are two way's for use this project, are be: 
- Generating an jar with maven using profile **"build"** and run with "java -jar nameOfJar.jar"
- Execute into the IDE "Intelli J", on this case not use profile of Maven.

### How to use?
After the start of project, you can use mock of REST and IBM JMS. 
- To use REST, define an property into the *application.properties*. 
Example: ` curl http://localhost:{port}/mock?contextPath={yourContext} `
```
**yourContext**={path}/fileOutput.{extention of file}
```
- To use IBM JMS, define some propeties into the *application.properties*. Example: send Message for queue INPUT with reply to queue OUTPUT
```
**mock.queue.name**=INPUT

**OUTPUT**={path}/fileOutput.{extention of file}
```
**Observation:** If your request have not a **reply to**, the mock will not response and the message and will be **ignored**.

#### Port
You can modify the port of application altering the propety with name *"PORT"*. If you remove this property, the port will be 8081 by default.

#### History changes in mock
If you change a mock file response, after the next request she will be saved in DB.

#### Database
To access the database send a request for the context **"/h2-console"**.
- For see all request saved in DB, utilize this query: `select * from MOCK`.
- You can modify the context for access a DB on property *"spring.h2.console.path"*.
- For disabled this feature, change a property *"spring.h2.console.enabled"* to false.
- The credentials to access are by default: 
  - Username: sa
  - password: password
  
 
