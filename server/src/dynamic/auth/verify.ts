import { App } from "../../Elysia"

export default (app: App) => {
    app.post("/auth/verify", async (req) => {
        const { authorization: token } = req.headers;
        const { store, error } = req;
        const tokenParts = token?.split(" ");
        console.log(tokenParts)
        if(!tokenParts || tokenParts.length != 2) return error(400, "Token is missing")
        if(tokenParts[0] !== "Prisoner") return error(400, "Invalid token type")
        
        const auth = await app.db.auth(tokenParts[1]);
        if(auth === 4003) return error(400, "Auth failed")
        else if(auth == 4008) {
            const heartbeatToken = tokenParts[1].split("-")[0]+"-"+tokenParts[1].split("-")[1];
            return { heartbeatToken }
        }

        return { success: true }
    })
}