import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.Element

println "\ninput message"
inputMessage = context.source("arg:message")
println inputMessage.dump()

namespaces = """
<namespaces>
  <namespace>
    <prefix>s11</prefix>
    <uri>http://schemas.xmlsoap.org/soap/envelope/</uri>
  </namespace>
  <namespace>
    <prefix>s12</prefix>
    <uri>http://www.w3.org/2003/05/soap-envelope</uri>
  </namespace>
</namespaces>
"""


//println "\nresolve payload tag local name"
xpath = context.createRequest("active:xpath2")
xpath.addArgumentByValue("operand", inputMessage)
xpath.addArgumentByValue("operator", "(//s11:Body|/s12:Body)/local-name(*[1])")
xpath.addArgumentByValue("namespaces", namespaces)
xpathResult = context.issueRequest(xpath)
println "\nxpathResult " + xpathResult

//println "\ntemplate"
DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance()
DocumentBuilder docBuilder = docFactory.newDocumentBuilder()
Document template = docBuilder.newDocument()
Element rootElement = template.createElement(xpathResult)
template.appendChild(rootElement)

//println "\ninput payload"
inputPayload = context.createRequest("active:wsSOAPGetBody")
inputPayload.addArgumentByValue("message", inputMessage)
inputPayload.addArgumentByValue("body", template)

println "\noutput payload"
outputPayload = context.issueRequest(inputPayload) as Document
docelement = outputPayload.getDocumentElement() as Element
inputNodename = docelement.getTagName()
outputNodename = inputNodename.contains("Request") ? inputNodename.replace("Request", "Response") : inputNodename
namespace = docelement.getNamespaceURI()
outputPayload.renameNode(docelement, namespace, outputNodename)
println outputPayload.dump()

//println "\noutput envelope template"
outputEnvelopeTemplate = context.createRequest("active:wsNewSOAPMessage")  //create new SOAP Envelope
outputEnvelopeTemplate.addArgumentByValue("message", inputMessage) // set SOAP version to match the input
outputEnvelope = context.issueRequest(outputEnvelopeTemplate)

//println "\noutput body"
outputBodyTemplate = context.createRequest("active:wsSOAPAddBody") //create new SOAP Body
outputBodyTemplate.addArgumentByValue("body", outputPayload)  //attach content to Body
outputBodyTemplate.addArgumentByValue("message", outputEnvelope) //attach Body to Envelope
outputBody = context.issueRequest(outputBodyTemplate)

//println "\nresponse"
context.createResponseFrom(outputBody)