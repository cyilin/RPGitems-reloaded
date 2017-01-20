package think.rpgitems.power;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import think.rpgitems.Plugin;
import think.rpgitems.power.types.*;

import java.io.File;
import java.nio.file.Files;

public class PowerJavaScript extends Power implements PowerHit, PowerHitTaken, PowerProjectileHit, PowerLeftClick, PowerRightClick, PowerTick {
    public String display = "";
    public String script_name = "";
    private Function takeHit;
    private Function projectileHit;
    private Function hit;
    private Function tick;
    private Context cx;
    private Scriptable scope;
    private Function leftClick = null;
    private Function rightClick = null;

    public void loadJS() {
        cx = Context.enter();
        scope = new ImporterTopLevel(cx);
        scope.put("RPGitemsPlugin", scope, Plugin.plugin);
        File file = new File(Plugin.plugin.getDataFolder().getPath() + "/javascripts/", script_name);
        if (!file.isFile()) {
            Plugin.logger.warning("File " + file.getPath() + " does not exists");
            return;
        }
        try {
            String script = new String(Files.readAllBytes(file.toPath()), "utf-8");
            cx.evaluateString(scope, script, script_name, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        leftClick = scope.has("onLeftClick", scope) ? (Function) scope.get("onLeftClick", scope) : null;
        rightClick = scope.has("onRightClick", scope) ? (Function) scope.get("onRightClick", scope) : null;
        hit = scope.has("onHit", scope) ? (Function) scope.get("onHit", scope) : null;
        projectileHit = scope.has("onProjectileHit", scope) ? (Function) scope.get("onProjectileHit", scope) : null;
        tick = scope.has("onTick", scope) ? (Function) scope.get("onTick", scope) : null;
        takeHit = scope.has("onTakeHit", scope) ? (Function) scope.get("onTakeHit", scope) : null;
    }

    public boolean scriptExists() {
        File file = new File(Plugin.plugin.getDataFolder().getPath() + "/javascripts/", script_name);
        return (file.exists() && file.isFile());
    }

    @Override
    public void init(ConfigurationSection s) {
        display = s.getString("display", "");
        script_name = s.getString("script_name", "");
        loadJS();
    }

    @Override
    public void save(ConfigurationSection s) {
        s.set("display", display);
        s.set("script_name", script_name);
    }

    @Override
    public String getName() {
        return "javascript";
    }

    @Override
    public String displayText() {
        return ChatColor.GREEN + display;
    }

    @Override
    public void leftClick(Player player, Block clicked) {
        if (leftClick != null) {
            leftClick.call(cx, scope, scope, new Object[]{player, clicked});
        }
    }

    @Override
    public void rightClick(Player player, Block clicked) {
        if (rightClick != null) {
            rightClick.call(cx, scope, scope, new Object[]{player, clicked});
        }
    }

    @Override
    public void tick(Player player) {
        if (tick != null) {
            tick.call(cx, scope, scope, new Object[]{player});
        }
    }

    @Override
    public void hit(Player player, LivingEntity entity, double damage) {
        if (hit != null) {
            hit.call(cx, scope, scope, new Object[]{player, entity, damage});
        }
    }

    @Override
    public void projectileHit(Player player, Projectile arrow) {
        if (projectileHit != null) {
            projectileHit.call(cx, scope, scope, new Object[]{player, arrow});
        }
    }

    /**
     * @param target  player been hit
     * @param damager who made the damage
     * @param damage  damage value
     * @return new damage value, if nothing change, return a negative number.
     */
    @Override
    public double takeHit(Player target, Entity damager, double damage) {
        if (takeHit != null) {
            return (double) takeHit.call(cx, scope, scope, new Object[]{target, damager, damage});
        }
        return -1;
    }
}
