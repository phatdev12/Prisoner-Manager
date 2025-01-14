import { App } from "../Elysia";

export default async function auth(req: any, app: App) {
    const { authorization: token } = req.headers;
    const { store, error } = req;
    const tokenParts = token?.split(" ");
    console.log(tokenParts)
    if(!tokenParts || tokenParts.length != 2) return error(400, "Token is missing")
    if(tokenParts[0] !== "Bearer") return error(400, "Invalid token type")
    
    const auth = await app.db.auth(tokenParts[1]);
    if(auth === 4003) return false;

    return true;
}
