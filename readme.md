# GT WebSocket Server

<h1>Installation</h1>
<ul>
<li>Java 8 and Maven is required to run this application.</li>
<li>IntelliJ Idea is preferred since it automatically sets up JDK and download maven repositories.</li>
</ul>

<h1>How to run?</h1>
<ol>
<li>Clone the repository.</li>
<li>Take a copy of the file <code>src/resources/application.properties.example</code> 
and rename it to <code>application.properties</code>.</li>
<li>Set up the environment variables properly in <code>application.properties</code> file. An explanation of the environment variables are provided below.</li>
<li>Now run the application in IntelliJ Idea. If you prefer to run using terminal, use
<code>mvn springboot:run</code></li>
</ol>

<h1>Environment Variables</h1>
<table>
<tr>
<th>Variable Name</th>
<th>Description</th>
<th>Recommended/ Default Value</th>
</tr>

<tr>
<td>spring.data.mongodb.uri</td>
<td>Connection URI for the MongoDB</td>
<td>mongodb://localhost:27017/mde</td>
</tr>

<tr>
<td>server.port</td>
<td>Port the server should run</td>
<td>8080</td>
</tr>

<tr>
<td>custom.cors.allowed_urls</td>
<td>The frontend URL. The server will set the CORS header to this URL. If there are multiple URLs, add them separated by commas</td>
<td>http://localhost</td>
</tr>

<tr>
<td>custom.concurrency.async.config.core_pool_size</td>
<td>Core size of the Thread Pool used by Spring Boot</td>
<td>200</td>
</tr>

<tr>
<td>custom.concurrency.async.config.max_pool_size</td>
<td>Max size of the Thread Pool used by Spring Boot</td>
<td>1000000</td>
</tr>

<tr>
<td>custom.gt-api.hostname</td>
<td>Host name of GT API Server</td>
<td>http://localhost:3000</td>
</tr>

<tr>
<td>custom.ws.message_queue.enable_external_broker</td>
<td>Specifies whether an external Message Broker should be configured or not.
If this is false, no need to configure subsequent env variables starting with 
<code>custom.ws.message_queue</code>. Leave them as they are.</td>
<td>false</td>
</tr>

<tr>
<td>custom.ws.message_queue.enable_ssl</td>
<td>Specifies whether to use SSL or not when connecting to Message Broker</td>
<td>false</td>
</tr>

<tr>
<td>custom.ws.message_queue.relay_host</td>
<td>Host Name of Message Broker</td>
<td>localhost</td>
</tr>

<tr>
<td>custom.ws.message_queue.relay_port</td>
<td>Message Broker' port</td>
<td>61613</td>
</tr>

<tr>
<td>custom.ws.message_queue.client_login</td>
<td>Client login username provided by Message Broker</td>
<td>guest</td>
</tr>

<tr>
<td>custom.ws.message_queue.client_passcode</td>
<td>Client login password provided by Message Broker</td>
<td>guest</td>
</tr>

<tr>
<td>custom.ws.message_queue.system_login</td>
<td>System login username provided by Message Broker</td>
<td>guest</td>
</tr>

<tr>
<td>custom.ws.message_queue.system_passcode</td>
<td>System login password provided by Message Broker</td>
<td>guest</td>
</tr>

<tr>
<td>server.ssl.certificate</td>
<td>If the server should use SSL, use this variable to set up SSL Certificate. The value should be the path to certificate file.</td>
<td>classpath:certs/fullchain.pem</td>
</tr>

<tr>
<td>server.ssl.certificate-private-key</td>
<td>If the server should use SSL, use this variable to set up SSL Private Key. The value should be the path to private key file.</td>
<td>classpath:certs/privkey.pem</td>
</tr>

</table>