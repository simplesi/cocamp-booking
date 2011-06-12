
package uk.org.woodcraft.bookings.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "authenticateResponse", namespace = "http://soap.bookings.woodcraft.org.uk/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticateResponse", namespace = "http://soap.bookings.woodcraft.org.uk/")
public class AuthenticateResponse {

    @XmlElement(name = "success", namespace = "")
    private boolean _return;

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(boolean _return) {
        this._return = _return;
    }

}
