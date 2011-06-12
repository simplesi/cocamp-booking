
package uk.org.woodcraft.bookings.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "myVillageSignupResponse", namespace = "http://soap.bookings.woodcraft.org.uk/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "myVillageSignupResponse", namespace = "http://soap.bookings.woodcraft.org.uk/")
public class MyVillageSignupResponse {

    @XmlElement(name = "result", namespace = "")
    private uk.org.woodcraft.bookings.soap.MyVillageResponse _result;

    /**
     * 
     * @return
     *     returns MyVillageResponse
     */
    public uk.org.woodcraft.bookings.soap.MyVillageResponse getResult() {
        return this._result;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setResult(uk.org.woodcraft.bookings.soap.MyVillageResponse _result) {
        this._result = _result;
    }

}
