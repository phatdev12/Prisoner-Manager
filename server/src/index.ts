import { Server } from "bun";
import { App } from "./Elysia";

const app = new App();
const clients = new Set<any>();

Bun.serve({
    port: 3000,
    fetch(req, server) {
        if (server.upgrade(req)) return;
        return app.handle(req);
    },  
    websocket: {
        open(ws) {
            console.log("Client connected");
            clients.add(ws);
        },
        message(ws, message) {
            console.log(`Broadcasting: ${message}`);
            for (const client of clients) {
                if (client.readyState === 1) {
                    client.send(message);
                }
            }
        },
        close(ws) {
            console.log("Client disconnected");
            clients.delete(ws);
        },
    },
});

app.dynamicRoutes();

console.log("Server da duoc khoi dong");