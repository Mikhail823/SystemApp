module ru.popov.systemapp {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires java.desktop;
    requires net.synedra.validatorfx;
    requires org.xerial.sqlitejdbc;
    requires org.apache.logging.log4j;
    opens ru.popov.systemapp to javafx.fxml;
    opens ru.popov.systemapp.controller;
    opens ru.popov.systemapp.dto;
    opens ru.popov.systemapp.exception;
    opens ru.popov.systemapp.model;
    opens ru.popov.systemapp.dao;
    opens ru.popov.systemapp.dialog;
    opens ru.popov.systemapp.service;
    opens ru.popov.systemapp.util;
    exports ru.popov.systemapp;
}