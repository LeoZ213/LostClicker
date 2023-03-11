package com.autoclicker.lclicker;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class HelpWindowController {

    public void gitHyperLinkClicked() throws URISyntaxException, IOException {
        URI githubLink = new URI("https://github.com/LeoZ213/AutoClicker");
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)){
                desktop.browse(githubLink);
            }
        }
    }

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
