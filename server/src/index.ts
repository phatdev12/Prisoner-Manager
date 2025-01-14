import { Server } from "bun";
import { App } from "./Elysia";

const app = new App();
const clients = new Set<any>();

const server = Bun.serve({
    port: 3001,
    fetch(req, server) {
        if (server.upgrade(req)) return;
        return new Response("Expected a WebSocket connection", { status: 426 });
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
app.listen(3000);

console.log("Server da duoc khoi dong");