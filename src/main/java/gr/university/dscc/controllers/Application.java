package gr.university.dscc.controllers;

import javax.ws.rs.ApplicationPath;

/**
 * main application path under which all the repository issues (through REST) will be handled
 */
@ApplicationPath("/api")
public class Application extends javax.ws.rs.core.Application {
}
