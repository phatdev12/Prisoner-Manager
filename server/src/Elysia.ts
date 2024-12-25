import { Elysia } from "elysia";
import fs from "fs";
import path from "path";
import { Database } from "./Database";

export class App extends Elysia {
  public db: Database;

  constructor() {
    super();
    this.db = new Database();
  }
  
  /**
   * Load all routes in the dynamic folder
   * @param dir 
   * @returns 
   */
  private loadRoutes(dir: string) {
    const elements = fs.readdirSync(dir);
    let routesList: Array<string> = [];
    for(const element of elements) {
        const elementPath = path.join(dir, element);
        const stat = fs.statSync(elementPath);

        if(stat.isDirectory()) {
            routesList = routesList.concat(this.loadRoutes(elementPath));
        } else {
            routesList.push(elementPath);
        }
    }
    return routesList;
  }

  /**
   * Initialize all dynamic routes
   */
  public dynamicRoutes() {
    const routes = this.loadRoutes(path.resolve(__dirname, "dynamic"));
    for(const route of routes) {
        const routePath = path.resolve(route);
        const routeModule = require(routePath);
        routeModule.default(this);
    }
  }
}