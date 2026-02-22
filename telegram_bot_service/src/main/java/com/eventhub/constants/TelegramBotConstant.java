package com.eventhub.constants;

import org.springframework.stereotype.Component;

@Component
public class TelegramBotConstant {
    public static final String  WELCOME_RETURNING_USER = "Welcome back! Your account is already linked. Use the menu or " +
            "the /events command to view your events.";
    public static final String WELCOME_NEW_USER = "Hi! To link your account and receive notifications, please enter the 6-digit code from your personal " +
            "account on the website.";
    public static final String AUTH_FAILED = "The code is incorrect or expired.";
    public static final String AUTH_REQUIRED = "You are not allowed to access this resource.";
    public static final String ERROR_FETCHING_EVENTS = "Failed to load your events. Please try again later.";
    public static final String NO_EVENTS = "You don't have any scheduled events yet.";
    public static final String NO_EVENTS_TODAY = "No events scheduled for today.";
    public static final String EVENTS_TITLE_ALL = "Your Events:";
    public static final String EVENTS_TITLE_TODAY = "Today's Events:";
    public static final String SERVICE_UNAVAILABLE =  "Service unavailable";
    public static final String DELETE_ACCOUNT = "Your account has been unlinked. Notifications are now disabled.";
    public static final String INVALID_COMMAND = "I'm not sure how to handle this. Try /help!";
    public static final String ERR_DATA_NOT_FOUND = "Sorry, but this data was not found or expired.";
    public static final String ERR_AUTH_FAILED = "Authentication failed between services.";
    public static final String ERR_UNEXPECTED_SERVER = "Oops! Something went wrong. We are already fixing it!";
    public static final String NOT_FOUND_TELEGRAM_USER = "No record of this account being linked was found in the system.";
    public static final String HELP_MESSAGE = """
        To start using the bot, you need to link your account:
        1. Log in to your personal account on our website.
        2. Find the 6-digit **verification code**.
        3. Send this code directly to me in this chat.
        
        *Available Commands:*
        /start - Show welcome message
        /events - View all your registered events
        /today - View events scheduled for today only
        /help - Show this help information
        
        If you have any issues, please contact our support team.
        """;

    private TelegramBotConstant() {}
}
