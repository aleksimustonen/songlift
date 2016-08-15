# urn.fi.songlift.nk
Random ROC and Microservice prototyping


Try SOAP with

POST /soap-test-server/test1 HTTP/1.1
Content-Type: application/soap+xml
soapAction: urn:org:ten60:soap:test:ping
Host: localhost:8080
Connection: close
User-Agent: Paw/3.0.5 (Macintosh; OS X/10.11.5) GCDHTTPRequest
Content-Length: 161

<env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/">
  <env:Body>
    <p:ping xmlns:p="http://1060.org/" />
  </env:Body>
</env:Envelope>

