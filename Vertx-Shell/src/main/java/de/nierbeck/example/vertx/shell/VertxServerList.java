/*
   Copyright 2016 Achim Nierbeck

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package de.nierbeck.example.vertx.shell;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;

import io.vertx.core.http.impl.HttpServerImpl;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.NetServerBase;
import io.vertx.core.net.impl.NetServerImpl;
import io.vertx.core.net.impl.ServerID;

@Command(scope = "vertx", name = "netlist", description = "Lists all running Servers")
@Service
public class VertxServerList extends AbstractVertxCommand {

    @Override
    public Object execute() throws Exception {

        ShellTable table = new ShellTable();

        table.column("HttpServer");
        table.column("NetServer");
        table.column("Host");
        table.column("Port");
        
        VertxInternal vertx = (VertxInternal) getVertxService();
        for (Entry<ServerID, NetServerBase> server : vertx.sharedNetServers().entrySet()) {
            table.addRow().addContent("", "X", server.getKey().host, server.getKey().port);
        }
        for (Map.Entry<ServerID, HttpServerImpl> server : vertx.sharedHttpServers().entrySet()) {
            table.addRow().addContent("X", "", server.getKey().host, server.getKey().port);
        }
        
        try {
            table.print(System.out);
        } catch (Throwable t)  {
            System.err.println("FAILED to write table");
        }
        
        return null;
    }

}
