package uk.org.woodcraft.bookings.soap;

import java.util.Iterator;

import javax.xml.bind.JAXB;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SAAJResult;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

import uk.org.woodcraft.bookings.soap.jaxws.Authenticate;
import uk.org.woodcraft.bookings.soap.jaxws.AuthenticateResponse;
import uk.org.woodcraft.bookings.soap.jaxws.MyVillageSignup;
import uk.org.woodcraft.bookings.soap.jaxws.MyVillageSignupResponse;

public class BookingsAPISOAPHandler {
	private static final String NAMESPACE_URI = "http://soap.bookings.woodcraft.org.uk/";
	private static final QName AUTHENTICATE_QNAME = new QName(NAMESPACE_URI, "authenticate");
	private static final QName MYVILLAGE_SIGNUP_QNAME = new QName(NAMESPACE_URI, "myVillageSignup");
	
	private MessageFactory messageFactory;
	private BookingsAPIAdaptor apiAdaptor;
	
	public BookingsAPISOAPHandler() throws SOAPException {
		messageFactory = MessageFactory.newInstance();
		apiAdaptor = new BookingsAPIAdaptor();
	}
	
	
	public SOAPMessage handleSOAPRequest(SOAPMessage request) throws SOAPException {
	    SOAPBody soapBody = request.getSOAPBody();
	    @SuppressWarnings("rawtypes")
		Iterator iterator = soapBody.getChildElements();
	    AuthenticateResponse authResponse = null;
	    Object responsePojo = null;
	    
	    while (iterator.hasNext()) {
	      Object next = iterator.next();
	      if (next instanceof SOAPElement) {
	        SOAPElement soapElement = (SOAPElement) next;
	        QName qname = soapElement.getElementQName();
	          if (AUTHENTICATE_QNAME.equals(qname)) {
	            authResponse = handleAuthRequest(soapElement);
	            break;
	          } else if (MYVILLAGE_SIGNUP_QNAME.equals(qname)) {
	            responsePojo = handleMyVillageSignupRequest(soapElement);
	            break;
	          }
	      }
	    }
	    
	    SOAPMessage soapResponse = messageFactory.createMessage();
	    soapBody = soapResponse.getSOAPBody();
	    
	    if (authResponse == null)
	    {
	    	SOAPFault fault = soapBody.addFault();
		    fault.setFaultString("Missing authetication data");
		    return soapResponse;
		    
	    } else if (!authResponse.isReturn())
	    {
	    	SOAPFault fault = soapBody.addFault();
		    fault.setFaultString("Failed authentication");
		    return soapResponse;
	    }
	    	
	    if (responsePojo != null) {
	      JAXB.marshal(responsePojo, new SAAJResult(soapBody));
	    } else {
	      SOAPFault fault = soapBody.addFault();
	      fault.setFaultString("Unrecognized SOAP request.");
	    }
	    return soapResponse;
	  }


	private AuthenticateResponse handleAuthRequest(SOAPElement soapElement) {
		Authenticate request = JAXB.unmarshal(new DOMSource(soapElement), Authenticate.class);
		return apiAdaptor.authenticate(request);
	}

	private MyVillageSignupResponse handleMyVillageSignupRequest(SOAPElement soapElement) {
		MyVillageSignup request = JAXB.unmarshal(new DOMSource(soapElement), MyVillageSignup.class);
		return apiAdaptor.myVillageSignup(request);
	}
}
