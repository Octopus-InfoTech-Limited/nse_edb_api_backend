package com.octopus_tech.share.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailUtil
{
	private EmailUtil()
	{}
	
	public static void sendMail(String to, String subject, String body) throws Exception
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties();
		
		String host = ep.getProperty("smtp.host", "smtp.googlemail.com");
		int port = ep.getNumber("smtp.port", 465).intValue();
		String username = ep.getProperty("smtp.username", "");
		String password = ep.getProperty("smtp.password", "");
		String from = ep.getProperty("smtp.from");
		boolean debug = "true".equals(ep.getProperty("smtp.debug"));
		
		org.simplejavamail.api.email.Email email = EmailBuilder.startingBlank()
		          .to(to)
		          //.ccWithFixedName("C. Bo group", "chocobo1@candyshop.org", "chocobo2@candyshop.org")
		          //.withRecipientsUsingFixedName("Tasting Group", BCC,
		          //              "taster1@cgroup.org;taster2@cgroup.org;tester <taster3@cgroup.org>")
		          //.bcc("Mr Sweetnose <snose@candyshop.org>")
		          //.withReplyTo("lollypop", "lolly.pop@othermail.com")
		          .withSubject(subject)
		          .withHTMLText(body)
		          .from(from)
		          //.withPlainText("Please view this email in a modern email client!")
		          //.withCalendar(CalendarMethod.REQUEST, iCalendarText)
		          //.withEmbeddedImage("wink1", imageByteArray, "image/png")
		          //.withEmbeddedImage("wink2", imageDatesource)
		          //.withAttachment("invitation", pdfByteArray, "application/pdf")
		          //.withAttachment("dresscode", odfDatasource)
		          //.withHeader("X-Priority", 5)
		          //.withReturnReceiptTo()
		          //.withDispositionNotificationTo("notify-read-emails@candyshop.com")
		          //.withBounceTo("tech@candyshop.com")
		          //.signWithDomainKey(privateKeyData, "somemail.com", "selector") // DKIM
		          //.signWithSmime(pkcs12Config)
		          //.encryptWithSmime(x509Certificate)
		          .buildEmail();

		Mailer mailer = MailerBuilder
		          .withSMTPServer(host, port, username, password)
		          .withTransportStrategy(TransportStrategy.SMTP_TLS)
		          //.withProxy("socksproxy.host.com", 1080, "proxy user", "proxy password")
		          .withSessionTimeout(10 * 1000)
		          .clearEmailAddressCriteria() // turns off email validation
		          .withProperty("mail.smtp.sendpartial", true)
		          .withDebugLogging(debug)
		          //.async()
		          // not enough? what about this:
		          //.withClusterKey(myPowerfulMailingCluster);
		          //.withThreadPoolSize(20); // multi-threaded batch handling
		          //.withConnectionPoolCoreSize(10); // reusable connection(s) / multi-server sending
		          //.withCustomSSLFactoryClass("org.mypackage.MySSLFactory");
		          //.withCustomSSLFactoryInstance(mySSLFactoryInstance);
		          //.manyMoreOptions()
		          .buildMailer();

		mailer.sendMail(email);
	}
	
	public static void sendMail2(String to, String subject, String body) throws Exception
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties();
		
		HtmlEmail email = new HtmlEmail();
		if("tls".equals(ep.getProperty("smtp.securty")))
		{
			email.setStartTLSRequired(true);
		}
		
		if("true".equals(ep.getProperty("smtp.debug")))
		{
			email.setDebug(true);
		}

		email.setSSLCheckServerIdentity(false);
		email.setHostName(ep.getProperty("smtp.host", "smtp.googlemail.com"));
		email.setSmtpPort(ep.getNumber("smtp.port", 465).intValue());
		email.setAuthenticator(new DefaultAuthenticator(ep.getProperty("smtp.username", ""), ep.getProperty("smtp.password", "")));
		email.setSSLOnConnect(true);
		email.setFrom(ep.getProperty("smtp.from"));
		email.setSubject(subject);
		email.setHtmlMsg(body);
		email.addTo(to);
		email.send();
	}
}
