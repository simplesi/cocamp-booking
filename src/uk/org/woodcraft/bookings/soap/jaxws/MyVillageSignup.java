package uk.org.woodcraft.bookings.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "myVillageSignup", namespace = "http://soap.bookings.woodcraft.org.uk/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "myVillageSignup", namespace = "http://soap.bookings.woodcraft.org.uk/", propOrder = {
    "eventKey",
    "userEmail",
    "userKey"
})
public class MyVillageSignup {

    @XmlElement(name = "eventKey", namespace = "")
    private String eventKey;
    @XmlElement(name = "userEmail", namespace = "")
    private String userEmail;
    @XmlElement(name = "userKey", namespace = "")
    private String userKey;
    @XmlElement(name = "externalUsername", namespace = "")
    private String externalUsername;
    /**
     * 
     * @return
     *     returns String
     */
    public String getEventKey() {
        return this.eventKey;
    }

    /**
     * 
     * @param eventKey
     *     the value for the eventKey property
     */
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getUserEmail() {
        return this.userEmail;
    }

    /**
     * 
     * @param userEmail
     *     the value for the userEmail property
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getUserKey() {
        return this.userKey;
    }

    /**
     * 
     * @param userKey
     *     the value for the userKey property
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

	public void setExternalUsername(String externalUsername) {
		this.externalUsername = externalUsername;
	}

	public String getExternalUsername() {
		return externalUsername;
	}
}