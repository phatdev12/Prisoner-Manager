import { App } from "../../Elysia"
import auth from "../../utils/auth";

export default (app: App) => {
    app.group('/chat', (chat) => {
        chat.get('/rooms/:userId', async (req) => {
            if (req && req.headers && req.headers.authorization) {
                if (!auth(req, app)) return { error: "Unauthorized" }

                const { userId } = req.params;
                const rooms = await app.db.getUserChatRooms(userId);
                
                if (rooms === 4003) return { error: "User not found" }

                return { rooms }
            }
        });

        chat.post('/rooms/private', async (req) => {
            if (req && req.headers && req.headers.authorization) {
                if (!auth(req, app)) return { error: "Unauthorized" }
                const { userId, otherUserId } = req.body as { userId: string, otherUserId: string };

                if (!userId || !otherUserId) return { error: "userId or otherUserId is missing" }

                const room = await app.db.createPrivateRoom(userId, otherUserId);

                return { room }
            }
        });

        chat.post('/rooms/group', async (req) => {
            if (req && req.headers && req.headers.authorization) {
                if (!auth(req, app)) return { error: "Unauthorized" }
                const { userId, name, members } = req.body as { userId: string, name: string, members: string[] };

                if (!userId || !name || !members) return { error: "userId, name or members is missing" }

                const room = await app.db.createGroupRoom(userId, name, members);

                return { room }
            }
        });

        chat.get('/rooms/messages/:roomId', async (req) => {
            if (req && req.headers && req.headers.authorization) {
                if (!auth(req, app)) return { error: "Unauthorized" }

                const { roomId } = req.params;
                const messages = await app.db.getChatRoomMessages(roomId);

                return { messages }
            }
        });

        chat.post('/messages', async (req) => {
            if (req && req.headers && req.headers.authorization) {
                if (!auth(req, app)) return { error: "Unauthorized" }
                const { roomId, senderId, content } = req.body as { roomId: string, senderId: string, content: string };

                if (!roomId || !senderId || !content) return { error: "roomId, senderId or content is missing" }

                const message = await app.db.sendMessage(roomId, senderId, content);

                return { message }
            }
        });
        return chat;
    });
}