package com.kahramani.crawler.telnet.action;

import com.kahramani.crawler.telnet.config.TelnetConfiguration;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.exception.TelnetResponseTimeOutException;
import com.kahramani.crawler.telnet.util.Chronometer;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kahramani on 11/29/2016.
 */
@Component
@Scope("prototype")
class TelnetDriver extends TelnetConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TelnetDriver.class);
    public static final int DEFAULT_TELNET_PORT = 23;

    private TelnetClient telnetClient;
    private InputStream reader; // to read response
    private OutputStream writer; // to write a command
    private String targetIp;
    private int targetPort;
    private boolean loggedin = false;

    @Bean
    protected TelnetDriver initialize() {
        return new TelnetDriver();
    }

    protected TelnetDriver() {
        super();
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    private void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public String getTargetIp() {
        return targetIp;
    }

    private void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getTargetPort() {
        return targetPort;
    }

    private void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public boolean isConnected() {
        return this.telnetClient != null && this.telnetClient.isConnected();
    }

    /**
     * to establish a telnet connection to a device
     * @param targetIp Ip wanted to be connected
     * @param targetPort Port wanted to be connected
     * @return a boolean which refers that connection and login (if required) is successful or not
     * @throws IOException if an error occurs in input or output stream operation
     */
    public boolean connect(String targetIp, int targetPort) throws IOException {
        Assert.hasText(targetIp, "'targetIp' cannot be null or empty");
        this.setTargetIp(targetIp);

        if (targetPort <= 0)
            targetPort = this.DEFAULT_TELNET_PORT;

        this.setTargetPort(targetPort);

        logger.debug("About to connect to " + this.address());

        this.telnetClient = new TelnetClient();
        this.telnetClient.setConnectTimeout(this.getConnectionTimeOut());
        this.telnetClient.connect(this.getTargetIp(), this.getTargetPort());
        if(this.getSocketTimeOut() > 0)
            this.telnetClient.setSoTimeout(this.getSocketTimeOut());
        this.reader = this.telnetClient.getInputStream();
        this.writer = this.telnetClient.getOutputStream();
        if (this.isLoginRequired())
            this.setLoggedin(this.login(this.getUsername(), this.getPassword()));
        else
            this.setLoggedin(true);

        if(this.isConnected() && this.isLoggedin()) {
            logger.info("Successfully connected and logged on to "
                    + this.address());
            return true;
        }

        return false;
    }

    /**
     * to disconnect from the device
     * @throws IOException if an error occurs in input or output stream operation
     */
    public void disconnect() throws IOException {
        if (this.isConnected()) {
            this.telnetClient.disconnect();
            logger.info("Successfully disconnected from " + this.address());
            this.nullEmAll();
            setLoggedin(false);
        }
    }

    /**
     * to run telnet command on device
     * @param command telnet command wanted to run
     * @return a StringBuilder which is the response of device -- IF response matched with the prompt
     * @throws IOException if an error occurs in input or output stream operation
     * @throws TelnetResponseTimeOutException if response does not match the expected prompt
     */
    public StringBuilder runCommand(String command) throws IOException, TelnetResponseTimeOutException {
        Chronometer cr = new Chronometer();
        if (isConnected()) {
            logger.debug("<" + command + "> will be executed for" +
                    " " + this.address());
            long postCommandDelay = this.getPostCommandDelay();
            write(command, postCommandDelay);
            long commandResponseTimeOut = this.getCommandResponseTimeOut();
            cr.start();
            StringBuilder sb = read(this.getDevicePrompt(), commandResponseTimeOut);
            cr.stop();
            if(sb != null) {
                logger.debug("Response of the command of <" + command + ">" +
                        " caught in " + cr.getDuration() +
                        " from " + this.address());
                return sb;
            }
        } else
            throw new IOException("There is no connection to execute a command");

        logger.debug("Response of the command of <" + command + ">" +
                " could not be caught in " + cr.getDuration() +
                " from " + this.address());

        return null;
    }

    /**
     * to login to device
     * @param username device's username to login
     * @param password device's password to login
     * @return a boolean value which implies login is success or not
     * @throws IOException if an error occurs in input stream operation
     */
    private boolean login(String username, String password) throws IOException {

        logger.debug("Login process started to: " + this.address());

        StringBuilder response;

        long loginTimeOut = this.getLoginTimeOut();
        long responseTimeOut = (long) Math.ceil((double) loginTimeOut / 10); // loop will be executed max 10 times

        String lookUpPrompt = this.getUsernamePrompt();
        boolean usernameWrote = false;
        boolean passwordWrote = false;
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        for(;chronometer.getTourDuration() < loginTimeOut;) {
            try {
                response = read(lookUpPrompt, responseTimeOut);

                if(!usernameWrote && StringUtils.hasText(response)) {
                    write(username, 0); // write username and then do not delay
                    usernameWrote = true;
                    lookUpPrompt = this.getPasswordPrompt(); // username is done, change prompt for password
                    response = read(lookUpPrompt, responseTimeOut); // read response for password's prompt
                }

                if(!passwordWrote && StringUtils.hasText(response)) {
                    write(password, 0); // write password and then do not delay
                    passwordWrote = true;
                    lookUpPrompt = this.getDevicePrompt(); // password is done, change prompt for general
                    response = read(lookUpPrompt, responseTimeOut); // read response for device's general prompt
                }

                // general device prompt showed up, login is successful!
                if(usernameWrote && passwordWrote && StringUtils.hasText(response)) {
                    chronometer.stop();
                    logger.debug("Login successful in " + chronometer.getDuration() +
                            " to " + this.address());
                    return true;
                }
            } catch (TelnetResponseTimeOutException e) {
                // catch but do nothing
            }
        }
        chronometer.stop();
        logger.debug("Login failed in " + chronometer.getDuration() +
                " to " + this.address());

        return false;
    }

    /**
     * to get devices' response to a command
     * @param prompt regex string which will be searched in response
     * @param responseTimeOut response timeOut (in millis)
     * @return a StringBuilder object that contains response -- IF response matched with prompt
     * @throws IOException if an error occurs in input stream operation
     * @throws TelnetResponseTimeOutException if response does not match the expected prompt
     */
    private StringBuilder read(String prompt, long responseTimeOut) throws IOException, TelnetResponseTimeOutException {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile(prompt, Pattern.MULTILINE);
        boolean responded = false;
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        for(;chronometer.getTourDuration() < responseTimeOut;) {
            int c;
            while ((c = this.reader.available()) > 0) {
                byte[] b = new byte[c];
                this.reader.read(b);
                sb.append(new String(b));
            }
            if(StringUtils.hasText(sb)) {
                responded = true;
            }
            Matcher m = p.matcher(sb);
            if(m.find()) {
                return sb;
            }
        }
        String e;
        if(responded) {
            e = "Device's response is too long to handle in specified timeOut("
                    + responseTimeOut + " millis).";
        } else {
            e = "Device did not respond.";
        }
        throw new TelnetResponseTimeOutException(e + " Address: " + this.address());
    }

    /**
     * to write with output stream -- Method has a loop with post command delay (in millis)
     * @param value string wanted to write
     * @param postDelay how long will be waited after write activity
     * @throws IOException if an error occurs in output stream operation
     */
    private void write(String value, long postDelay) throws IOException {
        writer.write(value.getBytes());
        writer.write((int) '\n');
        writer.flush();
        this.sleep(postDelay);
    }

    /**
     * to use thread sleep feature
     * @param t how long will be slept
     */
    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            logger.error("Thread sleep interrupted for value " + t, e);
        }
    }

    /**
     * to set null to driver variables
     */
    private void nullEmAll() {
        this.telnetClient = null;
        this.reader = null;
        this.writer = null;
        this.targetIp = null;
        this.targetPort = 0;
    }

    /**
     * to hold address which is currently operated -- Format IP:PORT
     * @return a StringBuilder which tells the device's address
     */
    private StringBuilder address() {
        char separator = ':';
        return new StringBuilder(this.getTargetIp()).append(separator).append(this.getTargetPort());
    }
}
