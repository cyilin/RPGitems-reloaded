/*
 *  This file is part of RPG Items.
 *
 *  RPG Items is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  RPG Items is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with RPG Items.  If not, see <http://www.gnu.org/licenses/>.
 */
package think.rpgitems.power;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import think.rpgitems.data.Locale;

/**
 * Power ranged.
 * <p>
 * Not a triggerable power.
 * Mark this item as ranged only.
 * </p>
 */
public class PowerRangedOnly extends Power {
    @Override
    public void init(ConfigurationSection s) {

    }

    @Override
    public void save(ConfigurationSection s) {

    }

    @Override
    public String getName() {
        return "rangedonly";
    }

    @Override
    public String displayText() {
        return ChatColor.GREEN + Locale.get("power.rangedonly");
    }
}
