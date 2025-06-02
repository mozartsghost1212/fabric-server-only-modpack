# fabric-server-only-modpack

Assorted server-side only QOL mods for Minecraft Fabric API

## 📘 Included Mods and Descriptions

### 🔧 Performance Optimization Mods

1. **Lithium**  
   Optimizes game logic, including entity collisions and block ticking, to improve server performance without altering game mechanics.

2. **FerriteCore**  
   Reduces memory usage by optimizing the way Minecraft handles data, leading to lower RAM consumption on servers.

3. **Krypton**  
   Enhances the networking stack of Minecraft, resulting in reduced latency and improved connection stability for players.

4. **C2ME (Concurrent Chunk Management Engine)**  
   Improves chunk loading and generation by utilizing multiple threads, leading to faster world loading times.

5. **ThreadTweak**  
   Optimizes thread scheduling to improve server responsiveness and reduce lag spikes.

6. **ModernFix**  
   Addresses various performance bottlenecks and memory leaks, enhancing overall server stability.

7. **Clumps**  
   Groups experience orbs into a single entity to reduce lag caused by excessive entities in the world.

8. **Very Many Players (VMP)**  
   Optimizes server performance to handle a large number of concurrent players more efficiently.

9. **Connectivity**  
   Fixes common network issues such as login timeouts and packet errors, improving player connection reliability.

10. **ServerCore**  
    Provides various optimizations to enhance server performance, including improved tick times and reduced CPU usage.

11. **Chunky**  
    Allows for pre-generating chunks to reduce in-game lag caused by chunk loading during gameplay.

12. **Spark**  
    A performance profiling tool that helps identify and diagnose server lag issues through detailed reports.

### 🛠️ Quality-of-Life Enhancements

1. **No Chat Reports**  
   Disables the chat reporting feature introduced in newer Minecraft versions, preserving player privacy on servers.

2. **Fabric Tailor**  
   Allows customization of server messages, such as the Message of the Day (MOTD) and join/leave notifications.

3. **Simple Voice Chat**  
   Enables proximity-based voice communication between players without requiring client-side installations.

4. **Graves**  
   Creates a grave at the player's death location, storing their items safely until retrieval.

5. **Fabric Homes**  
   Allows players to set and teleport to home locations, enhancing navigation without client mods.

6. **Fabric Warps**  
   Enables the creation of warp points for quick travel across the server world.

7. **Chunky Pregenerator**  
   Works alongside Chunky to pre-generate world chunks, further reducing in-game lag.

8. **FabricTPA**  
   Introduces a teleport request system, allowing players to request teleportation to each other.

9. **KeepInventory Command**  
   Adds a command to toggle the 'keepInventory' game rule, simplifying inventory management upon death.

10. **Mobs Attempt Parkour**  
    Enhances mob AI to allow them to navigate complex terrain more effectively, increasing challenge and immersion.

### 🎒 Server Backpacks and Dependencies

1. **Server Backpacks**  
   Adds various types of backpacks that players can use to store items. The backpacks are implemented server-side and can be accessed through commands or by interacting with items.

2. **Polymer**  
   A library required by Server Backpacks to function correctly.

3. **Fabric API**  
   A set of tools and libraries required by many Fabric mods, including Server Backpacks.
