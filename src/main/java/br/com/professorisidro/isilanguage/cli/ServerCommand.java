package br.com.professorisidro.isilanguage.cli;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import picocli.CommandLine.Command;

@Command(name = "server", description = "Runs a REST server for compiling IsiLanguage programs")
public class ServerCommand extends ServerResource implements Runnable {

    public void run() {
        try {
            String portStr = System.getenv("PORT");
            if (portStr == null || portStr.trim().isEmpty())
                portStr = "8080";
            int port = Integer.parseInt(portStr);

            Component component = new Component();
            component.getServers().add(Protocol.HTTP, port);

            Router router = new Router(component.getContext().createChildContext());
            router.attach("/compile", ServerCommand.class);

            component.getDefaultHost().attach(router);
            component.start();
            System.in.read();
            component.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("ERROR " + ex.getMessage());
        }
    }

    @Get
    public String toString() {
        return "Hello world";
    }

}
