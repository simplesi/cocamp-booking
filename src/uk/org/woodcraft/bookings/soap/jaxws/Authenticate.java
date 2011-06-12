
package uk.org.woodcraft.bookings.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "authenticate", namespace = "http://soap.bookings.woodcraft.org.uk/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticate", namespace = "http://soap.bookings.woodcraft.org.uk/", propOrder = {
    "siteKey",
    "username",
    "password"
})
public class Authenticate {

    @XmlElement(name = "siteKey", namespace = "")
    private String siteKey;
    @XmlElement(name = "username", namespace = "")
    private String username;
    @XmlElement(name = "password", namespace = "")
    private String password;

    /**
     * 
     * @return
     *     returns String
     */
    public String getSiteKey() {
        return this.siteKey;
    }

    /**
     * 
     * @param siteKey
     *     the value for the siteKey property
     */
    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 
     * @param username
     *     the value for the arg1 property
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 
     * @param password
     *     the value for the password property
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
