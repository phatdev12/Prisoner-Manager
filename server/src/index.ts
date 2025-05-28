import { Server } from 'bun';
import { App } from './Elysia';
import { isJsonString } from './utils/checker';

const app = new App();

class ChatSocketServer {
  static clients = new Map<string, Bun.ServerWebSocket>();
  static rooms = new Map<string, Set<string>>();

  static handleUpgrade(req: Request, server: Server) {
    const userId = new URL(req.url).searchParams.get('userId');
    if (!userId) {
      return new Response('Missing userId', { status: 400 });
    }
    const success = server.upgrade(req, { data: { userId } });
    return success ? undefined : new Response('WebSocket upgrade error', { status: 400 });
  }

  static websocket = {
    open(ws: Bun.ServerWebSocket) {
      const userId = (ws.data as any).userId;
      ChatSocketServer.clients.set(userId, ws);
      console.log(`User ${userId} connected`);
    },

    close(ws: Bun.ServerWebSocket) {
      const userId = (ws.data as any).userId;
      ChatSocketServer.clients.delete(userId);
      console.log(`User ${userId} disconnected`);

      // Remove user from all rooms
      for (const [roomId, members] of ChatSocketServer.rooms.entries()) {
        members.delete(userId);
        if (members.size === 0) ChatSocketServer.rooms.delete(roomId);
      }
    },

    message(ws: Bun.ServerWebSocket, message: string | Buffer) {
      try {
        // check if message is json string 
        if(isJsonString(message.toString()) === false) {
          ws.send(message.toString());
          console.log('Received non-JSON message:', message.toString());
          return;
        } else {
          const data = JSON.parse(message.toString());
          const senderId = (ws.data as any).userId;
          switch (data.type) {
            case 'join_room':
              ChatSocketServer.joinRoom(senderId, data.roomId);
              break;
            case 'send_message':
              ChatSocketServer.broadcastMessage(data.roomId, senderId, data.message);
              break;
          }
        }
      } catch (err) {
        console.error('Invalid message:', err);
      }
    }
  };

  static joinRoom(userId: string, roomId: string) {
    if (!this.rooms.has(roomId)) {
      this.rooms.set(roomId, new Set());
    }
    this.rooms.get(roomId)!.add(userId);
    console.log(`User ${userId} joined room ${roomId}`);
  }

  static broadcastMessage(roomId: string, senderId: string, message: any) {
    const members = this.rooms.get(roomId);
    if (!members) return;

    for (const userId of members) {
      const ws = this.clients.get(userId);
      if (ws && ws.readyState === 1) {
        ws.send(JSON.stringify({
          type: 'new_message',
          roomId,
          senderId,
          message
        }));
      }
    }
  }

  static sendMessageToUser(userId: string, message: any) {
    const ws = this.clients.get(userId);
    if (ws && ws.readyState === 1) {
      ws.send(JSON.stringify(message));
    }
  }
}

Bun.serve({
  port: 3000,
  fetch(req, server) {
    const userId = new URL(req.url).searchParams.get('userId');
    if (userId) {
      return ChatSocketServer.handleUpgrade(req, server);
    }
    return app.handle(req);
  },
  websocket: ChatSocketServer.websocket,
});

app.dynamicRoutes();
console.log("Server đã được khởi động trên port 3000");
