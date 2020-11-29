package com.group0179.scenes;

import com.group0179.MainView;
import com.group0179.exceptions.InvalidCredentialsException;
import com.group0179.exceptions.UsernameTakenException;
import com.group0179.filters.LoginFilter;
import com.group0179.presenters.LoginPresenter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.swing.*;

/**
 * LoginScene implementation. Responsible
 * for handling user login and account creation.
 * @author Zachariah Vincze
 */

public class LoginScene implements IScene {
    LoginFilter filter;
    LoginPresenter presenter;
    BorderPane borderPane;
    Scene mainScene;

    // Buttons
    Button loginScreenBtn;
    Button createAccountScreenBtn;

    Button loginBtn;
    Button createAccountBtn;

    // TextFields
    TextField loginTextField;
    TextField createAccountTextField;

    // Labels
    Label loginInfo;
    Label createAccountInfo;

    ChoiceBox accountTypeChoiceBox;

    // Grids
    GridPane loginGrid;
    GridPane createAccountGrid;

    // Top menu
    HBox topMenu;

    public LoginScene(LoginFilter filter, LoginPresenter presenter) {
        this.filter = filter;
        this.presenter = presenter;
    }

    private void changeToLoginPane() {
        borderPane.setCenter(loginGrid);
    }

    private void changeToCreateAccountPane() {
        borderPane.setCenter(createAccountGrid);
    }

    private void attemptLogin() {
        String username = loginTextField.getText();
        try {
            String accountType = presenter.loginUser(username);
            switch (accountType) {
                case "attendee":
                    MainView.setAttendeeScene();
                case "organizer":
                    MainView.setOrganizerScene();
                case "speaker":
                    MainView.setSpeakerScene();
            }
        } catch (InvalidCredentialsException e) {
            loginInfo.setText(e.getMessage());
        }
    }

    private void attemptAccountCreate() {
        String username = createAccountTextField.getText();
        String accountType = (String)accountTypeChoiceBox.getValue();
        try {
            presenter.createAccount(accountType, username);
        } catch (UsernameTakenException e) {
            createAccountInfo.setText(e.getMessage());
        }
        createAccountInfo.setText(presenter.accountCreationSuccess());
    }

    private void constructLoginGrid() {
        loginGrid = new GridPane();
        loginTextField = new TextField();
        loginBtn = new Button("Login");
        loginBtn.setOnAction(actionEvent -> {
            attemptLogin();
        });
        loginInfo = new Label();

        // Layout components
        loginGrid.add(loginTextField, 0, 0);
        loginGrid.add(loginBtn, 1, 0);
        loginGrid.add(loginInfo, 0, 1);
    }

    private void constructCreateAccountGrid() {
        createAccountGrid = new GridPane();
        accountTypeChoiceBox = new ChoiceBox();
        accountTypeChoiceBox.getItems().addAll(presenter.attendeeAccountChoice(),
                presenter.organizerAccountChoice(),
                presenter.speakerAccountChoice());
        accountTypeChoiceBox.setValue(presenter.attendeeAccountChoice());
        createAccountBtn = new Button("Create Account");
        createAccountBtn.setOnAction(actionEvent -> {
            attemptAccountCreate();
        });
        createAccountTextField = new TextField();
        createAccountInfo = new Label();

        // Layout components
        createAccountGrid.add(accountTypeChoiceBox, 0, 0);
        createAccountGrid.add(createAccountTextField, 1, 0);
        createAccountGrid.add(createAccountBtn, 0, 1);
        createAccountGrid.add(createAccountInfo, 1, 1);
    }

    @Override
    public void constructScene() {
        loginScreenBtn = new Button("Login");
        loginScreenBtn.setOnAction(actionEvent -> {
            changeToLoginPane();
        });
        createAccountScreenBtn = new Button("Create Account");
        createAccountScreenBtn.setOnAction(actionEvent -> {
            changeToCreateAccountPane();
        });
        topMenu = new HBox();
        topMenu.getChildren().addAll(loginScreenBtn, createAccountScreenBtn);

        borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        constructLoginGrid();
        constructCreateAccountGrid();

        // Set the default main grid
        borderPane.setCenter(loginGrid);

        mainScene = new Scene(borderPane, x, y);
    }

    @Override
    public void setScene() {
        MainView.getStage().setScene(mainScene);
    }
}
