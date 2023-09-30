package com.tikelespike.nilee.app.sessions;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        PlayerSessionManager playerSessionManager = applicationContext.getBean(PlayerSessionManager.class);
        playerSessionManager.destroy();
    }
}
