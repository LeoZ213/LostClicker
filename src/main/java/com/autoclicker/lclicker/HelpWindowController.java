package com.autoclicker.lclicker;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class HelpWindowController {

    /**
     * Handles the click event of the GitHub link button in the Help window.
     * Opens the GitHub repository URL in the default web browser.
     * @throws URISyntaxException if the syntax of the URI is incorrect.
     * @throws IOException if an I/O error occurs.
     */
    public void gitHyperLinkClicked() throws URISyntaxException, IOException {
        URI githubLink = new URI("https://github.com/LeoZ213/AutoClicker");
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)){
                desktop.browse(githubLink);
            }
        }
    }

    /**
     * Handles the click event of the author link button in the Help window.
     * Opens the author's GitHub profile URL in the default web browser.
     * @throws URISyntaxException if the syntax of the URI is incorrect.
     * @throws IOException if an I/O error occurs.
     */
    public void authorHyperLinkClicked () throws URISyntaxException, IOException{
        URI githubProfileLink = new URI("https://github.com/LeoZ213/");
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)){
                desktop.browse(githubProfileLink);
            }
        }
    }
}
