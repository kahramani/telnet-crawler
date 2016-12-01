package com.kahramani.crawler.telnet.config;

import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

/**
 * Created by kahramani on 11/29/2016.
 */
@Configuration
@Scope("prototype")
public class TelnetConfiguration {

    public static final int DEFAULT_CONNECTION_TIMEOUT = 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 0;
    public static final long DEFAULT_LOGIN_TIMEOUT = 5000;
    public static final long DEFAULT_COMMAND_RESPONSE_TIMEOUT = 1000;
    public static final long DEFAULT_POST_COMMAND_DELAY = 1000;
    public static final boolean DEFAULT_LOGIN_REQUIRED = true;

    private String username;
    private String password;
    private String usernamePrompt;
    private String passwordPrompt;
    private String devicePrompt;
    private int connectionTimeOut;
    private int socketTimeOut;
    private long loginTimeOut;
    private long commandResponseTimeOut;
    private long postCommandDelay;
    private boolean loginRequired;
    @Autowired
    private PropertyHelper propertyHelper;

    @Bean
    protected TelnetConfiguration initialize() {
        return new TelnetConfiguration();
    }

    protected void setConfiguration(PropertyPrefix propertyPrefix) {
        Assert.isTrue(propertyPrefix != null, "'propertyPrefix' cannot be null to change configuration");
        String prefix = propertyPrefix.getPrefix();
        this.username = this.propertyHelper.getString(prefix + ".username");
        this.password = this.propertyHelper.getString(prefix + ".password");
        this.usernamePrompt = this.propertyHelper.getString(prefix + ".username.prompt.pattern");
        this.passwordPrompt = this.propertyHelper.getString(prefix + ".password.prompt.pattern");
        this.devicePrompt = this.propertyHelper.getString(prefix + ".device.prompt.pattern");
        this.connectionTimeOut = this.propertyHelper.getInt(prefix + ".connection.timeOut",
                DEFAULT_CONNECTION_TIMEOUT);
        this.socketTimeOut = this.propertyHelper.getInt(prefix + ".socket.timeOut",
                DEFAULT_SOCKET_TIMEOUT);
        this.loginTimeOut = this.propertyHelper.getLong(prefix + ".login.timeOut",
                DEFAULT_LOGIN_TIMEOUT);
        this.commandResponseTimeOut = this.propertyHelper.getLong(prefix + ".response.timeOut",
                DEFAULT_COMMAND_RESPONSE_TIMEOUT);
        this.postCommandDelay = this.propertyHelper.getLong(prefix + ".post.command.delay",
                DEFAULT_POST_COMMAND_DELAY);
        this.loginRequired = DEFAULT_LOGIN_REQUIRED;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernamePrompt() {
        return usernamePrompt;
    }

    public void setUsernamePrompt(String usernamePrompt) {
        this.usernamePrompt = usernamePrompt;
    }

    public String getPasswordPrompt() {
        return passwordPrompt;
    }

    public void setPasswordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
    }

    public String getDevicePrompt() {
        return devicePrompt;
    }

    public void setDevicePrompt(String devicePrompt) {
        this.devicePrompt = devicePrompt;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public long getLoginTimeOut() {
        return loginTimeOut;
    }

    public void setLoginTimeOut(long loginTimeOut) {
        this.loginTimeOut = loginTimeOut;
    }

    public long getCommandResponseTimeOut() {
        return commandResponseTimeOut;
    }

    public void setCommandResponseTimeOut(long commandResponseTimeOut) {
        this.commandResponseTimeOut = commandResponseTimeOut;
    }

    public long getPostCommandDelay() {
        return postCommandDelay;
    }

    public void setPostCommandDelay(long postCommandDelay) {
        this.postCommandDelay = postCommandDelay;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }
}
