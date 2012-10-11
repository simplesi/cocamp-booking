package uk.org.woodcraft.bookings.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uk.org.woodcraft.bookings.datamodel.User;


public class EmailUtils {

	public static void emailUser(User user, String subject, String body)
	{
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            
            msg.setFrom(new InternetAddress("bookings-email-archive@woodcraft.org.uk", "Woodcraft Folk Bookings"));
           // msg.setFrom(new InternetAddress("simon@simplesi.org", "CoCamp Bookings Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail(), user.getName()));
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress("bookings-email-archive@woodcraft.org.uk", user.getName()));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);

        // FIXME: Exception handling cleanup
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
        	throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
        	throw new RuntimeException(e);
		}
	}
}
