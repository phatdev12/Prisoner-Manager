import { App } from "./Elysia";

const app = new App();
app.dynamicRoutes();
app.listen(3000);

console.log("Server da duoc khoi dong");