package com.group0179;

import com.group0179.PresenterFactory.AttendeePresenterFactory;
import com.group0179.PresenterFactory.LoginPresenterFactory;
import com.group0179.PresenterFactory.OrganizerPresenterFactory;
import com.group0179.controllers.AttendeePresenter;
import com.group0179.controllers.LoginController;
import com.group0179.entities.Organizer;
import com.group0179.filters.*;
import com.group0179.scenes.*;
import com.group0179.presenters.*;
import com.group0179.gateways.*;
import com.group0179.use_cases.*;
import javafx.application.Application;

import java.io.IOException;

/**
 * Application entrypoint
 * @author Justin Chan
 */

public class AppMain {
    public static void main(String[] args) {
        // Gateways
        MessageManagerGateway messageManagerGateway= new MessageManagerGateway();
        UserManagerGateway userManagerGateway = new UserManagerGateway();
        RoomManagerGateway roomManagerGateway = new RoomManagerGateway();

        // Use Cases (deserialized from external file)
        UserManager userManager = userManagerGateway.read("usermanager.ser");
        RoomManager roomManager = roomManagerGateway.read("roommanager.ser");
        MessageManager messageManager = messageManagerGateway.read("messagemanager.ser");

        // Input filters
        LoginFilter loginFilter = new LoginFilter(userManager, roomManager, messageManager);
        SpeakerFilter speakerFilter = new SpeakerFilter(userManager, roomManager, messageManager);
        OrganizerFilter organizerFilter = new OrganizerFilter(userManager, roomManager, messageManager);

        // Presenters
        ILoginPresenter loginPresenter = new LoginPresenterEN(); // TODO: Figure out how to switch presenters.
        SpeakerPresenter speakerPresenter = new SpeakerPresenter(userManager, roomManager, messageManager);
        OrganizerPresenterEN organizerPresenter = new OrganizerPresenterEN(userManager, roomManager, messageManager);

        //PresenterFactory
        LoginPresenterFactory loginPresenterFactory = new LoginPresenterFactory();
        OrganizerPresenterFactory organizerPresenterFactory = new OrganizerPresenterFactory(userManager, roomManager, messageManager);
        AttendeePresenterFactory attendeePresenterFactory = new AttendeePresenterFactory();

        // Controllers
        LoginController loginController = new LoginController(userManager);
        AttendeePresenter attendeePresenter = new AttendeePresenter(userManager, roomManager, messageManager);

        // Scene Setup
        LoginScene loginScene = new LoginScene(loginFilter, loginPresenterFactory, loginController);
        OrganizerScene organizerScene = new OrganizerScene(organizerFilter, organizerPresenterFactory, loginController);
        AttendeeScene attendeeScene = new AttendeeScene(attendeePresenter, loginController, attendeePresenterFactory);
        SpeakerScene speakerScene = new SpeakerScene(speakerFilter, speakerPresenter, loginController);
        LanguageScene languageScene = new LanguageScene();

        // Set up MainView and launch
        MainView.setup(loginScene, organizerScene, attendeeScene, speakerScene, languageScene);
        Application.launch(MainView.class);

        // Attempt to serialize all Use Case data to external file.
        try {
            userManagerGateway.write(userManager, "usermanager.ser");
            roomManagerGateway.write(roomManager, "roommanager.ser");
            messageManagerGateway.write(messageManager, "messagemanager.ser");
        } catch (IOException e) {
            System.out.println("Unable to write manager data to file.");
            e.printStackTrace();
        }
    }
}
